package fr.utt.if26.cs.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;



public abstract class DataBean {
	
	/**
	 * List of fields to export in Database
	 */
	protected String[] export;
	
	public DataBean(){
		
	}
	
	private static String ucfirst(String str){
		return str.substring(0, 1).toUpperCase()+ str.substring(1).toLowerCase();
	}
	
	private String getParam(String param){
		Method method = null;
		try {
			method = this.getClass().getMethod("get"+DataBean.ucfirst(param));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} 
		try {
			return (String) method.invoke(this);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		} catch (ClassCastException e){
			if(e.getMessage().substring(0, 17).equals("java.lang.Integer"))
				try {
					return Integer.toString((int) method.invoke(this));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					e1.printStackTrace();
					return null;
				}
			else
				return null;
		}
	}
	
	public BSONObject getBSONRepresentation(){
		return getBSONRepresentation(null);
	}
	
	public BSONObject getBSONRepresentation(String[] filters){
		BasicBSONObject BSONData = new BasicBSONObject();
		for(String f : this.export){
			if(filters!=null){
				if(inArray(f, filters)){
					BSONData.put(f, this.getParam(f));
				}
			}
			else {
				BSONData.put(f, this.getParam(f));
			}
		}
		return BSONData;
	}
	
	public String getJSONStringRepresentation(){
		return JSON.serialize(this.getBSONRepresentation());
	}
	
	public String getJSONStringRepresentation(String[] filters){
		return JSON.serialize(this.getBSONRepresentation(filters));
	}
	
	public HashMap<String, String> getHashRepresentation(){
		HashMap<String, String> map = new HashMap<String, String>();
		for(String f : this.export){
			map.put(f, this.getParam(f));
		}
		return map;
	}
	
	/***
	 * Teste si un élément est contenu dans un tableau
	 * @param needle : l'élément à rechercher
	 * @param haystack : le tableau
	 * @return true si présent, sinon false
	 */
	private static boolean inArray(String needle, String[] array) {
		for(String s : array)
			if(s.equals(needle))
				return true;
		return false;
	}
}
