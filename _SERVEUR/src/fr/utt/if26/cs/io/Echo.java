package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSONObject;

public abstract class Echo {
	
	protected PrintWriter out;
	
	public Echo(PrintWriter writer){
		this.out = writer;
	}
	
	public abstract void echo(String msg);
	public abstract void echo(BSONObject obj);
}
