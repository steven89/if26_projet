package fr.utt.if26.cs.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;

import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.io.JsonEcho;
import fr.utt.if26.cs.model.LoginManager;

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
		Echo out = new JsonEcho(response.getWriter());
		String paramStr = "";
		String line = "";
		while((line = request.getReader().readLine()) != null){
			paramStr += line;
		}
		BSONObject params = (BasicBSONObject) JSON.parse(paramStr);
		try {
			out.echo(LoginManager.logOut(params));
		} catch (BeanException e) {
			out.echo(e.getMessage());
			e.printStackTrace();
		}
	}

}
