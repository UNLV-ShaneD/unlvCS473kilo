package edu.unlv.kilo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ItemEntity {

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions = new HashSet<TransactionEntity>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ItemAdjustment> adjustments = new HashSet<ItemAdjustment>();

    private String description;
    private boolean inflation;
    
    private boolean recurrenceIsAutomatic;
    private int recurrenceManualInterval;
    
    /**
     * Create a new, empty item entity
     * @param description
     * @param inflation
     * @param recurrenceIsAutomatic
     * @param recurrenceManualInterval
     */
    public ItemEntity(String description, boolean inflation, boolean recurrenceIsAutomatic, int recurrenceManualInterval) {
    	this.description = description;
    	this.inflation = inflation;
    	this.recurrenceIsAutomatic = recurrenceIsAutomatic;
    	this.recurrenceManualInterval = recurrenceManualInterval;
    	
    	persist();
    }
    
    /**
     * Add a transaction to this item
     * Note that this does not guarantee the transaction is only associated with one item - transactions must be manually removed from other items before adding it to another.
     * A single transaction should be associated with at most one item.
     * @param transaction
     */
    public void addTransaction(TransactionEntity transaction) {
    	transactions.add(transaction);
    }
    
    /**
     * Add transactions to this item
     * Note that this does not guarantee the transactions are only associated with one item - transactions must be manually removed from other items before adding it to another.
     * A single transaction should be associated with at most one item.
     * @param transaction
     */
    public void addTransactions(List<TransactionEntity> transactions) {
    	this.transactions.removeAll(transactions);
    }
    
    /**
     * Remove a transaction from this item
     * @param transaction
     */
    public void removeTransaction(TransactionEntity transaction) {
    	transactions.remove(transaction);
    }

    /**
     * Remove several transactions from this item each if they are present
     * @param transactions
     */
	public void removeTransactions(List<TransactionEntity> transactions) {
		this.transactions.removeAll(transactions);
	}
    
    /**
     * Calculate the average interval to find the base interval between transactions for this item
     * @return Average number of days between transactions. 0 if an interval cannot be calculated.
     */
    public int getBaseRecurrenceInterval() {
    	if (!recurrenceIsAutomatic) {
    		return recurrenceManualInterval;
    	}

    	// Find the average interval from recurring transactions
    	if (transactions.size() <= 1) {
    		return 0;
    	}
    	
    	boolean first = true;
    	Date firstDate = null;
    	Date lastDate = null;
    	int numberRecurringTransactions = 0;
    	
    	for (TransactionEntity transaction : transactions) {
    		// Disregard non-recurring transactions
    		if (!transaction.isRecurring()) {
    			continue;
    		}

			++numberRecurringTransactions;
    		
    		if (first && transaction.isRecurring()) {
    			firstDate = transaction.getTimeof();
    			lastDate = transaction.getTimeof();
    			first = false;
    			continue;
    		}
    		
    		lastDate = transaction.getTimeof();
    	}
    	
    	Days days = Days.daysBetween(new DateTime(firstDate), new DateTime(lastDate));
    	
    	return days.getDays() / numberRecurringTransactions;
    }
}
