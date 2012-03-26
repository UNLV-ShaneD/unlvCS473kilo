package edu.unlv.kilo.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.ui.ModelMap;

import edu.unlv.kilo.web.MonetaryTransactionFilter;
import edu.unlv.kilo.web.MonetaryTransactionFilterDescription;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserData {
	@ManyToMany(cascade = CascadeType.ALL)
	List<TransactionEntity> transactionsX = new ArrayList<TransactionEntity>();
	@ManyToMany(cascade = CascadeType.ALL)
	List<ItemEntity> items = new ArrayList<ItemEntity>();
	@ManyToMany(cascade = CascadeType.ALL)
	List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();

	public UserData() {
//		persist();
	}
	
	public static UserData getSessionUserData(HttpSession session) {
		return (UserData)session.getAttribute("UserData");
	}

	public static void setSessionUserData(HttpSession session, UserData userData) {
		session.setAttribute("UserData", userData);
	}

	public void buildTaggingModel(ModelMap modelMap) {
		modelMap.addAttribute("transactions", transactions);
		modelMap.addAttribute("items", items);
	}

	private void addDummyTransactionsAndFilter(
			List<TransactionEntity> transactions) {

		Calendar calendar = Calendar.getInstance();
		{
			calendar.set(2012, 03, 01);
			TransactionDescription desc = new TransactionDescription(
					"Test transaction A", "");
			TransactionEntity transaction = new TransactionEntity(true,
					new MoneyValue(300), calendar.getTime(), desc);

			transactions.add(transaction);
		}

		{
			calendar.set(2012, 03, 02);
			TransactionDescription desc = new TransactionDescription(
					"Test transaction B", "");
			TransactionEntity transaction = new TransactionEntity(true,
					new MoneyValue(300), calendar.getTime(), desc);

			transactions.add(transaction);
		}
		;

		{
			calendar.set(2012, 03, 05);
			TransactionDescription desc = new TransactionDescription(
					"Rest tiontracsan sigma", "");
			TransactionEntity transaction = new TransactionEntity(true,
					new MoneyValue(300), calendar.getTime(), desc);

			transactions.add(transaction);
		}
		;
	}

	public void makeDebugTransactionsAvailable(HttpSession session) {
		if (transactions.size() == 0) {
			transactions = new ArrayList<TransactionEntity>();
			addDummyTransactionsAndFilter(transactions);
			session.setAttribute("transactions", transactions);
		}
	}

	public void transactionDelete(int index) {
		// TransactionEntity transaction = transactions.get(index);
	}

	public void transactionRemove(int index) {
		transactions.remove(index);
	}
	
	/**
	 * Find if any of the selected transactions are associated with an item already - if so, remove them from that item
	 * @param transactions
	 */
	public void unitemizeTransactions(List<TransactionEntity> transactions) {
		for (ItemEntity loopItem : items) {
			loopItem.removeTransactions(transactions);
		}
	}

	public void filterTransactions(List<TransactionEntity> filteredTransactions, List<TransactionEntity> antifilteredTransactions, List<MonetaryTransactionFilter> filters) {
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
	
	public void removeFromPendingTransactions(List<TransactionEntity> transactions) {
		this.transactions.removeAll(transactions);
	}

	public void saveItem(ItemEntity item) {
		items.add(item);
	}

	
	/**
	 * Create an item from the active selection of transactions, then clear the current filter set
	 * 
	 * @param itemDescription
	 * @param itemInflation
	 * @param itemRecurrenceAutomatic
	 * @param itemRecurrenceInterval
	 */
	public void transactionItemCreate(List<MonetaryTransactionFilter> filters, String itemDescription, boolean itemInflation, boolean itemRecurrenceAutomatic, int itemRecurrenceInterval) {
		ItemEntity item = new ItemEntity(itemDescription, itemInflation, itemRecurrenceAutomatic, itemRecurrenceInterval);
		
		// Filter the transactions
		List<TransactionEntity> filteredTransactions = new LinkedList<TransactionEntity>();
		List<TransactionEntity> antifilteredTransactions = new LinkedList<TransactionEntity>();
		filterTransactions(filteredTransactions, antifilteredTransactions, filters);
		
		unitemizeTransactions(filteredTransactions);
		
		// Remove filtered transactions from transaction pool
		removeFromPendingTransactions(filteredTransactions);
		
		// Add selected transactions to the new item
		item.addTransactions(filteredTransactions);
		
		// Save our new item
		saveItem(item);
		
		// Now clear active filters
		filters.clear();
	}
	
	/**
	 * Use an existing item to add the active selection of transactions to, then clear the current filter set
	 * @param item
	 */
	public void transactionItemUse(List<MonetaryTransactionFilter> filters, int itemIndex) {
		ItemEntity item = items.get(itemIndex);
		
		// Filter the transactions
		List<TransactionEntity> filteredTransactions = new LinkedList<TransactionEntity>();
		List<TransactionEntity> antifilteredTransactions = new LinkedList<TransactionEntity>();
		filterTransactions(filteredTransactions, antifilteredTransactions, filters);
		
		unitemizeTransactions(filteredTransactions);
		
		// Remove filtered transactions from transaction pool
		removeFromPendingTransactions(filteredTransactions);
		
		// Add selected transactions to the item
		item.addTransactions(filteredTransactions);

		// Now clear active filters
		filters.clear();
	}
}
