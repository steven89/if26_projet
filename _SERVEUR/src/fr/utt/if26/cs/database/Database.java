package fr.utt.if26.cs.database;

import java.util.ArrayList;

import org.bson.BSONObject;

import fr.utt.if26.cs.model.DataBean;

public abstract class Database {
	
	private static Database db;
	
	public abstract boolean insertBean(DataBean bean);
	public abstract boolean updateBean(DataBean bean);
	public abstract boolean removeBean(DataBean bean);
	
	public abstract DataBean getBean(BSONObject datas);
	/**
	 * find a bean using a key
	 * @param key
	 * @param value
	 * @return
	 */
	public abstract DataBean getBean(String key, String value);
	
	/**
	 * find some beans un the DB
	 * @param datas : key and value, added to the WHERE clause
	 * 			ex : "name":"john"
	 * @return list of beans found in DB
	 */
	public abstract ArrayList<DataBean> findBeans(BSONObject datas);
	
	public abstract void open();
	public abstract void close();
	
	public static Database getInstance(){
		return null;
	}
}
