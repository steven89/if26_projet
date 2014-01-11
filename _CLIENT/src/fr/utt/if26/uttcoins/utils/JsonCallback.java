package fr.utt.if26.uttcoins.utils;

import org.json.JSONObject;

import android.os.Bundle;

public interface JsonCallback {
	public JSONObject call(JSONObject jsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
