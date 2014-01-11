package fr.utt.if26.uttcoins.error;

public class CustomServerException extends Exception {
	//lazy implementation, devait �tre sub class� pour chaque erreur serveur
	
	public static String getErrorTitle(){
		return "Erreur serveur";
	}
	
	public static String getErrorMsg(){
		return "Le serveur � rencontr� une erreur";
	}
	
}
