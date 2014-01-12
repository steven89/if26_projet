package fr.utt.if26.uttcoins.server.json;

import org.json.JSONObject;

import fr.utt.if26.uttcoins.server.CustomHttpRequestCallback;
import android.os.Bundle;

public interface CustomJSONCallback extends CustomHttpRequestCallback{
	public Object call(JSONObject jsonResponse);
	public void onError(Bundle errorObject);
	public void beforeCall();
}
