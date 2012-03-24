package edu.unlv.kilo.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.unlv.kilo.domain.MoneyValue;
import edu.unlv.kilo.domain.TransactionDescription;
import edu.unlv.kilo.domain.TransactionEntity;

@RequestMapping("/tagging/**")
@Controller
public class TaggingController {
	List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();
	List<MonetaryTransactionFilter> filters = new ArrayList<MonetaryTransactionFilter>();

	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public void post(@PathVariable Long id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
	}
	
	private void addDummyTransactionsAndFilter(List<TransactionEntity> transactions) {

		Calendar calendar = Calendar.getInstance();
		{
			TransactionEntity transaction = new TransactionEntity();
			transaction.setAmount(new MoneyValue(300));
			calendar.set(2012, 03, 01);
			transaction.setTimeof(calendar.getTime());
			TransactionDescription desc = new TransactionDescription();
			desc.setDescription("Test transaction A");
			transaction.setDescription(desc);
			
			transactions.add(transaction);
		}
		
		{
			TransactionEntity transaction = new TransactionEntity();
			transaction.setAmount(new MoneyValue(300));
			calendar.set(2012, 03, 02);
			transaction.setTimeof(calendar.getTime());
			TransactionDescription desc = new TransactionDescription();
			desc.setDescription("Test transaction B");
			transaction.setDescription(desc);
			
			transactions.add(transaction);
		};
		
		{
			TransactionEntity transaction = new TransactionEntity();
			transaction.setAmount(new MoneyValue(300));
			calendar.set(2012, 03, 05);
			transaction.setTimeof(calendar.getTime());
			
			TransactionDescription desc = new TransactionDescription();
			desc.setDescription("Rest tiontracsan sigma");
			transaction.setDescription(desc);
			
			transactions.add(transaction);
		};
		
		try {
			MonetaryTransactionFilterDescription filter = new MonetaryTransactionFilterDescription("sigma", true);
//			filters.add(filter);
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public void actionDelete(int index) {
		TransactionEntity transaction = transactions.get(index);
	}
	
	public void actionRemove(int index) {
		transactions.remove(index);
	}
	
	public void actionFilter(String filterText, boolean exclusive) {
		try {
			MonetaryTransactionFilterDescription filter = new MonetaryTransactionFilterDescription(filterText, exclusive);
			filters.add(filter);
		} catch (Exception e) {
			// Non-critical invalid input detected; do nothing
		}
	}
	
	public void actionRemoveFilter(int index) {
		filters.remove(index);
	}

	@RequestMapping
	public String index(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		if (transactions.size() == 0) {
			transactions = new ArrayList<TransactionEntity>();
			addDummyTransactionsAndFilter(transactions);
			session.setAttribute("transactions", transactions);
		}
		
		// Execute any posted commands (e.g. delete transaction, remove transaction)
		try {
			String command = request.getParameter("method");
			TaggingAction action = TaggingAction.get(command);
			
			String id = request.getParameter("id");
			int index = Integer.parseInt(id);
			
			switch (action) {
			case DELETE:
				actionDelete(index);
				break;
			case REMOVE:
				actionRemove(index);
				break;
			case FILTER:
				String filterText = request.getParameter("filterText");
				String filterExclusive = request.getParameter("filterExclusive");
				
				actionFilter(filterText, filterExclusive != null);
				break;
			case REMOVEFILTER:
				actionRemoveFilter(index);
				break;
			}
			
//			action.execute(this);
		} catch (NumberFormatException e) {
			// Do nothing
		} catch (IndexOutOfBoundsException e) {
			// Do nothing
		}

		// Build model
		modelMap.addAttribute("transactions", transactions);
		modelMap.addAttribute("filters", filters);

		// Filter transactions
		List<TransactionEntity> filteredTransactions = new LinkedList<TransactionEntity>();
		List<TransactionEntity> antifilteredTransactions = new LinkedList<TransactionEntity>();
		filterTransactions(filteredTransactions, antifilteredTransactions);
		modelMap.addAttribute("filteredtransactions", filteredTransactions);
		modelMap.addAttribute("antifilteredtransactions", antifilteredTransactions);
		
		return "tagging/index";
	}
	
	private void filterTransactions(List<TransactionEntity> filteredTransactions, List<TransactionEntity> antifilteredTransactions) {
		for (TransactionEntity transaction : transactions) {
			boolean pass = true;
			for (MonetaryTransactionFilter filter : filters) {
				if (!filter.checkPasses(transaction)) {
					pass = false;
					break;
				}
			}
			
			if (pass) {
				filteredTransactions.add(transaction);
			} else {
				antifilteredTransactions.add(transaction);
			}
		}
	}
	
	/**
	 * INTERNAL: Process requests to remove a transaction from selection, delete a transaction from the system, etc.
	 * @param command textual representation of action
	 * @param id index of affected transaction from current session's transactions list
	 */
//	private void processCommand(TaggingAction action, TransactionEntity transaction) {
//		action.execute(transaction);
//	}
}
