package fr.utt.if26.uttcoins.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bson.BSON;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONObject;
import org.json.JSONException;

import android.util.Log;
import fr.utt.if26.uttcoins.error.CustomServerException;
import fr.utt.if26.uttcoins.utils.ErrorHelper;

public class BsonHandler {
	public static char[] DEFAULT_ENCODING_CHAR_TABLE = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 
		'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 
		'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	public static BasicBSONObject readBSONResponse(InputStream stream) 
			throws CustomServerException, JSONException, IOException{
		BasicBSONObject bsonResponse;
		String lineRead = "";
		StringBuilder stringResponse = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			while((lineRead = reader.readLine())!=null){
				stringResponse.append(lineRead);
			}
		} catch (IOException e) {
			throw e;
		}
		bsonResponse = decodeBSONResponse(stringResponse.toString());
		if(bsonResponse.containsField("error")){
			throw ErrorHelper.getCustomServerException(bsonResponse.getString("error"));
		}
		return bsonResponse;
	}
	
	public static String encodeBSONObjectToString(BSONObject bsonObject){
		byte[] byteResponse = BSON.encode(bsonObject);
		String StringResponse = "";
		for(int i = 0; i < byteResponse.length; i++){
			StringResponse += Byte.toString(byteResponse[i]) + getRandomChar(DEFAULT_ENCODING_CHAR_TABLE);
		}
		return StringResponse;
	}
	
	protected static char getRandomChar(char[] charTable){
		int i = (int) Math.round(Math.random() * (charTable.length-1)); 
		return charTable[i];
	}

	protected static BasicBSONObject decodeBSONResponse(String encodedResponse){
		BasicBSONObject bsonResponse;
		Log.i("DECODE BSON", "encodedresponse = "+encodedResponse);
		String[] chunks = encodedResponse.split("[a-zA-Z]");
		byte[] decodedByteResponse = new byte[chunks.length];
		for(int i = 0; i < chunks.length; i++){
			Log.i("DECODE BSON", "chunks["+Integer.toString(i)+"] = "+chunks[i]);
			decodedByteResponse[i] = Byte.valueOf(chunks[i]);
		}
		BSONDecoder decoder = new BasicBSONDecoder();
		bsonResponse = (BasicBSONObject) decoder.readObject(decodedByteResponse);
		return bsonResponse;
	}
}


