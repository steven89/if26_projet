package fr.utt.if26.cs.model;

import org.bson.BSONObject;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.utils.Crypt;

public class LoginManager {
	
	private final static String dico = "abcdefghijklmnopqrstwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final static int tokenSize = 30;
	private final static String requiredLogin[] = {"email", "pass"};
	private final static String requiredLogout[] = {"email", "token"};
	private final static String requiredAuth[] = {"email", "token"};
	private final static int base = DatabaseManager.USERS;
	
	private static final int LOGIN = 0;
	private static final int LOGOUT = 1;
	private static final int AUTH = 2;
	
	public static String generateToken(){
		String token="";
		int max = dico.length()-1;
		for(int i=0; i<LoginManager.tokenSize; i++){
			int random = (int) (Math.random()*max);
			token += dico.charAt(random);
		}
		return token;
	}
	
	private static boolean hasRequiredFields(BSONObject datas, int type){
		String[] requiredFields;
		switch (type) {
		case LoginManager.LOGIN:
			requiredFields = LoginManager.requiredLogin;
			break;
		case LoginManager.LOGOUT:
			requiredFields = LoginManager.requiredLogout;
			break;
		case LoginManager.AUTH:
			requiredFields = LoginManager.requiredAuth;
			break;
		default:
			requiredFields = LoginManager.requiredLogin;
			break;
		}
		for(String field : requiredFields){
			if(!datas.containsField(field))
				return false;
		}
		return true;
	}
	
	public static String logIn(BSONObject params) throws BeanException{
		if(LoginManager.hasRequiredFields(params, LOGIN)){
			User user = new User((String) params.get("email"), (String) params.get("pass"));
			DataBean bean=null;
			Database db = DatabaseManager.getInstance().getBase(LoginManager.base);
			db.open();
			bean = db.getBean("email", user.getEmail());
			db.close();
			if(bean!=null)
				if(Crypt.match(user.getPass(), ((User) bean).getPass())){
					((User) bean).setToken(LoginManager.generateToken());
					db.open();
					db.updateBean(bean);
					db.close();
					//return "{'email': '"+((User) bean).getEmail()+"', 'token' : '"+((User) bean).getToken()+"'}";
					return bean.getJSONStringRepresentation(new String[] {"email", "token", "tag"});
				}
				else
					return "{'error':'auth_error'}";
			else
				return "{'error':'auth_unknow'}";
		}
		else
			return "{'error':'field_missing'}";
	}
	
	public static String logOut(BSONObject params) throws BeanException{
		if(LoginManager.hasRequiredFields(params, LOGOUT)){
			DataBean bean = null;
			Database db = DatabaseManager.getInstance().getBase(LoginManager.base);
			db.open();
			bean = db.getBean("email", (String) params.get("email"));
			db.close();
			if(bean!=null){
				if(((User) bean).getToken().equals((String) params.get("token"))){
					((User) bean).setToken(LoginManager.generateToken());
					db.open();
					db.updateBean(bean);
					db.close();
					return "{'error':'auth_ok'}";
				}
				else
					return "{'error':'auth_error'}";
			}
			else
				return "{'error':'auth_unknow'}";
		}
		else
			return "{'error':'field_missing'}";
	}
	
	public static boolean checkAuth(BSONObject params){
		if(LoginManager.hasRequiredFields(params, AUTH)){
			Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
			DataBean user=null;
			db.open();
			try {
				user = db.getBean("email", (String) params.get("email"));
			} catch (BeanException e) {
				db.close();
				return false;
			}
			db.close();
			if(user!=null){
				return (((User) user).getToken().equals((String) params.get("token")))?true:false;
			}
			else
				return false;
		}
		else
			return false;
	}
}
