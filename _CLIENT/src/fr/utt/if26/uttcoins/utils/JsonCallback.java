package fr.utt.if26.uttcoins.utils;

import org.json.JSONObject;

public interface JsonCallback {
	public JSONObject call(JSONObject jsonResponse);
}
