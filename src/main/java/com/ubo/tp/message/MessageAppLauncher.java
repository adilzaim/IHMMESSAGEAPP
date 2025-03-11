package com.ubo.tp.message;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.ihm.MessageApp;
import com.ubo.tp.message.mock.MessageAppMock;

/**
 * Classe de lancement de l'application.
 *
 * @author S.Lucas
 */
public class MessageAppLauncher {

	/**
	 * Indique si le mode bouchoné est activé.
	 */
	protected static boolean IS_MOCK_ENABLED = false;

	/**
	 * Launcher.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		IDatabase database = new Database();

		EntityManager entityManager = new EntityManager(database);

		if (IS_MOCK_ENABLED) {
			MessageAppMock mock = new MessageAppMock(database, entityManager);
			mock.showGUI();
		}

		MessageApp messageApp = new MessageApp(database, entityManager);
		messageApp.init();
		messageApp.show();

	}
}
