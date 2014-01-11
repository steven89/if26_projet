package fr.utt.if26.cs.io;

import java.io.PrintWriter;

import org.bson.BSON;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

public class BsonEcho extends Echo {
	
	private final static String dico = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public BsonEcho(PrintWriter writer) {
		super(writer);
	}

	@Override
	public void echo(String msg) {
		try{
			BSONObject obj = (BSONObject) JSON.parse(msg);
			this.echo(obj);
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
		String str = "{'"+msgType+"' : '"+msg+"'}";
		try{
			BSONObject obj = (BSONObject) JSON.parse(str);
			this.echo(obj);
		} catch (JSONParseException e){
			
		}
	}

	@Override
	public void echo(BSONObject obj) {
		byte[] bte = BSON.encode(obj);	
		String str = "";
		for(byte b : bte){
			int random = (int) (Math.random()*BsonEcho.dico.length());
			str+=b;
			str+=BsonEcho.dico.charAt(random);
		}
		out.println(str);
		
		
		//decoding
		/*String[] tab = str.split("[a-zA-Z]");
		byte[] array = new byte[tab.length];
		for(int i =0; i<tab.length; i++){
			array[i] = Byte.valueOf(tab[i]);
		}
		BSONDecoder decoder = new BasicBSONDecoder();
		BasicBSONObject obje = (BasicBSONObject) decoder.readObject(array);
		out.println(obje.toString());*/
	}
}
