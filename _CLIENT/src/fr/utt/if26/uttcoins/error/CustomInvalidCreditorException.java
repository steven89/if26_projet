package fr.utt.if26.uttcoins.error;

public class CustomInvalidCreditorException extends CustomServerException {
	public static String getErrorTitle(){
		return "L'op�ration � �chou�";
	}
	
	public static String getErrorMessage(){
		return " Compte crediteur inconnu";
	}
}
