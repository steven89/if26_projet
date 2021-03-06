package fr.utt.if26.cs.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bson.types.ObjectId;

import fr.utt.if26.cs.exceptions.BeanException;

/**
 * bean used for a transaction
 * @author steven
 */
public class Transaction extends DataBean {
	private static final long serialVersionUID = 1L;
	protected int amount;
	protected String from; // User.SYS_USER or tag
	protected String to; // user tag
	protected String date;
	protected String id;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyy");
	
	
	public Transaction(int somme, String crediteurPseudo, String destinatairePseudo) throws BeanException{
		this.setAmount(somme);
		this.setFrom(crediteurPseudo);
		this.setTo(destinatairePseudo);
		this.setDate();
		this.export = new String[] {"amount", "from", "to", "date"};
	}
	
	public Transaction(String dbID, int somme, String crediteurPseudo, String destinatairePseudo, String date) throws BeanException{
		this.setAmount(somme);
		this.setFrom(crediteurPseudo);
		this.setTo(destinatairePseudo);
		this.setId(dbID);
		this.setDate(date);
		this.export = new String[] {"id", "amount", "from", "to","date"};
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
	
	public void setId(String id) throws BeanException{
		if(ObjectId.isValid(id))
			this.id = id;
		else
			throw new BeanException("invalid id");
	}
	
	public void setAmount(int coins) throws BeanException{
		if(coins>0){
			this.amount = coins;
		}
		else
			throw new BeanException("invalid amount");
	}
	
	public void setFrom(String from) throws BeanException{
		if(from.matches("^[a-zA-Z0-9_-]{3,}$") || from.equals(User.SYS_USER))
			this.from = from;
		else
			throw new BeanException("invalid from");
	}
	
	public void setTo(String to) throws BeanException{
		if(to.matches("^[a-zA-Z0-9_-]{3,}$"))
			this.to = to;
		else
			throw new BeanException("invalid to");
	}
	
	public void setDate() throws BeanException{
		this.setDate(Transaction.dateFormat.format(Calendar.getInstance().getTime()));
	}
	
	public void setDate(String date) throws BeanException{
		if(date.matches("^[0-2][0-9]:[0-5][0-9] - [0-3][0-9]/[0-1][0-9]/[0-9]{4}$"))
			this.date = date;
		else
			throw new BeanException("invalid date format");
	}
}
