package fr.utt.if26.cs.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.BsonEcho;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.model.LoginManager;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.ServletUtils;
import fr.utt.if26.cs.utils.TransactionsUtils;
import fr.utt.if26.cs.utils.UserUtils;

/**
 * Servlet implementation class UserWallet
 */
@WebServlet("/UserWallet")
public class UserWallet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserWallet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Echo out = new BsonEcho(response.getWriter());
		BasicBSONObject params = ServletUtils.extractRequestData(request);
		if(LoginManager.checkAuth(params)){
			try {
				BSONObject obj = TransactionsUtils.toBSON(
					TransactionsUtils.getUserTransactions(
						UserUtils.getUserFromEmail(params.getString("email"))
					)
				);
				out.echo("{'balance':'"+obj.get("balance")+"'}");
			} catch (BeanException e) {
				out.echo("fail");
			}
		}
		else
			out.echo(Echo.ERR, "auth required");
	}

}
