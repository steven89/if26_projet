package fr.utt.if26.cs.database;

import org.bson.BSONObject;

import fr.utt.if26.cs.model.DataBean;

public abstract class Database {
	
	private static Database db;
	
	public abstract boolean insertBean(DataBean bean);
	public abstract boolean updateBean(DataBean bean);
	public abstract boolean removeBean(DataBean bean);
	
	public abstract DataBean getBean(BSONObject datas);
	public abstract DataBean getBean(String key, String value);
	
	public abstract void open();
	public abstract void close();
	
	public static Database getInstance(){
		return null;
	}
	
	
}
