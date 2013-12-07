package fr.utt.if26.cs;

import java.util.HashMap;
import java.util.Map;
/***
 * <p> An abstract class which define a minimalist service manager,
 * intend to be sub-classed for more specialization.
 * This class provide basic tools to ensure dependencies management,
 * over inversion control (design pattern Service Locator).</p>
 * 
 *
 */
public class ServiceManager {
	/***
	 * ServiceManager object, available from anywhere in the application.
	 */
	private static ServiceManager instance;
	
	/***
	 * Key/Value storage for services that will be used as dependencies over the application.
	 */
	private HashMap<String, Object> services = new HashMap<String, Object>();
	
	/***
	 * <p>'Load' a ServiceManager object which will be available 
	 * from anywhere in the application.</p>
	 * 
	 * @param serviceManager
	 * 						A ServiceManager object
	 */
	public static void load(ServiceManager serviceManager){
		instance = serviceManager;
	}
	
	/***
	 * Return a service, depending on a (string) key.
	 * 
	 * @param key
	 * 			The name under which the service has been registered.
	 * @return An object which will be used as a dependency.
	 */
	public static Object getService(String key){
		return instance.services.get(key);	
	}
	
	/***
	 * Return an array of services, depending on an array of (string) key.
	 * 
	 * @param keys
	 * 			Array of string, under which some services has been registered.
	 * @return An array of object which will be used as a dependency.
	 */
	public static Object[] getServices(String... keys){
		Object[] servicesToReturn = new Object[keys.length];
		for(int i =0; i < keys.length; i++){
			servicesToReturn[i] = instance.services.get(keys[i]);
		}
		return servicesToReturn;
	}
	
	/***
	 * <p>Register an Object under a (string) key. 
	 * <b>This object is meant to be used later in the application as a dependency.</b>
	 * </p>
	 * 
	 * @param key
	 * 			The name under which the service has been registered.
	 * @param service
	 * 			The service to register
	 */
	public void registerService(String key, Object service){
		services.put(key, service);
	}
	
	/***
	 * Register services to be used later in the application as dependencies.</b>
	 * @param servicesMap
	 * 					A map of Object mapped with (String) keys.
	 */
	public void registerServices(Map<String, Object> servicesMap){
		instance.services.putAll(servicesMap);
	}
	
}
