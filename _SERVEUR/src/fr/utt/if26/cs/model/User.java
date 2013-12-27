package fr.utt.if26.cs.model;

import java.util.HashMap;

import fr.utt.if26.cs.utils.Crypt;


public class User extends DataBean {
	
	private String id=null, email, pass, prenom, nom, token, tag;
	private int wallet=0;
	private final static int defaultWallet = 50;
	private HashMap<String, Transaction> transactions;
	
	
	/**
	 * Constructeur pour la recupération d'un utilisateur en DB
	 * @param id
	 * @param email
	 * @param pass : crypté
	 * @param prenom
	 * @param nom
	 * @param tag
	 * @param coins
	 */
	public User(String id, String email, String pass, String prenom, String nom, String tag, int coins){
		this.setId(id);
		this.setEmail(email);
		this.setPass(pass, true);
		this.setPrenom(prenom);
		this.setNom(nom);
		this.setTag(tag);
		this.setWallet(coins);
		this.export = new String[]{"id","email","pass","prenom","nom","token","wallet","tag"};
	}
	
	/**
	 * Constructeur pour la création d'un nouvel utilisateur
	 * @param email
	 * @param pass : non-crypté + stocké crypté dans l'objet
	 * @param prenom
	 * @param nom
	 * @param tag
	 * @param useDefaultWallet : le nouvel utilisateur aura un solde par défault
	 */
	public User(String email, String pass, String prenom, String nom, String tag, boolean useDefaultWallet){
		this.setEmail(email);
		this.setPass(pass);
		this.setPrenom(prenom);
		this.setNom(nom);
		this.setTag(tag);
		if(useDefaultWallet)
			this.setWallet(User.defaultWallet);
		this.export = new String[]{"email","pass","prenom","nom","tag","wallet"};
	}
	
	/**
	 * Constructeur pour une tentative de login
	 * @param email
	 * @param pass : non crypté + stocké en clair dans l'objet (pour comparaison via {@link Crypt#match(String, String)})
	 */
	public User(String email, String pass){
		this.setEmail(email);
		this.setPass(pass, true);
		this.export = new String[]{"email","pass"};
	}
	
	public void setId(String id){
		if(this.id==null)
			this.id = id;
	}
	
	public void setEmail(String email){
		// TODO : regex verification du mail
		this.email = email;
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
	
	public void setPrenom(String prenom){
		this.prenom = prenom;
	}
	
	public void setNom(String nom){
		this.nom = nom;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public void setWallet(int coins){
		this.wallet = coins;
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
