package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSONObject;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

public class JsonEcho extends Echo {

	public JsonEcho(PrintWriter writer) {
		super(writer);
	}

	@Override
	public void echo(String msg) {
		try{
			JSON.parse(msg);
			this.out.println(msg);
		} catch (JSONParseException e){
			this.echo(Echo.MSG, msg);
		}
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
