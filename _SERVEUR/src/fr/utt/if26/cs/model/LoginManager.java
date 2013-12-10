package fr.utt.if26.cs.model;

public class LoginManager {
	
	private static String dico = "abcdefghijklmnopqrstwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@$";
	
	public LoginManager(){
		
	}
	
	public static String generateToken(){
		String token="";
		int max = dico.length()-1;
		for(int i=0; i<30; i++){
			int random = (int) Math.random()*max;
			token += dico.charAt(random);
		}
		return token;
	}
}
