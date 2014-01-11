package fr.utt.if26.cs.exceptions;

public class BeanException extends Exception {
	private static final long serialVersionUID = 328556003946144467L;

	public BeanException(String message){
		super("{'error':'"+message+"'}");
	}
}
