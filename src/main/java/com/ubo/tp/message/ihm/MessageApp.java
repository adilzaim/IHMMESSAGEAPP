package main.java.com.ubo.tp.message.ihm;

import java.io.File;

import main.java.com.ubo.tp.message.core.EntityManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.core.directory.IWatchableDirectory;
import main.java.com.ubo.tp.message.core.directory.WatchableDirectory;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.listener.LoginListener;
import main.java.com.ubo.tp.message.ihm.serviceUser.Service;

import static main.java.com.ubo.tp.message.ihm.LoginView.showPopup;
import static main.java.com.ubo.tp.message.ihm.MessageAppMainView.chooseDirectoryOnStartup;


/**
 * Classe principale l'application.
 *
 * @author S.Lucas
 */
public class MessageApp implements IDatabaseObserver {
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


	/**
	 * Constructeur.
	 *
	 * @param entityManager
	 * @param database
	 */
	public MessageApp(IDatabase database, EntityManager entityManager) {
		this.mDatabase = database;
		this.mEntityManager = entityManager;
		this.userService = new Service(this.mDatabase,this.mEntityManager);

	}

	/**
	 * Initialisation de l'application.
	 */
	public void init() {
		this.mDatabase.addObserver(this);
		// Init du look and feel de l'application
		this.initLookAndFeel();

		// Initialisation de l'IHM
		this.initGui();


	}

	/**
	 * Initialisation du look and feel de l'application.
	 */
	protected void initLookAndFeel() {
	}

	/**
	 * Initialisation de l'interface graphique.
	 */
	protected void initGui() {
		// Choisir un répertoire d'échange (appel à la méthode existante)
		this.initDirectory();



	}

	/**
	 * Initialisation du répertoire d'échange (depuis la conf ou depuis un file
	 * chooser). <br/>
	 * <b>Le chemin doit obligatoirement avoir été saisi et être valide avant de
	 * pouvoir utiliser l'application</b>
	 */
	protected void initDirectory() {
		String path = chooseDirectoryOnStartup();
	if(this.isValideExchangeDirectory(new File(path))) {
		System.out.println("path found");
		this.initDirectory(path);
		LoginView loginView = new LoginView();
		loginView.setLoginListener(new LoginListener() {
			@Override
			public void loginVerify(String username, String tag) {
				 doLogin(username,tag);
			}

			@Override
			public void createUser(String name, String tag, String avatarPath ,String password) {
				createNewUser(name,tag, avatarPath,password);
			}
		});
		mMainView = new MessageAppMainView(path,loginView);



		mMainView.setListener(()-> doQuit());
		mMainView.setVisible(true);

	}else {
		System.out.println("path not found");
	}


	}

	protected void doQuit()

	{

			System.exit(0);

	}

	protected void doLogin(String name, String tag){
		User user = this.userService.doLogin(name,tag);
		if(user != null) {
			this.mMainView.setUserMapView(user);
		}else {
			showPopup("identifiants erroné", false);
		}
	}

	protected void createNewUser(String name, String tag , String path, String password){
		if(!(this.userService.createUser(name,tag,path, password))) showPopup("tag déja utilisé , choisisez un autre tag !", false);


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

	}

	@Override
	public void notifyMessageAdded(Message addedMessage) {
		System.out.println("---> Message ajouté : " + addedMessage.getText());
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
		System.out.println("---> Utilisateur ajouté : " + addedUser.getName());
	}

	@Override
	public void notifyUserDeleted(User deletedUser) {
		System.out.println("---> Utilisateur supprimé : " + deletedUser.getName());
	}

	@Override
	public void notifyUserModified(User modifiedUser) {
		System.out.println("--->Utilisateur modifié : " + modifiedUser.getName());
	}

}
