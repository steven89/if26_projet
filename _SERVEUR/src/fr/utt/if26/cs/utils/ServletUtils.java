package fr.utt.if26.cs.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

public class ServletUtils {
	
	public static final boolean useBSON = true;
	
	public final static int GET = 0;
	public final static int POST = 1;
	public final static int PUT = 2;
	public final static int DELETE = 3;
	public final static int HEAD = 4;
	
	public static boolean checkRequiredFields(String[] args, BSONObject datas){
		for(String field : args){
			if(!datas.containsField(field) || datas.get(field)==null)
				return false;
		}
		return true;
	}
	
	public static BasicBSONObject extractRequestData(HttpServletRequest request){
		if(request.getMethod().equals("PUT") || request.getMethod().equals("POST") || request.getMethod().equals("DELETE"))
			return extractPostRequestData(request);
		else if(request.getMethod().equals("GET"))
			return extractGetRequestData(request);
		else
			return new BasicBSONObject();
	}
	
	
	private static BasicBSONObject extractGetRequestData(HttpServletRequest request){
		try{
			String query;
			if(useBSON)
				query = ServletUtils.decryptBSON(request.getQueryString());
			else
				query = request.getQueryString();
			if(query!=null){
				query = query.replaceAll("(%22|%27)", "\"");
				query = query.replaceAll("%20", " ");
				BasicBSONObject params = (BasicBSONObject) JSON.parse(query);
				return params;
			}
			else
				return new BasicBSONObject();
		} catch (JSONParseException e){
			return new BasicBSONObject();
		}
	}
	
	private static BasicBSONObject extractPostRequestData(HttpServletRequest request){
		String params = "";
		String line = "";
		try {
			while((line = request.getReader().readLine()) != null){
				params += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
			if(useBSON)
				params = ServletUtils.decryptBSON(params);
			BasicBSONObject jsonParams = (BasicBSONObject) JSON.parse(params);
			return jsonParams;
		} catch (JSONParseException e){
			return new BasicBSONObject();
		}
	}
	
	private static String decryptBSON(String datas){
		String[] tab = datas.split("[a-zA-Z]");
        byte[] array = new byte[tab.length];
        for(int i =0; i<tab.length; i++){
            array[i] = Byte.valueOf(tab[i]);
        }
        BSONDecoder decoder = new BasicBSONDecoder();
        BasicBSONObject obj = (BasicBSONObject) decoder.readObject(array);
        System.out.println(obj);
        return obj.toString();
	}
}
