package fr.utt.if26.uttcoins.server.bson;

import org.bson.BasicBSONObject;
import org.json.JSONObject;

import android.os.Bundle;

public interface BasicBSONCallback {
	public Object call(BasicBSONObject bsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
