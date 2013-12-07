package fr.utt.if26.cs.model;

import java.util.Date;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import fr.utt.if26.cs.database.DBManager;
import fr.utt.if26.cs.database.DBPersistentObject;

public class Transaction implements DBPersistentObject{

	protected int montant;
	protected String crediteur;
	protected String destinataire;
	protected Date date;
	//l'id est stocké sous un wrapper Long >> autorise les test conditionnels (id!=null)
	protected Long id;
	protected static DBManager defaultDBManager;
	
	
	public Transaction(int somme, String crediteurPseudo, String destinatairePseudo){
		this.montant = somme;
		this.crediteur = crediteurPseudo;
		this.destinataire = destinatairePseudo;
	}
	
	public Transaction(long dbID, int somme, String crediteurPseudo, String destinatairePseudo){
		this.montant = somme;
		this.crediteur = crediteurPseudo;
		this.destinataire = destinatairePseudo;
		this.id = dbID;
		//la date sera instanciée à l'appel de la méthode this.save()
	}
	
	@Override
	public void save() {
		this.save(defaultDBManager);
	}
	
	@Override
	public void save(DBManager... DBManagers) {
		// TODO Auto-generated method stub
		BasicDBObject data = (BasicDBObject) this.getBSONRepresentation();
		this.date = new Date();
		for(DBManager _dbManager : DBManagers){
			if(this.id == null){
				data.put("insertion_date", this.date);
			}else{
				data.put("update_date", this.date);
				data.put(_dbManager.getObjectIDKey(), this.id);
			}
			_dbManager.insert("TODO", data);
		}
	}
	
	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}
	
	public static void setDefaultDBManager(DBManager dbManager){
		defaultDBManager = dbManager;
	}
	
	@Override
	public BSONObject getBSONRepresentation() {
		// TODO Auto-generated method stub
		BasicBSONObject BSONData = new BasicBSONObject();
		BSONData.put("montant", this.montant);
		BSONData.put("crediteur", this.crediteur);
		BSONData.put("destinataire", this.destinataire);
		return BSONData;
	}

	@Override
	public String getJSONStringRepresentation() {
		// TODO Auto-generated method stub
		return JSON.serialize(this.getBSONRepresentation());
	}
}
