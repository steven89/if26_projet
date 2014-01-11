package fr.utt.if26.uttcoins.error;

public class CustomAuthException extends CustomServerException {

	public static String getErrorTitle(){
		return "L'authentification à échoué";
	}
	
	public static String getErrorMessage(){
		return "Mot de passe ou identifiant invalide";
	}
	
}
