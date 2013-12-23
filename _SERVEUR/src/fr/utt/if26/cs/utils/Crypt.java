package fr.utt.if26.cs.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class Crypt {
	
	private static String dico = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static int saltSize = 10;
	
	
	public static String crypt(String passwd){
		return Crypt.crypt(passwd, null);
	}
	
	public static String crypt(String passwd, String salt){
		String s = (salt!=null)?salt:Crypt.generateSalt();
		String hash = Crypt.sha1(s);
		String pass = passwd+hash;
		pass = Crypt.sha1(pass);
		return pass+"@"+s;
	}
	
	public static Boolean match(String pass, String crypt){
		String salt = Crypt.extractSalt(crypt);
		String passwd = Crypt.crypt(pass, salt);
		return (passwd==crypt);
	}
	
	private static String sha1(String password){
	    String sha1 = "";
	    try{
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(password.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e){
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e){
	        e.printStackTrace();
	    }
	    return sha1;
	}

	private static String byteToHex(final byte[] hash){
	    Formatter formatter = new Formatter();
	    for (byte b : hash){
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	private static String generateSalt(){
		String salt="";
		int max = Crypt.dico.length()-1;
		for(int i=0; i<Crypt.saltSize; i++){
			int random = (int) (Math.random()*max);
			salt += Crypt.dico.charAt(random);
		}
		return salt;
	}
	
	private static String extractSalt(String passwd){
		return passwd.substring(passwd.lastIndexOf("@")+1);
	}
}
