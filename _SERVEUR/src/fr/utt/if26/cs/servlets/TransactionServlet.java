package fr.utt.if26.cs.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;

import com.mongodb.util.JSON;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.io.Echo;
import fr.utt.if26.cs.io.JsonEcho;
import fr.utt.if26.cs.model.DataBean;
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
		Echo out = new JsonEcho(response.getWriter());
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.GET, request);
		if(params.containsField("id")){
			if(ObjectId.isValid(params.getString("id"))){
				dbTransactions.open();
				Transaction transaction=null;
				try {
					transaction = (Transaction) dbTransactions.getBean("id", params.getString("id"));
				} catch (BeanException e) {
					out.echo(e.getMessage());
					e.printStackTrace();
				}
				dbTransactions.close();
				out.echo(transaction.getJSONStringRepresentation());
			}
			else{
				out.echo("{'error':'invalid_id'}");
			}
		} 
		else {
			if(ServletUtils.checkRequiredFields(new String[]{"tag"}, params)){
				try {
					User userFrom = UserUtils.getUserFromTag(params.getString("tag"));
					ArrayList<DataBean>[] transactions = TransactionsUtils.getUserTransactions(userFrom);
					out.echo(TransactionsUtils.toBSON(transactions));
					
					/*out.echo("{ \n\t'balance' : "+TransactionsUtils.computeTransactions(transactions)+",");
					out.echo("\t'from': [");
					for(DataBean t : transactions[0]){
						System.out.echo(JSON.parse(t.toString()));
						out.echo("\t\t"+t.getJSONStringRepresentation()+",");
					}
					out.echo("\t],"); 
					out.echo("\t'to': [");
					for(DataBean t : transactions[1]){
						out.echo("\t\t"+t.getJSONStringRepresentation()+",");
					}
					out.echo("\t]\n}");*/
				} catch (BeanException e) {
					out.echo(e.getMessage());
					e.printStackTrace();
				}
				
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Echo out = new JsonEcho(response.getWriter());
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.POST, request);
		if(ServletUtils.checkRequiredFields(new String[] {"from", "to", "amount"}, params)){
			Transaction transaction=null;
			try {
				transaction = new Transaction(
					params.getInt("amount"), 
					params.getString("from"),
					params.getString("to")
				);
				TransactionsUtils.doTransaction(transaction);
				out.echo(transaction.getJSONStringRepresentation());
			} catch (BeanException e1) {
				out.echo(e1.getMessage());
				e1.printStackTrace();
			}
		}
	}
}
