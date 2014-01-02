package fr.utt.if26.cs.model;

import java.util.Date;


public class Transaction extends DataBean {

	protected int montant;
	protected String crediteur;
	protected String destinataire;
	protected Date date;
	protected String id;
	
	
	public Transaction(int somme, String crediteurPseudo, String destinatairePseudo){
		this.montant = somme;
		this.crediteur = crediteurPseudo;
		this.destinataire = destinatairePseudo;
		this.export = new String[] {"montant", "crediteur", "destinataire"};
	}
	
	public Transaction(String dbID, int somme, String crediteurPseudo, String destinatairePseudo){
		this.montant = somme;
		this.crediteur = crediteurPseudo;
		this.destinataire = destinatairePseudo;
		this.id = dbID;
		this.export = new String[] {"id", "montant", "crediteur", "destinataire",};
		//la date sera instanci�e � l'appel de la m�thode this.save()
	}
	
	public String getId(){
		return this.id;
	}
	
	public int getMontant(){
		return this.montant;
	}
	
	public String getCrediteur(){
		return this.crediteur;
	}
	
	public String getDestinataire(){
		return this.destinataire;
	}
}
