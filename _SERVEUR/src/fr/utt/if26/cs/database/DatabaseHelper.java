package fr.utt.if26.cs.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.BSONObject;


/***
 * <p> An interface which define a basic set of function that services which manipulate
 * a database should implement </p>
 * 
 */
public interface DatabaseHelper {
	/*** 
	 * Save objects in a database
	 * @param obj TODO
	 */
	public void insert(BSONObject datas);
	
	/*** 
	 * Save objects in a database
	 * @param query TODO
	 */
	public void insert(String query);
	
	/***
	 * 
	 * @param query
	 * 				An object which represent a query to perform against the database
	 * @return an implementation of Iterator to fetch results
	 */
	public ArrayList<BSONObject> find(BSONObject datas);
	
	/**
	 * Find one entry (use only with 'unique' fields)
	 * @param key : field to search in
	 * @param value : value to search for
	 * @return entry corresponding to requestd value
	 */
	public BSONObject findByKey(String key, String value);
	
	/***
	 * 
	 * @return The name of the key that uses the database the current DBManager represent (ie : "_id")
	 */
	public String getObjectIDKey();
	
	public void update(BSONObject datas);
}
