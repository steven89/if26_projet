package fr.utt.if26.cs.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Transaction extends DataBean {

	protected int amount;
	protected String from;
	protected String to;
	protected String date;
	protected String id;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyy");
	
	
	public Transaction(int somme, String crediteurPseudo, String destinatairePseudo){
		this.amount = somme;
		this.from = crediteurPseudo;
		this.to = destinatairePseudo;
		this.date = dateFormat.format(Calendar.getInstance().getTime());
		this.export = new String[] {"amount", "from", "to", "date"};
	}
	
	public Transaction(String dbID, int somme, String crediteurPseudo, String destinatairePseudo, String date){
		this.amount = somme;
		this.from = crediteurPseudo;
		this.to = destinatairePseudo;
		this.id = dbID;
		this.export = new String[] {"id", "amount", "from", "to","date"};
		//la date sera instanci�e � l'appel de la m�thode this.save()
	}
	
	public String getId(){
		return this.id;
	}
	
	public int getAmount(){
		return this.amount;
	}
	
	public String getFrom(){
		return this.from;
	}
	
	public String getTo(){
		return this.to;
	}
	
	public String getDate(){
		return this.date;
	}
}
