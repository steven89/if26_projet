package fr.utt.if26.cs.utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

public class ServletUtils {
	
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
	
	public static BasicBSONObject extractRequestData(int type, HttpServletRequest request){
		switch (type) {
		case ServletUtils.GET:
			return extractGetRequestData(request);
		case ServletUtils.PUT:
		case ServletUtils.POST:
			return extractPostRequestData(request);
		default:
			return null;
		}
	}
	
	
	private static BasicBSONObject extractGetRequestData(HttpServletRequest request){
		Map<String, String[]> params = request.getParameterMap();
		BasicBSONObject obj = new BasicBSONObject();
		for(String key : params.keySet()){
			if(params.get(key).length<2)
				obj.put(key, params.get(key)[0]);
			else
				obj.put(key, params.get(key));
		}
		return obj;
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
