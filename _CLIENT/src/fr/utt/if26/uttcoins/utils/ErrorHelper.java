package fr.utt.if26.uttcoins.utils;

import java.util.HashMap;

import org.apache.http.NoHttpResponseException;

import fr.utt.if26.uttcoins.error.CustomServerException;
import android.content.res.Resources;
import android.os.Bundle;

public class ErrorHelper {
	
	public static final String ERROR_TITLE_KEY = "error_title";
	public static final String ERROR_MSG_KEY = "error_msg";
	public static final HashMap<Class<? extends Exception>, String> ERROR_TITLE_MAP = new HashMap<Class<? extends Exception>, String>();
	public static final HashMap<Class<? extends Exception>, String> ERROR_MSG_MAP = new HashMap<Class<? extends Exception>, String>();

	static{
		initErrorMaps();
	}
	
	public static Bundle getErrorObject(Exception exeptionCls){
		Bundle errorObject = new Bundle();
		errorObject.putString(ERROR_TITLE_KEY, ERROR_TITLE_MAP.get(exeptionCls.getClass()));
		errorObject.putString(ERROR_MSG_KEY, ERROR_MSG_MAP.get(exeptionCls.getClass()));
		return errorObject;
	}
	
	private static void initErrorMaps(){
		//todo : chercher les string dans les strings androids
		String networkUnknowErrorTitle = "Erreur réseaux";
		String networkUnknowErrorMsg = "Vérifiez votre connexion internet. "
				+ "\n Si le problème persiste, le problème vient peut être du serveur. ";
		putError(NoHttpResponseException.class, networkUnknowErrorTitle, networkUnknowErrorMsg);
		putError(CustomServerException.class, CustomServerException.getErrorTitle(), CustomServerException.getErrorMsg());
	}
	
	public static <T extends Exception> void putError(Class<T> exeptionCls, String title, String msg){
		ERROR_TITLE_MAP.put(exeptionCls, title);
		ERROR_MSG_MAP.put(exeptionCls, msg);
	}

	public static CustomServerException getCustomServerExecption(String error_tag) {
		//lazy implementation
		CustomServerException serveur_execption = new CustomServerException();
		//true implementation exemple
//		if(error_tag == "invalid tag"){
//			serveur_execption = new UnknowTagExecption();
//		}
//		if(error_tag ....)
		return serveur_execption;
	}
}
