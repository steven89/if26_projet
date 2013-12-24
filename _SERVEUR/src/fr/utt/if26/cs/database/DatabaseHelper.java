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
	 * @param BSONObject TODO
	 */
	public void insert(BSONObject BSONObject);
	
	/*** 
	 * Save objects in a database
	 * @param JSONString TODO
	 */
	public void insert(String JSONString);
	
	public void insert(HashMap<String, String> map);
	
	/***
	 * Remove objects from a database
	 * @param BSONObjects
	 * 					Array of documents to remove, in a BSON representation
	 * @return true or false depending on the removal of the objects
	 */
	public boolean remove(BSONObject... BSONObjects);
	
	/***
	 * Remove objects from a database
	 * @param object
	 * 				Array of documents to remove, in a JSON string representation
	 * @return true or false depending on the removal of the objects
	 */
	public boolean remove(String... JSONStrings);
	
	/***
	 * 
	 * @param query
	 * 				An object which represent a query to perform against the database
	 * @return an implementation of Iterator to fetch results
	 */
	public ArrayList<BSONObject> find(Object query);
	
	/***
	 * 
	 * @param query
	 * 				An object which represent a query to perform against the database
	 * @return an implementation of Iterator to fetch results
	 */
	public ArrayList<BSONObject> find(BSONObject query);
	
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
}
