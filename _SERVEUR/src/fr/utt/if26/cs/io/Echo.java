package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSONObject;

/**
 * Print a message on http response (response.writer)
 * @author steven
 */
public abstract class Echo {
	
	protected PrintWriter out;
	
	public final static int MSG = 0;
	public final static int ERR = 1;
	public final static int INFO = 2;
	
	public Echo(PrintWriter writer){
		this.out = writer;
	}
	
	/**
	 * Write a message
	 * @param msg : message to write
	 * if message is not a JSON String {@link Echo#echo(int, String)} will be call with default {@value Echo#MSG} value
	 * @see Echo#echo(int, String)
	 */
	public abstract void echo(String msg);
	
	/**
	 * Write a short message using a tag (type)
	 * @param type : type of the message {@value Echo#MSG}, {@value Echo#ERR}, {@value Echo#INFO}
	 * @param msg : message to write
	 */
	public abstract void echo(int type, String msg);
	
	/**
	 * Write a BSON object
	 * @param obj : datas to write
	 */
	public abstract void echo(BSONObject obj);
}
