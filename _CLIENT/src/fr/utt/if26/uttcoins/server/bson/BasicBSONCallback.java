package fr.utt.if26.uttcoins.server.bson;

import org.bson.BasicBSONObject;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.server.CustomHttpRequestCallback;
import android.os.Bundle;

public interface BasicBSONCallback extends CustomHttpRequestCallback{

	public Object call(BasicBSONObject bsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
