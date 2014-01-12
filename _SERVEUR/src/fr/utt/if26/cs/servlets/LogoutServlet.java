package fr.utt.if26.cs.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;


import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.BsonEcho;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.model.LoginManager;
import fr.utt.if26.cs.utils.ServletUtils;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Echo out = new BsonEcho(response.getWriter());
		BSONObject params = ServletUtils.extractRequestData(request);
		try {
			String logInfos = LoginManager.logOut(params);
			if(logInfos.contains("ok"))
				out.echo(Echo.INFO, logInfos);
			else
				out.echo(Echo.ERR, logInfos);
		} catch (BeanException e) {
			out.echo(e.getMessage());
			e.printStackTrace();
		}
	}

}
