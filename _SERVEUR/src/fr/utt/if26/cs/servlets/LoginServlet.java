package fr.utt.if26.cs.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.Crypt;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String base = "users";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		BSONObject params = new BasicBSONObject();
		for(String key : request.getParameterMap().keySet()){
			//out.println(key+" : "+request.getParameter(key));
			params.put(key, request.getParameter(key));
		}
		boolean hasRequiredFields = true;
		String requiredFields[] = {"email", "pass"};
		for(String field : requiredFields){
			if(!params.containsField(field))
				hasRequiredFields = false;
		}
		if(hasRequiredFields){
			User user = new User((String) params.get("email"), (String) params.get("pass"));
			DataBean bean=null;
			Database db = DatabaseManager.getInstance().getBase(LoginServlet.base);
			db.open();
			bean = db.getBean("email", user.getEmail());
			db.close();
			if(bean!=null)
				if(Crypt.match(user.getPass(), ((User) bean).getPass())){
					out.println("auth_ok");
				}
				else{
					out.println("{'error':'auth_error'}");
				}
			else
				out.println("{'error':'auth_unknow'}");
		}
		else{
			out.println("{'error':'field_missing'}");
		}
	}

}
