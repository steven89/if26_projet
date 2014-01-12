package fr.utt.if26.cs.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;

import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.BsonEcho;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.io.JsonEcho;
import fr.utt.if26.cs.model.LoginManager;
import fr.utt.if26.cs.utils.ServletUtils;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * GET /Login not allowed : return 404
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Echo out = new BsonEcho(response.getWriter());
		BSONObject params = ServletUtils.extractRequestData(ServletUtils.PUT, request);
		try {
			String logInfos = LoginManager.logIn(params);
			if(logInfos.contains("{"))
				out.echo(logInfos);
			else
				out.echo(Echo.ERR, logInfos);
		} catch (BeanException e) {
			out.echo(Echo.ERR, e.getMessage());
			e.printStackTrace();
		}
	}
}
