package fr.utt.if26.cs.model;

import java.util.HashMap;

import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.utils.Crypt;

/**
 * bean used for an user
 * @author steven
 */
public class User extends DataBean {
	
	public final static String SYS_USER = "admin@system";
	
	private String id=null, email, pass, prenom, nom, token, tag;
	private int wallet=0;
	private HashMap<String, Transaction> transactions;
	
	
	/**
	 * Constructeur pour la recup�ration d'un utilisateur en DB
	 * @param id
	 * @param email
	 * @param pass : crypt�
	 * @param prenom
	 * @param nom
	 * @param tag
	 * @param coins
	 * @throws BeanException 
	 */
	public User(String id, String email, String pass, String prenom, String nom, String tag) throws BeanException{
		this.setId(id);
		this.setEmail(email);
		this.setPass(pass, true);
		this.setPrenom(prenom);
		this.setNom(nom);
		this.setTag(tag);
		this.export = new String[]{"id","email","pass","prenom","nom","token","tag","wallet"};
	}
	
	/**
	 * Constructeur pour la cr�ation d'un nouvel utilisateur
	 * @param email
	 * @param pass : non-crypt� + stock� crypt� dans l'objet
	 * @param prenom
	 * @param nom
	 * @param tag
	 * @param useDefaultWallet : le nouvel utilisateur aura un solde par d�fault
	 * @throws BeanException 
	 */
	public User(String email, String pass, String prenom, String nom, String tag) throws BeanException{
		this.setEmail(email);
		this.setPass(pass);
		this.setPrenom(prenom);
		this.setNom(nom);
		this.setTag(tag);
		this.setToken(LoginManager.generateToken());
		this.export = new String[]{"email","pass","prenom","nom","tag","wallet","token"};
	}
	
	/**
	 * Constructeur pour une tentative de login
	 * @param email
	 * @param pass : non crypt� + stock� en clair dans l'objet (pour comparaison via {@link Crypt#match(String, String)})
	 * @throws BeanException 
	 */
	public User(String email, String pass) throws BeanException{
		this.setEmail(email);
		this.setPass(pass, true);
		this.export = new String[]{"email","pass"};
	}
	
	public void setId(String id){
		if(this.id==null)
			this.id = id;
	}
	
	public void setEmail(String email) throws BeanException{
		if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$") || email.equals(User.SYS_USER))
			this.email = email;
		else
			throw new BeanException("invalid email");
	}
	
	/**
	 * Set user password using non-crypted pass
	 * @param pass password
	 */
	public void setPass(String pass){
		this.setPass(pass, false);
	}
	
	/**
	 * Set user password
	 * @param pass password
	 * @param isCrypted whether or not the pass is already crypted or needs to be
	 */
	public void setPass(String pass, boolean isCrypted){
		this.pass = (isCrypted)?pass:Crypt.crypt(pass);
	}
	
	public void setPass(String pass, String salt){
		this.pass = Crypt.crypt(pass, salt);
	}
	
	public void setPrenom(String prenom) throws BeanException{
		if(prenom.matches("^[a-zA-Z]{2,}$"))
			this.prenom = prenom;
		else
			throw new BeanException("invalid prenom");
	}
	
	public void setNom(String nom) throws BeanException{
		if(nom.matches("^[a-zA-Z]{2,}$"))
			this.nom = nom;
		else
			throw new BeanException("invalid nom");
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public void setTag(String tag) throws BeanException{
		if(tag.matches("^[a-zA-Z0-9]{3,}$"))
			this.tag = tag;
		else
			throw new BeanException("invalid tag");
	}
	
	public void setWallet(int coins) throws BeanException{
		if(coins>=0)
			this.wallet = coins;
		else
			throw new BeanException("invalid wallet");
	}
	
	public void setTransactions(HashMap<String, Transaction> t){
		this.transactions = t;
	}
	
	public void addTransaction(Transaction t){
		this.transactions.put(t.getId(), t);
	}
	
	public void removeTransaction(String id){
		this.transactions.remove(id);
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getPass(){
		return this.pass;
	}
	
	public String getPrenom(){
		return this.prenom;
	}
	
	public String getNom(){
		return this.nom;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public int getWallet(){
		return this.wallet;
	}
	
}
