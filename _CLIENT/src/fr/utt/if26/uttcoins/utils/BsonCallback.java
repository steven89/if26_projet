package fr.utt.if26.uttcoins.utils;

import org.bson.BSONObject;
import org.json.JSONObject;

import android.os.Bundle;

public interface BsonCallback {
	public BSONObject call(BSONObject jsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
