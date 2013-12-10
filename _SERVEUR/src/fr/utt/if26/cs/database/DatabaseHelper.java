package fr.utt.if26.cs.database;

import java.util.Iterator;

import org.bson.BSONObject;


/***
 * <p> An interface which define a basic set of function that services which manipulate
 * a database should implement </p>
 * 
 */
public interface DatabaseHelper {
	/*** 
	 * Save objects in a database
	 * 
	 * @param filterQuery
	 * 					An Object used to specify where and how to insert the data on the database
	 * @param BSONObjects
	 *             		Array of documents to save, in a BSON representation
	 * @return an array of objects after insertion, useful when objects are modified during insertion (ie : updating the id)
	 */
	public Object[] insert(BSONObject... BSONObjects);
	
	/*** 
	 * Save objects in a database
	 * @param JSONStrings
	 *              	Array of documents to save, in a JSON string representation
	 * @return an array of objects after insertion, useful when objects are modified during insertion (ie : updating the id)
	 */
	public Object[] insert(String... JSONStrings);
	
	/***
	 * Remove objects from a database
	 * @param BSONObjects
	 * 					Array of documents to remove, in a BSON representation
	 * @return true or false depending on the removal of the objects
	 */
	public boolean remove(BSONObject... BSONObjects);
	
	/***
	 * Remove objects from a database
	 * @param filterQuery
	 * 					An Object used to specify where and how to insert the data on the database
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
	public Iterator<Object> find(Object query);
	
	/***
	 * 
	 * @return The name of the key that uses the database the current DBManager represent (ie : "_id")
	 */
	public String getObjectIDKey();
}
