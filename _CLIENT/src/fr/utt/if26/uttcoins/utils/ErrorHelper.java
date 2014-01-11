package fr.utt.if26.uttcoins.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.http.NoHttpResponseException;

import fr.utt.if26.uttcoins.error.CustomAuthException;
import fr.utt.if26.uttcoins.error.CustomFieldMissingInRequestException;
import fr.utt.if26.uttcoins.error.CustomServerException;
import android.content.res.Resources;
import android.os.Bundle;

public class ErrorHelper {
	
	public static final String ERROR_TITLE_KEY = "error_title";
	public static final String ERROR_MSG_KEY = "error_msg";
	public static HashMap<Class<? extends Exception>, String> ERROR_TITLE_MAP = new HashMap<Class<? extends Exception>, String>();
	public static HashMap<Class<? extends Exception>, String> ERROR_MSG_MAP = new HashMap<Class<? extends Exception>, String>();
	public static HashMap<String, Class<? extends CustomServerException>> CUSTOM_ERROR_MAP = new HashMap<String, Class<? extends CustomServerException>>(); 

	static{
		initTitleAndMsgMaps();
		initCustomErrorMap();
	}
	
	public static Bundle getErrorObject(Exception exeptionCls){
		Bundle errorObject = new Bundle();
		errorObject.putString(ERROR_TITLE_KEY, ERROR_TITLE_MAP.get(exeptionCls.getClass()));
		errorObject.putString(ERROR_MSG_KEY, ERROR_MSG_MAP.get(exeptionCls.getClass()));
		return errorObject;
	}
	
	private static void initTitleAndMsgMaps(){
		//todo : chercher les string dans les strings androids
		String networkUnknowErrorTitle = "Erreur réseaux";
		String networkUnknowErrorMsg = "Vérifiez votre connexion internet. "
				+ "\n Si le problème persiste, le problème vient peut être du serveur. ";
		putError(NoHttpResponseException.class, networkUnknowErrorTitle, networkUnknowErrorMsg);
		putError(CustomServerException.class, CustomServerException.getErrorTitle(), CustomServerException.getErrorMessage());
	}
	
	private static void initCustomErrorMap(){
		CUSTOM_ERROR_MAP.put("auth_error", CustomAuthException.class);
		CUSTOM_ERROR_MAP.put("auth_unknow", CustomAuthException.class);
		CUSTOM_ERROR_MAP.put("field_missing", CustomFieldMissingInRequestException.class);
	}
	
	public static <T extends Exception> void putError(Class<T> exceptionCls, String title, String msg){
		ERROR_TITLE_MAP.put(exceptionCls, title);
		ERROR_MSG_MAP.put(exceptionCls, msg);
	}

	public static CustomServerException getCustomServerException(String error_tag) {
		//lazy implementation
		CustomServerException serveur_exception = null;
		try {
			serveur_exception = CUSTOM_ERROR_MAP.get(error_tag).getConstructor().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (serveur_exception != null) ? serveur_exception : new CustomServerException();
	}
}
