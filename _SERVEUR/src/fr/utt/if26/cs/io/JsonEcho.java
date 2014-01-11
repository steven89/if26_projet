package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSONObject;

public class JsonEcho extends Echo {

	public JsonEcho(PrintWriter writer) {
		super(writer);
	}

	@Override
	public void echo(String msg) {
		this.out.println(msg);
	}
	
	@Override
	public void echo(int type, String msg) {
		String msgType = "";
		switch(type){
		case Echo.ERR:
			msgType = "error";
			break;
		case Echo.INFO:
			msgType = "info";
			break;
		default:
			msgType = "message";
			break;
		}
		this.out.println("{'"+msgType+"' : '"+msg+"'}");
	}

	@Override
	public void echo(BSONObject obj) {
		this.out.println(obj);
	}
}
