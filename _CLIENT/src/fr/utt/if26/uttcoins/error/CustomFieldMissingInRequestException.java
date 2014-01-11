package fr.utt.if26.uttcoins.error;

public class CustomFieldMissingInRequestException extends CustomServerException {

	public static String getErrorTitle(){
		return "L'opération à échoué";
	}
	
	public static String getErrorMessage(){
		return "Un champ est manquant";
	}
	
}
