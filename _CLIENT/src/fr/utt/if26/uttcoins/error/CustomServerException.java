package fr.utt.if26.uttcoins.error;

public class CustomServerException extends Exception {
	
	public static String getErrorTitle(){
		return "Erreur serveur";
	}
	
	public static String getErrorMessage(){
		return "Le serveur à rencontré une erreur";
	}
	
}
