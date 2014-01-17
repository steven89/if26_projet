package fr.utt.if26.cs.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;


/**
 * Generic bean
 * @author steven
 */
public abstract class DataBean {
	
	/**
	 * pramas to list when using a bean representation
	 * @see DataBean#toString()
	 * @see DataBean#toBSON()
	 * @see DataBean#toJSONString()
	 */
	protected String[] export;
	
	public DataBean(){
		
	}
	
	/**
	 * Utilis : Upper case the first char of a String
	 * @param str : string to use
	 * @return string with first char upper
	 */
	private static String ucfirst(String str){
		return str.substring(0, 1).toUpperCase()+ str.substring(1).toLowerCase();
	}
	
	/**
	 * get a bean parameter by name (using a 'get' function)
	 * @param param : param to get
	 * @return param value
	 */
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
			else if(e.getMessage().substring(0, 14).equals("java.lang.Long"))
				try {
					return Long.toString((long) method.invoke(this));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					e1.printStackTrace();
					return null;
				}
			else
				return null;
		}
	}
	
	/**
	 * return a BSON representation of the bean
	 * @return BSON object
	 */
	public BSONObject toBSON(){
		return toBSON(null);
	}
	
	/**
	 * return a BSON representation of the bean using filters
	 * @param filters : whitelist of fields to get into the representation
	 * @return BSON object
	 */
	public BSONObject toBSON(String[] filters){
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
	
	/**
	 * return a JSON representation of the bean
	 * @return JSON formated string
	 */
	public String toJSONString(){
		return JSON.serialize(this.toBSON());
	}
	
	/**
	 * return a JSON representation of the bean using filters
	 * @param filters : whitelist of fields to get into the representation
	 * @return JSON formated string
	 */
	public String toJSONString(String[] filters){
		return JSON.serialize(this.toBSON(filters));
	}
	
	/**
	 * get a string version of the bean
	 * @see DataBean#toJSONString()
	 */
	public String toString(){
		return this.toJSONString();
	}
	
	/***
	 * test whether or not a value is in an array
	 * @param needle : value to look for
	 * @param array : array to look in
	 * @return true if needle found in array
	 */
	private static boolean inArray(String needle, String[] array) {
		for(String s : array)
			if(s.equals(needle))
				return true;
		return false;
	}
}
