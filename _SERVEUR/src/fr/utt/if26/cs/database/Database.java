package fr.utt.if26.cs.database;

import java.util.ArrayList;

import org.bson.BSONObject;

import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;
/**
 * Generic database class
 * @author steven
 *
 */
public abstract class Database {
	
	private static Database db;
	
	protected Database(){
		Database.db = this;
	}
	
	/**
	 * Singleton
	 * @return global database instance
	 */
	public static Database getInstance(){
		return null;
	}
	
	/**
	 * get current DB
	 * @return current DB instance
	 */
	public static Database getDb() {
		return db;
	}
	
	/**
	 * Open the DB,
	 * set resources
	 */
	public abstract void open();
	
	/**
	 * Close the DB,
	 * unset resources
	 */
	public abstract void close();
	
	/**
	 * Choose the set to request in (collection/table)
	 * @param index : set index
	 */
	public abstract void chooseSet(int index);
	/**
	 * Insert a bean into DB
	 * @param bean : bean to insert
	 * @return true if succeeded 
	 */
	public abstract boolean insertBean(DataBean bean);
	
	/**
	 * Update a bean from DB
	 * @param bean : bean to update (id must be set)
	 * @return true if succeeded
	 */
	public abstract boolean updateBean(DataBean bean);
	
	/**
	 * Remove a bean from DB
	 * @param bean : bean to remove (id must be set)
	 * @return true if succeeded
	 */
	public abstract boolean removeBean(DataBean bean);
	
	/**
	 * remove entries from DB using selection 'datas'
	 * @param datas : entries to remove
	 * @return true if succeeded
	 */
	public abstract boolean removeBeans(BSONObject datas);
	
	/**
	 * find a bean using a key attribute
	 * @param key : attribute
	 * @param value : value to look for
	 * @return bean found or null
	 * @throws BeanException 
	 */
	public abstract DataBean getBean(String key, String value) throws BeanException;
	
	/**
	 * find some beans in the DB
	 * @param datas : key and value, added to the WHERE clause
	 * 			ex : "name":"john"
	 * @return list of beans found in DB or null
	 * @throws BeanException 
	 */
	public abstract ArrayList<DataBean> findBeans(BSONObject datas) throws BeanException;
}
