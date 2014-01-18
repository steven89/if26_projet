package fr.utt.if26.uttcoins.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.http.NoHttpResponseException;

import fr.utt.if26.uttcoins.error.CustomAuthException;
import fr.utt.if26.uttcoins.error.CustomFieldMissingInRequestException;
import fr.utt.if26.uttcoins.error.CustomInvalidCreditorException;
import fr.utt.if26.uttcoins.error.CustomServerException;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

//une classe permettant de gérer les erreur personnalisé dans l'application
public class ErrorHelper {
	
	public static final String ERROR_TITLE_KEY = "error_title";
	public static final String ERROR_MSG_KEY = "error_msg";
	//les titres d'erreur, mappés par type d'erreur
	public static HashMap<Class<? extends Exception>, String> ERROR_TITLE_MAP = new HashMap<Class<? extends Exception>, String>();
	//les messages d'erreur, mappés par type d'erreur
	public static HashMap<Class<? extends Exception>, String> ERROR_MSG_MAP = new HashMap<Class<? extends Exception>, String>();
	//les erreur de l'application, mappées par leur identifiant serveur
	public static HashMap<String, Class<? extends CustomServerException>> CUSTOM_ERROR_MAP = new HashMap<String, Class<? extends CustomServerException>>(); 

	private static String DEFAULT_TITLE = "Erreur";
	private static String DEFAULT_MSG = "Oups, une erreur inattendue est survenue";
	
	static{
		initTitleAndMsgMaps();
		initCustomErrorMap();
	}
	
	//permet d'obtenir dynamiquement le titre et le message à afficher à l'utilisateur,
	//pour toute erreur survenant dans l'application
	public static Bundle getErrorObject(Exception exeptionCls){
		Bundle errorObject = new Bundle();
		String error_title = ERROR_TITLE_MAP.get(exeptionCls.getClass());
		error_title = (error_title != null && error_title.length() > 0) ? error_title : DEFAULT_TITLE;
		String error_msg  = ERROR_MSG_MAP.get(exeptionCls.getClass());
		error_msg = (error_msg != null && error_msg.length() > 0) ? error_msg : DEFAULT_MSG;

		errorObject.putString(ERROR_TITLE_KEY, error_title);
		errorObject.putString(ERROR_MSG_KEY, error_msg);
		return errorObject;
	}
	
	private static void initTitleAndMsgMaps(){
		//todo : chercher les string dans les strings androids
		String networkUnknowErrorTitle = "Erreur réseaux";
		String networkUnknowErrorMsg = "Vérifiez votre connexion internet. "
				+ "\n Si le problème persiste, le problème vient peut être du serveur. ";
		//A faire dans une boucle, avec de la reflexivite
		putError(NoHttpResponseException.class, networkUnknowErrorTitle, networkUnknowErrorMsg);
		putError(CustomServerException.class, CustomServerException.getErrorTitle(), CustomServerException.getErrorMessage());
		putError(CustomAuthException.class, CustomAuthException.getErrorTitle(), CustomAuthException.getErrorMessage());
		putError(CustomFieldMissingInRequestException.class, CustomFieldMissingInRequestException.getErrorTitle(), CustomFieldMissingInRequestException.getErrorMessage());
		putError(CustomInvalidCreditorException.class, CustomInvalidCreditorException.getErrorTitle(), CustomInvalidCreditorException.getErrorMessage());
	}
	
	private static void initCustomErrorMap(){
		CUSTOM_ERROR_MAP.put("auth_error", CustomAuthException.class);
		CUSTOM_ERROR_MAP.put("auth_unknow", CustomAuthException.class);
		CUSTOM_ERROR_MAP.put("field_missing", CustomFieldMissingInRequestException.class);
		CUSTOM_ERROR_MAP.put("invalid creditor", CustomInvalidCreditorException.class);
	}

	
	public static <T extends Exception> void putError(Class<T> exceptionCls, String title, String msg){
		ERROR_TITLE_MAP.put(exceptionCls, title);
		ERROR_MSG_MAP.put(exceptionCls, msg);
	}

	//permet de lever l'exception adéquat celon le message d'erreur renvoyé depuis le serveur
	//voir : BsonHandler.readResponse(InputStream is);
	public static CustomServerException getCustomServerException(String error_tag) {
		//lazy implementation
		CustomServerException serveur_exception = null;
		try {
			//Log.i("ERROR", error_tag);
			serveur_exception = CUSTOM_ERROR_MAP.get(error_tag).getConstructor().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (serveur_exception != null) ? serveur_exception : new CustomServerException();
	}
}
