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

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.utils.ServletUtils;


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
		PrintWriter out = response.getWriter();
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.GET, request);
		if(params.containsField("id")){
			if(ObjectId.isValid(params.getString("id"))){
				dbTransactions.open();
				Transaction transaction = (Transaction) dbTransactions.getBean("id", params.getString("id"));
				dbTransactions.close();
				out.println(transaction.getJSONStringRepresentation());
			}
			else{
				out.println("{'error':'invalid_id'}");
			}
		} 
		else {
			if(ServletUtils.checkRequiredFields(new String[]{"tag"}, params)){
				BSONObject datas = new BasicBSONObject();
				datas.put("from", params.getString("tag"));
				dbTransactions.open();
				ArrayList<DataBean> transactionsFrom = dbTransactions.findBeans(datas);
				datas = new BasicBSONObject();
				datas.put("to", params.getString("tag"));
				ArrayList<DataBean> transactionsTo = dbTransactions.findBeans(datas);
				dbTransactions.close();
				out.println("{ 'from': [");
				for(DataBean t : transactionsFrom){
					out.println("\t"+t.getJSONStringRepresentation()+", ");
				}
				out.println("], 'to': [");
				for(DataBean t : transactionsTo){
					out.println("\t"+t.getJSONStringRepresentation());
				}
				out.println("]}");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
