package fr.utt.if26.cs.utils;

import java.io.Console;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

public class ServletUtils {
	
	public static final boolean useBSON = false;
	
	public final static int GET = 0;
	public final static int POST = 1;
	public final static int PUT = 2;
	public final static int DELETE = 3;
	public final static int HEAD = 4;
	
	public static boolean checkRequiredFields(String[] args, BSONObject datas){
		for(String field : args){
			if(!datas.containsField(field))
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
			String query = request.getQueryString();
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
			BasicBSONObject jsonParams = (BasicBSONObject) JSON.parse(params);
			return jsonParams;
		} catch (JSONParseException e){
			return new BasicBSONObject();
		}
		
	}
}
