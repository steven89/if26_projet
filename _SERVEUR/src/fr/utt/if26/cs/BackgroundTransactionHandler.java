package fr.utt.if26.cs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import fr.utt.if26.cs.database.DatabaseManager;

/**
 * Application Lifecycle Listener implementation class BackgroundTransactionHandler
 *
 */
@WebListener
public class BackgroundTransactionHandler implements ServletContextListener {
	
	private DatabaseManager dbManager;
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	// init reduction des transaction 
    	System.out.println("init databases");
    	dbManager = DatabaseManager.getInstance();
    	dbManager.getBase(DatabaseManager.USERS).open();
    	dbManager.getBase(DatabaseManager.TRANSACTIONS).open();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	System.out.println("close databases");
    	dbManager.getBase(DatabaseManager.USERS).close();
    	dbManager.getBase(DatabaseManager.TRANSACTIONS).close();
    }
	
}
