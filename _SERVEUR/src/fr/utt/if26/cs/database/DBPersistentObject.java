package fr.utt.if26.cs.database;

import org.bson.BSONObject;

import fr.utt.if26.cs.ServiceManager;

/***
 * <p>An interface that define basic required functions to insure
 * the persistence of an object on a database.</p>
 * 
 * <p>No direct database Manipulation should be used here, 
 * instead use a wrapper, for instance an implementation of DBManager.</p>
 * 
 * @see DBManager
 *
 */
public interface DBPersistentObject {
	/***
	 * <p>Save the current DBPersistentObject in the database, 
	 * delegating database manipulation to the DBPersistentObject's current DBManager.
	 * </p>
	 * 
	 * @see DBManager#insert(Object...)
	 */
	public void save();
	
	/***
	 * <p>Save the current DBPersistentObject in the database, 
	 * delegating database manipulation to each specified DBManager.
	 * </p>
	 * 
	 * @param DBManagers
	 * 					An array of DBManager objects.
	 * 
	 * @see DBManager#insert(Object...)
	 * @see ServiceManager#registerService(String, Object)
	 * @see ServiceManager#getServices(String...)
	 * 
	 */
	public void save(DBManager... DBManagers);
	
	/***
	 * <p>Synchronized the current DBPersistentObject with its counterpart in the database :
	 * merging attributes and saving the resulting object on the database if necessary.</p>
	 */
	public void sync();
	
	/***
	 * 
	 * @return The DBPersistentObject's BSON representation.
	 * 
	 * @see BSONObject
	 */
	public BSONObject getBSONRepresentation();
	
	/***
	 * 
	 * @return The DBPersistentObject's BSON representation.
	 * 
	 */
	public String getJSONStringRepresentation();
}
