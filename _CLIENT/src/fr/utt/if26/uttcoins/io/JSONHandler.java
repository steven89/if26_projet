package fr.utt.if26.uttcoins.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.utt.if26.uttcoins.error.CustomServerException;
import fr.utt.if26.uttcoins.utils.ErrorHelper;

@Deprecated
public class JSONHandler {
	
	@Deprecated
	protected static JSONObject JSONParse(String jsonString){
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}
	
	@Deprecated
	public static JSONObject readResponse(InputStream stream) 
			throws CustomServerException, JSONException, IOException{
		String lineRead = "";
		StringBuilder stringResponse = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		JSONObject JsonResponse;
		try {
			while((lineRead = reader.readLine())!=null){
				stringResponse.append(lineRead);
			}
		} catch (IOException e) {
			throw e;
		}
		Log.i("STRING RESPONSE", stringResponse.toString());
		JsonResponse = JSONParse(stringResponse.toString());
		if(JsonResponse.has("error")){
			throw ErrorHelper.getCustomServerException(JsonResponse.getString("error"));
		}
		return JsonResponse;
	}
}
