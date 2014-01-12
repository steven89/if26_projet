package fr.utt.if26.uttcoins.server.bson;

import org.bson.BSONObject;
import org.json.JSONObject;

import android.os.Bundle;

public interface BsonCallback {
	public Object call(BSONObject bsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
