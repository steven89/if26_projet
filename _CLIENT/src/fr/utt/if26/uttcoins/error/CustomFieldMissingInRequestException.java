package fr.utt.if26.uttcoins.error;

import android.text.style.SuperscriptSpan;

public class CustomFieldMissingInRequestException extends CustomServerException {

	public static String getErrorTitle(){
		return "L'op�ration � �chou�";
	}
	
	public static String getErrorMessage(){
		return "Un champ est manquant";
	}
	
}
