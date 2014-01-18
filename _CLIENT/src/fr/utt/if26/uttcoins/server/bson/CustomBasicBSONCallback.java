package fr.utt.if26.uttcoins.server.bson;

import org.bson.BasicBSONObject;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.server.CustomHttpRequestCallback;
import android.os.Bundle;

public interface CustomBasicBSONCallback extends CustomHttpRequestCallback{

	//lorsque la r�ponse d'une requ�te est arriv�e
	public Object call(BasicBSONObject bsonResponse);
	//avant d'envoyer les req�tes
	public void onError(Bundle errorObject);
	//lorsqu'une erreur est survenue dans une requ�te
	public void beforeCall();
}
