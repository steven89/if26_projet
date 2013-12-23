package fr.utt.if26.cs.database.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.model.DataBean;

public class SQLDatabase extends Database {
	
	private static SQLDatabase db=null;
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String url = "jdbc:mysql://localhost:3306/if26_projet";
	private final static String user = "root";
	private final static String passwd = "";
	
	private SQLHelper sqlHelper;
	
	private Connection connexion = null;
	
	private SQLDatabase(){
		try {
			Class.forName( SQLDatabase.driver );
		} catch (ClassNotFoundException e) {
			// erreur driver
			e.printStackTrace();
		}
	}
	
	public static SQLDatabase getInstance(){
		return (SQLDatabase.db!=null)?SQLDatabase.db:new SQLDatabase();
	}
	
	@Override
	public void open() {
		try {
			this.connexion = DriverManager.getConnection(SQLDatabase.url, SQLDatabase.user, SQLDatabase.passwd);
			this.sqlHelper = new SQLHelper(this.connexion, "users");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			if(this.connexion!=null)
				this.connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean insertBean(DataBean bean) {
		sqlHelper.insert(bean.getHashRepresentation());
		return false;
	}

	@Override
	public boolean updateBean(DataBean bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeBean(DataBean bean) {
		// TODO Auto-generated method stub
		return false;
	}
}
