package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSONObject;

public abstract class Echo {
	
	protected PrintWriter out;
	
	public final static int MSG = 0;
	public final static int ERR = 1;
	public final static int INFO = 2;
	
	public Echo(PrintWriter writer){
		this.out = writer;
	}
	
	public abstract void echo(String msg);
	public abstract void echo(int type, String msg);
	public abstract void echo(BSONObject obj);
}
