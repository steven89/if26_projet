package fr.utt.if26.uttcoins.error;

//classe mère des erreur custom de l'appli (souvent en lien avec une erreur serveur)
public class CustomServerException extends Exception {
	
	//le titre de l'erreur, à afficher à l'utilisateur
	public static String getErrorTitle(){
		return "Erreur serveur";
	}
	
	//le message de l'erreur, à afficher à l'utilisateur
	public static String getErrorMessage(){
		return "Le serveur à rencontré une erreur";
	}
	
}
