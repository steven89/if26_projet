package fr.utt.if26.uttcoins.error;

//classe m�re des erreur custom de l'appli (souvent en lien avec une erreur serveur)
public class CustomServerException extends Exception {
	
	//le titre de l'erreur, � afficher � l'utilisateur
	public static String getErrorTitle(){
		return "Erreur serveur";
	}
	
	//le message de l'erreur, � afficher � l'utilisateur
	public static String getErrorMessage(){
		return "Le serveur � rencontr� une erreur";
	}
	
}
