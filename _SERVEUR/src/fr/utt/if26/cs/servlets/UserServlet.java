package fr.utt.if26.cs.servlets;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSON;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.ServletUtils;
import fr.utt.if26.cs.utils.TransactionsUtils;

/**
 * Servlet implementation class User
 */
@WebServlet("/User")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int base = DatabaseManager.USERS;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		DataBean bean=null;
		try{
			int id = Integer.parseInt(request.getQueryString());
			Database db = DatabaseManager.getInstance().getBase(UserServlet.base);
			db.open();
			bean = db.getBean("id", Integer.toString(id));
			db.close();
		} catch (NumberFormatException e){
			Database db = DatabaseManager.getInstance().getBase(UserServlet.base);
			db.open();
			if(request.getQueryString().indexOf("@")!=-1){
				bean = db.getBean("email", request.getQueryString());
			}
			else{
				bean = db.getBean("tag", request.getQueryString());
			}
			db.close();	
		}
		if(bean==null)
			out.println("{'error':'404'}");
			//response.sendError(HttpServletResponse.SC_NOT_FOUND);
		else {
			TransactionsUtils.applyTransactionsOnUser(bean);
			out.println(bean.getJSONStringRepresentation(new String[] {"email","tag","prenom","nom","wallet"}));
		}
		//BasicBSONObject bson =  (BasicBSONObject) JSON.parse("{'test': 'aaeaze', 't':'a'}");
		//byte[] bte = BSON.encode(bson);	
		/*OutputStream os = response.getOutputStream();
		os.write(bte);
		os.flush();*/
		/*PrintWriter out = response.getWriter();
		String str = "";
		for(byte b : bte){
			str+=b+"&";
			//out.print(b+"\n");
		}
		out.print(str);
		
//		String decode = new String(bte, "UTF-8");
//		out.print("\n"+decode.toString());
		String[] tab = str.split("&");
		byte[] array = new byte[tab.length];
		for(int i =0; i<tab.length; i++){
//			out.print("\n"+tab[i]);
			array[i] = Byte.valueOf(tab[i]);
		}
		BSONDecoder decoder = new BasicBSONDecoder();
		BasicBSONObject obj = (BasicBSONObject) decoder.readObject(array);
		//out.print(obj.toString());*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.POST, request);
		if(ServletUtils.checkRequiredFields(new String[] {"email", "pass", "prenom", "nom", "tag"}, params)){
			DataBean user = new User(
					params.getString("email"),
					params.getString("pass"),
					params.getString("prenom"),
					params.getString("nom"),
					params.getString("tag"));
			TransactionsUtils.doBaseTransaction(((User) user).getTag());
			Database db = DatabaseManager.getInstance().getBase(UserServlet.base);
			db.open();
			db.insertBean(user);
			db.close();
			
			// ajout de la transaction de depart
			
			out.println(user.getJSONStringRepresentation());
		}
		else{
			out.println("{'error':'field_missing'}");
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
