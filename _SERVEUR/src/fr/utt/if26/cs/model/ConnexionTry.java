package fr.utt.if26.cs.model;

import java.util.Calendar;
import java.util.Date;

import fr.utt.if26.cs.exceptions.BeanException;

public class ConnexionTry extends DataBean {
	
	
	private String email;
	private long date;
	
	public ConnexionTry(String email) throws BeanException{
		this.setEmail(email);
		this.setDate(Calendar.getInstance().getTime().getTime());
		this.export = new String[] {"email","date"};
	}
	
	public ConnexionTry(String email, long date) throws BeanException{
		this.setEmail(email);
		this.setDate(date);
		this.export = new String[] {"email","date"};
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public long getDate(){
		return this.date;
	}
	
	public void setEmail(String email) throws BeanException{
		if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"))
			this.email = email;
		else
			throw new BeanException("invalid email");
	}
	
	public void setDate(long date) throws BeanException{
		//System.out.println(new Date(date+(60*60000)));
		if(date>0)
			this.date = date;
		else
			throw new BeanException("invalid date");
	}
}
