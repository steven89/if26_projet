package fr.utt.if26.uttcoins.utils;

import org.json.JSONObject;

import android.os.Bundle;

public interface JsonCallback {
	public Object call(JSONObject bsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
