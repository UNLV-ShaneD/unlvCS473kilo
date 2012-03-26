package edu.unlv.kilo.web;

import java.util.ArrayList;
import java.util.Calendar;
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

import edu.unlv.kilo.domain.ItemEntity;
import edu.unlv.kilo.domain.MoneyValue;
import edu.unlv.kilo.domain.TransactionDescription;
import edu.unlv.kilo.domain.TransactionEntity;

@RequestMapping("/tagging/**")
@Controller
/**
 * Sorts transactions into items
 * Not safe with concurrent multi-actor operations on the same user's data
 * @author Shane
 *
 */
public class TaggingController {
	
	// This transactions list is independent of the user transaction list - this way, we can filter only transactions that come from a scrape
	List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();
	List<ItemEntity> items = new ArrayList<ItemEntity>();
	List<MonetaryTransactionFilter> filters = new ArrayList<MonetaryTransactionFilter>();

	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public void post(@PathVariable Long id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
	}
	
	private void addDummyTransactionsAndFilter(List<TransactionEntity> transactions) {

		Calendar calendar = Calendar.getInstance();
		{
			calendar.set(2012, 03, 01);
			TransactionDescription desc = new TransactionDescription("Test transaction A", "");
			TransactionEntity transaction = new TransactionEntity(true, new MoneyValue(300), calendar.getTime(), desc);
			
			transactions.add(transaction);
		}
		
		{
			calendar.set(2012, 03, 02);
			TransactionDescription desc = new TransactionDescription("Test transaction B", "");
			TransactionEntity transaction = new TransactionEntity(true, new MoneyValue(300), calendar.getTime(), desc);
			
			transactions.add(transaction);
		};
		
		{
			calendar.set(2012, 03, 05);
			TransactionDescription desc = new TransactionDescription("Rest tiontracsan sigma", "");
			TransactionEntity transaction = new TransactionEntity(true, new MoneyValue(300), calendar.getTime(), desc);
			
			transactions.add(transaction);
		};
		
		try {
			MonetaryTransactionFilterDescription filter = new MonetaryTransactionFilterDescription("sigma", true);
			filters.add(filter);
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public void actionDelete(int index) {
//		TransactionEntity transaction = transactions.get(index);
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
	
	public void actionRemoveAllFilters() {
		filters.clear();
	}
	
	/**
	 * Find if any of the selected transactions are associated with an item already - if so, remove them from that item
	 * @param transactions
	 */
	private void unitemizeTransactions(List<TransactionEntity> transactions) {
		for (ItemEntity loopItem : items) {
			loopItem.removeTransactions(transactions);
		}
	}
	
	/**
	 * Create an item from the active selection of transactions, then clear the current filter set
	 * 
	 * @param itemDescription
	 * @param itemInflation
	 * @param itemRecurrenceAutomatic
	 * @param itemRecurrenceInterval
	 */
	public void actionItemCreate(String itemDescription, boolean itemInflation, boolean itemRecurrenceAutomatic, int itemRecurrenceInterval) {
		ItemEntity item = new ItemEntity(itemDescription, itemInflation, itemRecurrenceAutomatic, itemRecurrenceInterval);
		
		// Filter the transactions
		List<TransactionEntity> filteredTransactions = new LinkedList<TransactionEntity>();
		List<TransactionEntity> antifilteredTransactions = new LinkedList<TransactionEntity>();
		filterTransactions(filteredTransactions, antifilteredTransactions);
		
		unitemizeTransactions(filteredTransactions);
		
		// Remove filtered transactions from transaction pool
		transactions.removeAll(filteredTransactions);
		
		// Add selected transactions to the new item
		item.addTransactions(filteredTransactions);
		
		// Save our new item
		items.add(item);
		
		// Now clear active filters
		actionRemoveAllFilters();
	}
	
	/**
	 * Use an existing item to add the active selection of transactions to, then clear the current filter set
	 * @param item
	 */
	private void actionItemUse(ItemEntity item) {
		
		// Filter the transactions
		List<TransactionEntity> filteredTransactions = new LinkedList<TransactionEntity>();
		List<TransactionEntity> antifilteredTransactions = new LinkedList<TransactionEntity>();
		filterTransactions(filteredTransactions, antifilteredTransactions);
		
		unitemizeTransactions(filteredTransactions);
		
		// Remove filtered transactions from transaction pool
		transactions.removeAll(filteredTransactions);
		
		// Add selected transactions to the item
		item.addTransactions(filteredTransactions);

		// Now clear active filters
		actionRemoveAllFilters();
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
			case REMOVEALLFILTERS:
				actionRemoveAllFilters();
				break;
			case ITEMCREATE:
				String itemDescription = request.getParameter("newitemDescription");
				boolean itemInflation = request.getParameter("newitemInflation") == null;
				String itemRecurrenceAutomaticString = request.getParameter("newitemRecurrence");
				boolean itemRecurrenceAutomatic = itemRecurrenceAutomaticString.compareTo("automatic") == 0;
				String itemRecurrenceIntervalString = request.getParameter("newitemRecurrenceInterval");
				int itemRecurrenceInterval = 0;
				if (!itemRecurrenceAutomatic) {
					itemRecurrenceInterval = Integer.parseInt(itemRecurrenceIntervalString);
				}
				if (itemDescription.length() == 0)
				{
					break;
				}
				if (!itemRecurrenceAutomatic && itemRecurrenceInterval <= 0)
				{
					break;
				}
				actionItemCreate(itemDescription, itemInflation, itemRecurrenceAutomatic, itemRecurrenceInterval);
				break;
			case ITEMUSE:
				String itemIndexString = request.getParameter("useitemDescription");
				int itemIndex = Integer.parseInt(itemIndexString);
				
				ItemEntity item = items.get(itemIndex);
				
				actionItemUse(item);
				
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
		modelMap.addAttribute("items", items);

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








/*
Removed from index.jspx:
					<td>
						<form id="command" action="/offbudget/tagging/index" method="post">
							<input type="hidden" name="method" value="DELETE" /> <input
								type="hidden" name="id" value="${transactionStatus.index}" /> <input
								onclick="return confirm('Are you sure want to delete this transaction from offBudget?');"
								value="Delete Transaction" type="image"
								title="Delete transaction"
								src="/offbudget/resources/images/delete.png" class="image"
								alt="Delete Transaction" />
						</form>
					</td>
 */

