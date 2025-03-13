package com.ubo.tp.message.ihm;

import java.io.File;
import java.util.List;

import com.ubo.tp.message.ihm.searchUser.ControllerSearch;
import com.ubo.tp.message.ihm.searchUser.SearchUserModel;
import com.ubo.tp.message.ihm.searchUser.SearchUserView;
import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.directory.IWatchableDirectory;
import com.ubo.tp.message.core.directory.WatchableDirectory;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.ListUserComponent.*;
import com.ubo.tp.message.ihm.listener.LoginListener;
import com.ubo.tp.message.ihm.messageComponent.MessageAnnouncementView;
import com.ubo.tp.message.ihm.messageComponent.MessageController;
import com.ubo.tp.message.ihm.messageComponent.MessageModel;
import com.ubo.tp.message.ihm.messageComponent.MessagePanel;
import com.ubo.tp.message.ihm.serviceUser.Service;
import com.ubo.tp.message.ihm.userComponent.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import static com.ubo.tp.message.ihm.LoginView.showPopup;
import static com.ubo.tp.message.ihm.MessageAppMainView.chooseDirectoryOnStartup;

/**
 * Classe principale l'application.
 *
 * @author S.Lucas
 */
public class MessageApp implements IDatabaseObserver, UserModelObserver {
	/**
	 * Base de données.
	 */
	protected IDatabase mDatabase;

	/**
	 * Gestionnaire des entités contenu de la base de données.
	 */
	protected EntityManager mEntityManager;

	/**
	 * Vue principale de l'application.
	 */
	protected MessageAppMainView mMainView;

	/**
	 * Classe de surveillance de répertoire
	 */
	protected IWatchableDirectory mWatchableDirectory;

	/**
	 * Répertoire d'échange de l'application.
	 */
	protected String mExchangeDirectoryPath;

	/**
	 * Nom de la classe de l'UI.
	 */
	protected String mUiClassName;

	private Service userService;

	private MessageAnnouncementView messageAnnouncementView;
	private UserModel userModel;

	/**
	 * Constructeur.
	 *
	 * @param entityManager
	 * @param database
	 */
	public MessageApp(IDatabase database, EntityManager entityManager) {
		this.mDatabase = database;
		this.mEntityManager = entityManager;
		this.userService = new Service(this.mDatabase, this.mEntityManager);
		this.userModel = new UserModel();
		this.userModel.addObserver(this);
	}

	/**
	 * Initialisation de l'application.
	 */
	public void init() {
		this.mDatabase.addObserver(this);
		// Init du look and feel de l'application
		this.initLookAndFeel();

		// Initialisation du gui sera fait après le choix du répertoire
	}

	/**
	 * Initialisation du look and feel de l'application.
	 */
	protected void initLookAndFeel() {
		// Peut rester vide ou ajouter des styles JavaFX
	}

	/**
	 * Initialisation de l'interface graphique.
	 */
	protected void initGui() {
		Platform.runLater(() -> {
			String path = chooseDirectoryOnStartup();

			if (path != null && this.isValideExchangeDirectory(new File(path))) {
				System.out.println("Path found: " + path);
				this.initDirectory(path);

				// Création de la vue de connexion
				LoginView loginView = new LoginView();
				loginView.setLoginListener(new LoginListener() {
					@Override
					public void loginVerify(String username, String tag) {
						doLogin(username, tag);
					}

					@Override
					public void createUser(String name, String tag, String avatarPath, String password) {
						createNewUser(name, tag, avatarPath, password, mEntityManager);
					}
				});

				// Création de la vue principale
				mMainView = new MessageAppMainView(path, loginView);
				mMainView.setListener(() -> doQuit());
				mMainView.show(); // Assurez-vous que cette méthode existe dans MessageAppMainView
			} else {
				System.out.println("Path not found or invalid!");
				// Peut-être réessayer ou quitter proprement
				System.exit(1);
			}
		});
	}

	protected void doQuit() {
		System.exit(0);
	}

	protected void doLogin(String name, String tag) {
		User user = this.userService.doLogin(name, tag, this.userModel);

		if (user == null) {
			showPopup("Identifiants erronés", false);
		}
	}

	protected void createNewUser(String name, String tag, String path, String password, EntityManager mEntityManager) {
		if (!(this.userService.createUser(name, tag, path, password, mEntityManager))) {
			showPopup("Tag déjà utilisé, choisissez un autre tag !", false);
		}
	}

