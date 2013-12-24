package fr.utt.if26.cs.model;

import java.util.Date;


public class Transaction {

	protected int montant;
	protected String crediteur;
	protected String destinataire;
	protected Date date;
	//l'id est stock� sous un wrapper Long >> autorise les test conditionnels (id!=null)
	protected String id;
	
	
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
}
