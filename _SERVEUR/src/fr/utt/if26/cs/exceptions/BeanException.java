package fr.utt.if26.cs.exceptions;

public class BeanException extends Exception {
	
	public BeanException(String message){
		super("{'error':'"+message+"'}");
	}
}
