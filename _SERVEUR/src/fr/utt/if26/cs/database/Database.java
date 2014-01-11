package fr.utt.if26.cs.database;

import java.util.ArrayList;

import org.bson.BSONObject;

import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;

public abstract class Database {
	
	private static Database db;
	
	protected Database(){
		Database.db = this;
	}
	
	public abstract boolean insertBean(DataBean bean);
	public abstract boolean updateBean(DataBean bean);
	public abstract boolean removeBean(DataBean bean);
	
	public abstract DataBean getBean(BSONObject datas);
	
	/**
	 * find a bean using a key
	 * @param key
	 * @param value
	 * @return
	 * @throws BeanException 
	 */
	public abstract DataBean getBean(String key, String value) throws BeanException;
	
	/**
	 * find some beans un the DB
	 * @param datas : key and value, added to the WHERE clause
	 * 			ex : "name":"john"
	 * @return list of beans found in DB
	 * @throws BeanException 
	 * @throws NumberFormatException 
	 */
	public abstract ArrayList<DataBean> findBeans(BSONObject datas) throws BeanException;
	
	public abstract void open();
	public abstract void close();
	
	public static Database getInstance(){
		return null;
	}
	public static Database getDb() {
		return db;
	}
	public static void setDb(Database db) {
		Database.db = db;
	}
}