	/**
	 * Indique si le fichier donné est valide pour servir de répertoire d'échange
	 *
	 * @param directory , Répertoire à tester.
	 */
	protected boolean isValideExchangeDirectory(File directory) {
		// Valide si répertoire disponible en lecture et écriture
		return directory != null && directory.exists() && directory.isDirectory() && directory.canRead()
				&& directory.canWrite();
	}

	/**
	 * Initialisation du répertoire d'échange.
	 *
	 * @param directoryPath
	 */
	protected void initDirectory(String directoryPath) {
		mExchangeDirectoryPath = directoryPath;
		mWatchableDirectory = new WatchableDirectory(directoryPath);
		mEntityManager.setExchangeDirectory(directoryPath);

		mWatchableDirectory.initWatching();
		mWatchableDirectory.addObserver(mEntityManager);
	}

	public void show() {
		// Lancer l'initialisation de l'interface graphique
		initGui();
	}

	@Override
	public void notifyMessageAdded(Message addedMessage) {
		System.out.println("---> Message ajouté : " + addedMessage.toString());
		if (userModel.getCurrentUser() == null) return;
		List<Message> liste = this.userService.getMessageUser(this.userModel.getCurrentUser());
		this.messageAnnouncementView.unsetMessageList(liste);
		this.mMainView.setRightBottomComponent(this.messageAnnouncementView);
	}

	@Override
	public void notifyMessageDeleted(Message deletedMessage) {
		System.out.println("---> Message supprimé : " + deletedMessage.getText());
	}

	@Override
	public void notifyMessageModified(Message modifiedMessage) {
		System.out.println("---> Message modifié : " + modifiedMessage.getText());
	}

	@Override
	public void notifyUserAdded(User addedUser) {
		System.out.println("---> Utilisateur ajouté : " + addedUser.toString());
	}

	@Override
	public void notifyUserDeleted(User deletedUser) {
		System.out.println("---> Utilisateur supprimé : " + deletedUser.getName());
	}

	@Override
	public void notifyUserModified(User modifiedUser) {
		System.out.println("--->Utilisateur modifié : " + modifiedUser.getName());
	}

	@Override
	public void onUserLoggedIn(User user) {
		ModelData modelData = new ModelData(this.mDatabase);
		ListService service = new ListService(this.mDatabase);
		service.initializeFollowLists(this.userModel.getCurrentUser(), modelData);
		MainView mainView = new MainView(this.userModel.getCurrentUser(), modelData);
		ListUserController listUserController = new ListUserController(userModel, mainView, modelData, service, this.userModel.getCurrentUser(), this.mDatabase);
		SearchUserModel modelSearch = new SearchUserModel(this.mDatabase);
		SearchUserView viewSearchUser = new SearchUserView(modelSearch);
		UserMapView userView = new UserMapView(this.userModel.getCurrentUser(), viewSearchUser);
		UserController userController = new UserController(this.userModel, this.mMainView, userView, mainView);
		this.messageAnnouncementView = new MessageAnnouncementView(this.userService.getMessageUser(this.userModel.getCurrentUser()));
		MessageModel modelMessage = new MessageModel(this.mDatabase, this.userModel.getCurrentUser());
		MessageController messageController = new MessageController(this.userModel, new MessagePanel(this.userModel.getCurrentUser(), modelMessage), this.mDatabase, this.mMainView, this.messageAnnouncementView, this.mEntityManager);
		this.initializeListener(service, modelData, userView, mainView);

		ControllerSearch controllerSearch = new ControllerSearch(this.mDatabase, modelSearch, viewSearchUser, this.mMainView, userView);
	}

	private void initializeListener(ListService service, ModelData modelData, UserMapView userView, MainView mainView) {
		ListListener listListener = new ListListener() {
			@Override
			public void follow(User userToFollow) {
				service.follow(userModel.getCurrentUser(), userToFollow, modelData, mEntityManager);
			}

			@Override
			public void unFollow(User userToUnFollow) {
				service.unfollow(userModel.getCurrentUser(), userToUnFollow, modelData, mEntityManager);
			}

			@Override
			public void back() {
				mMainView.setUserMapView(userView);
			}
		};
		mainView.setListener(listListener);
	}

	@Override
	public void onUserLoggedOut() {
		this.mMainView.setLoginView();
	}

	/**
	 * Classe interne pour gérer l'initialisation de JavaFX
	 */
	public static class MessageAppFX extends Application {
		private static MessageApp messageApp;

		public static void launchApp(MessageApp app) {
			messageApp = app;
			launch();
		}

		@Override
		public void start(Stage primaryStage) {
			// Initialiser JavaFX ici si nécessaire
			// puis déléguer à MessageApp
			if (messageApp != null) {
				messageApp.init();
				messageApp.show();
			}
		}
	}
}