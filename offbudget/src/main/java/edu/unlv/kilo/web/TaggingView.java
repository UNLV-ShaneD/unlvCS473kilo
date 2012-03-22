/**
 * 
 */
package edu.unlv.kilo.web;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.ui.ModelMap;

import edu.unlv.kilo.domain.MoneyValue;
import edu.unlv.kilo.domain.TransactionDescription;
import edu.unlv.kilo.domain.TransactionEntity;

/**
 * @author Shane
 * 
 * Generates HTML for tagging system
 * Lists transactions provided and allows options for filtering the transactions
 * Has option to mark all of the visible filtered transactions to an expense (/deprecated: itemEntity)
 *
 */
public class TaggingView {
	List<TransactionEntity> transactions;
	
	/**
	 * Creates a tagging view for the specified list of transactions
	 * This view will be used to put said transactions into expenses (deprecated name: itemEntities)
	 * @param transactions List of transactions to prompt for sorting into expenses
	 */
	TaggingView(List<TransactionEntity> transactions) {
		this.transactions = transactions;
	}
	
	/**
	 * Internal: Creates a form for deleting a transaction
	 * @param id
	 * @return
	 */
	private String renderDeleteForm(int id) {
		String out = "";
		out += "<td><form id=\"command\" action=\"/offbudget/tagging/index\" method=\"post\"><input type=\"hidden\" name=\"method\" value=\"REMOVE\"/>";
		out += "<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>";
		out += "<input value=\"Remove Transaction\" type=\"image\" title=\"Remove Transaction\" src=\"/offbudget/resources/images/delete.png\" class=\"image\" alt=\"Remove transaction from selection\"/>";
		out += "</form></td>";
		out += "<td><form id=\"command\" action=\"/offbudget/tagging/index\" method=\"post\"><input type=\"hidden\" name=\"method\" value=\"DELETE\"/>";
		out += "<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>";
		out += "<input onclick=\"return confirm('Are you sure want to delete this transaction from offBudget?');\" value=\"Delete Transaction\" type=\"image\" title=\"Delete transaction\" src=\"/offbudget/resources/images/delete.png\" class=\"image\" alt=\"Delete Transaction\"/>";
		out += "</form></td>";
		return out;
	}
	
	/**
	 * Generates HTML string for rendering by client
	 * @param modelMap
	 */
	void render(ModelMap modelMap) {
		modelMap.addAttribute("transactions", transactions);
		
		String output = "";
		
		// Filter form
		output += "<div><form id=\"command\" action=\"/offbudget/tagging/index\" method=\"post\"><input type=\"hidden\" name=\"method\" value=\"FILTER\"/>";
		output += "<input type=\"hidden\" name=\"id\" value=\"0\"/>";
		output += "<label for=\"filter\">Filter : </filter>";
		output += "<input id=\"filter\" name=\"Filter\" value=\"\" type=\"text\" />";
		output += "</form></div>";
		
		output += "<table>" +
				"	<tr>" +
				"		<th>Date</th>" +
				"		<th>Description</th>" +
				"		<th>Amount</th>" +
				"		<th />" +
				"		<th />" +
				"	</tr>" +
				"";
		
		int id = 0;
		for (TransactionEntity transactionEntity : transactions) {
			output += "<tr>";
			output += transactionEntity.getTDset();
			output += renderDeleteForm(id);
			output += "</tr>";
			
			++id;
		}
		
		output += "</table>";
		
		modelMap.addAttribute("output", output);
	}
}
