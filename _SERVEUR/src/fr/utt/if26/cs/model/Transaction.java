package fr.utt.if26.cs.model;

import java.util.Date;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import fr.utt.if26.cs.database.DatabaseHelper;
import fr.utt.if26.cs.database.DBPersistentObject;

public class Transaction implements DBPersistentObject{

	protected int montant;
	protected String crediteur;
	protected String destinataire;
	protected Date date;
	//l'id est stock� sous un wrapper Long >> autorise les test conditionnels (id!=null)
	protected String id;
	protected static DatabaseHelper defaultDBManager;
	
	
	public Transaction(int somme, String crediteurPseudo, String destinatairePseudo){
		this.montant = somme;
		this.crediteur = crediteurPseudo;
		this.destinataire = destinatairePseudo;
	}
	
	public Transaction(String dbID, int somme, String crediteurPseudo, String destinatairePseudo){
		this.montant = somme;
		this.crediteur = crediteurPseudo;
		this.destinataire = destinatairePseudo;
		this.id = dbID;
		//la date sera instanci�e � l'appel de la m�thode this.save()
	}
	
	public String getId(){
		return this.id;
	}
	
	@Override
	public void save() {
		this.save(defaultDBManager);
	}
	
	@Override
	public void save(DatabaseHelper... DBManagers) {
		BasicDBObject data = (BasicDBObject) this.getBSONRepresentation();
		this.date = new Date();
		for(DatabaseHelper _dbManager : DBManagers){
			if(this.id == null){
				data.put("insertion_date", this.date);
			}else{
				data.put("update_date", this.date);
				data.put(_dbManager.getObjectIDKey(), this.id);
			}
			_dbManager.insert(data);
		}
	}
	
	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}
	
	public static void setDefaultDBManager(DatabaseHelper dbManager){
		defaultDBManager = dbManager;
	}
	
	@Override
	public BSONObject getBSONRepresentation() {
		BasicBSONObject BSONData = new BasicBSONObject();
		BSONData.put("montant", this.montant);
		BSONData.put("crediteur", this.crediteur);
		BSONData.put("destinataire", this.destinataire);
		return BSONData;
	}

	@Override
	public String getJSONStringRepresentation() {
		return JSON.serialize(this.getBSONRepresentation());
	}
}
