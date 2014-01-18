package fr.utt.if26.uttcoins.server.bson;

import org.bson.BasicBSONObject;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.server.CustomHttpRequestCallback;
import android.os.Bundle;

public interface CustomBasicBSONCallback extends CustomHttpRequestCallback{

	//lorsque la réponse d'une requête est arrivée
	public Object call(BasicBSONObject bsonResponse);
	//avant d'envoyer les reqêtes
	public void onError(Bundle errorObject);
	//lorsqu'une erreur est survenue dans une requête
	public void beforeCall();
}
