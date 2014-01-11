package fr.utt.if26.cs.servlets;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BasicBSONObject;



import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.io.JsonEcho;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.LoginManager;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.ServletUtils;
import fr.utt.if26.cs.utils.TransactionsUtils;

/**
 * Servlet implementation class User
 */
@WebServlet("/User")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Echo out = new JsonEcho(response.getWriter());
		DataBean bean=null;
		try{
			int id = Integer.parseInt(request.getQueryString());
			Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
			bean = db.getBean("id", Integer.toString(id));
		} catch (NumberFormatException e){
			try {
				Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
				if(request.getQueryString().indexOf("@")!=-1)
					bean = db.getBean("email", request.getQueryString());
				else{
					bean = db.getBean("tag", request.getQueryString());
				}
			} catch (BeanException e1) {
				e1.printStackTrace();
			}
		} catch (BeanException e){
			e.printStackTrace();
		}
		if(bean==null)
			out.echo("{'error':'404'}");
			//response.sendError(HttpServletResponse.SC_NOT_FOUND);
		else {
			try {
				TransactionsUtils.applyTransactionsOnUser(bean);
			} catch (BeanException e) {
				out.echo(e.getMessage());
				e.printStackTrace();
			}
			out.echo(bean.getJSONStringRepresentation(new String[] {"email","tag","prenom","nom","wallet"}));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Echo out = new JsonEcho(response.getWriter());
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.POST, request);
		if(ServletUtils.checkRequiredFields(new String[] {"email", "pass", "prenom", "nom", "tag"}, params)){
			DataBean user=null;
			try {
				user = new User(
					params.getString("email"),
					params.getString("pass"),
					params.getString("prenom"),
					params.getString("nom"),
					params.getString("tag")
				);
				Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
				boolean inserted = db.insertBean(user);
				if(inserted){
					TransactionsUtils.doBaseTransaction(((User) user).getTag());
					TransactionsUtils.applyTransactionsOnUser(user);
					out.echo(user.getJSONStringRepresentation());
				}
				else{
					out.echo(Echo.ERR, "user already exists");
				}
			} catch (BeanException e) {
				out.echo(e.getMessage());
				e.printStackTrace();
			}
		}
		else{
			out.echo("{'error':'field_missing'}");
		}
	}
}
