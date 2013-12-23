package fr.utt.if26.cs.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSON;
import org.bson.BSONDecoder;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.model.User;

/**
 * Servlet implementation class User
 */
@WebServlet("/User")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String base = "users";
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().print("HELLO");
		BasicBSONObject bson =  (BasicBSONObject) JSON.parse("{'test': 'aaeaze', 't':'a'}");
		byte[] bte = BSON.encode(bson);	
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
		String params = "";
		String line = "";
		while((line = request.getReader().readLine()) != null){
			params += line;
		}
		BasicBSONObject jsonParams = (BasicBSONObject) JSON.parse(params);		
		
		String email = jsonParams.getString("email");
		String pass = jsonParams.getString("pass");
		String prenom = jsonParams.getString("prenom");
		String nom = jsonParams.getString("nom");
		String tag = jsonParams.getString("tag");
		User user = new User(email, pass, prenom, nom, tag, true);
		
		Database db = DatabaseManager.getInstance().getBase(this.base);
		db.open();
		db.insertBean(user);
		db.close();
		out.println(user.getJSONStringRepresentation());
//		out.println(params);
//		for(String key : jsonParams.keySet()){
//			out.println(key+" : "+jsonParams.get(key));
//		}
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
