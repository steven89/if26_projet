package fr.utt.if26.cs.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.BsonEcho;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.LoginManager;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.ServletUtils;
import fr.utt.if26.cs.utils.TransactionsUtils;
import fr.utt.if26.cs.utils.UserUtils;


/**
 * Servlet implementation class Transaction
 */
@WebServlet("/Transaction")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GET /Transaction");
		Echo out = new BsonEcho(response.getWriter());
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BasicBSONObject params = ServletUtils.extractRequestData(request);
		if(LoginManager.checkAuth(params)){
			User user;
			try {
				user = UserUtils.getUserFromEmail(params.getString("email"));
				if(params.containsField("id")){
					if(ObjectId.isValid(params.getString("id"))){
						Transaction transaction=null;
						try {
							transaction = (Transaction) dbTransactions.getBean("id", params.getString("id"));
						} catch (BeanException e) {
							out.echo(e.getMessage());
						}
						if(transaction!=null && ( transaction.getFrom().equals(user.getEmail()) || transaction.getTo().equals(user.getTag()) ))
							out.echo(transaction.toJSONString());
						else
							out.echo(Echo.ERR, "not allowed");
					}
					else{
						out.echo("{'error':'invalid_id'}");
					}
				} 
				else {
					try {
						ArrayList<DataBean>[] transactions = TransactionsUtils.getUserTransactions(user);
						out.echo(TransactionsUtils.toBSON(transactions));
					} catch (BeanException e) {
						out.echo(e.getMessage());
						e.printStackTrace();
					}
				}
			} catch (BeanException e1) {
				System.out.println(e1.getMessage());
				out.echo(Echo.ERR, "auth required");
			}
			
		}
		else
			out.echo(Echo.ERR, "auth required");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST /Transaction");
		Echo out = new BsonEcho(response.getWriter());
		BasicBSONObject params = ServletUtils.extractRequestData(request);
		if(LoginManager.checkAuth(params)){
			if(ServletUtils.checkRequiredFields(new String[] {"email", "to", "amount"}, params)){
				Transaction transaction=null;
				
				try {
					User user = UserUtils.getUserFromEmail(params.getString("email"));
					transaction = new Transaction(
						params.getInt("amount"), 
						user.getTag(),
						params.getString("to")
					);
					TransactionsUtils.doTransaction(transaction);
					out.echo(transaction.toJSONString());
				} catch (BeanException e1) {
					out.echo(e1.getMessage());
					e1.printStackTrace();
				}
			}
			else
				out.echo(Echo.ERR, "fields_missing");
		}
		else
			out.echo(Echo.ERR, "auth required");
	}
}
