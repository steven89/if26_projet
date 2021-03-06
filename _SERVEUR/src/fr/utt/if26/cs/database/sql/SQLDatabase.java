package fr.utt.if26.cs.database.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bson.BSONObject;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.TransactionsUtils;

public class SQLDatabase extends Database {
	
	private static SQLDatabase db=null;
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String url = "jdbc:mysql://localhost:3306/if26_projet";
	private final static String user = "root";
	private final static String passwd = "";
	
	private SQLHelper sqlHelper;
	
	private Connection connexion = null;
	
	private SQLDatabase(){
		super();
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
	
	public void chooseSet(int index){
		
	}
	
	@Override
	public void open() {
		System.out.println("opening connexion on SQL database");
		try {
			this.connexion = DriverManager.getConnection(SQLDatabase.url, SQLDatabase.user, SQLDatabase.passwd);
			this.sqlHelper = new SQLHelper(this.connexion, "users");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		System.out.println("closing connexion on SQL database");
		try {
			if(this.connexion!=null)
				this.connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean insertBean(DataBean bean) {
		String result = sqlHelper.insert(bean.toBSON(
			new String[]{"email","pass","nom","prenom","tag","token"}
		));
		if(result.equals("ok"))
			return true;
		else
			return false;
	}

	@Override
	public boolean updateBean(DataBean bean) {
		sqlHelper.update(bean.toBSON(new String[]{"id","email","pass","nom","prenom","tag","token"}));
		return false;
	}

	@Override
	public boolean removeBean(DataBean bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataBean getBean(String key, String value) throws BeanException {
		DataBean bean;
		BSONObject datas = sqlHelper.findByKey(key, value);
		if(datas!=null && datas.containsField("id")){
			bean = new User(
					(String) datas.get("id"), 
					(String) datas.get("email"),
					(String) datas.get("pass"), 
					(String) datas.get("prenom"), 
					(String) datas.get("nom"),
					(String) datas.get("tag"));
			TransactionsUtils.applyTransactionsOnUser(bean);
			if(datas.containsField("token")){
				((User) bean).setToken((String) datas.get("token"));
			}
		}
		else
			bean = null;
		return bean;
	}

	@Override
	public ArrayList<DataBean> findBeans(BSONObject datas) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeBeans(BSONObject datas) {
		// TODO Auto-generated method stub
		return false;
	}
}
