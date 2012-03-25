package edu.unlv.kilo.domain;

import java.util.*;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * @author Dane
 * The purpose of the Projection class is to calculate the user's budget based on the items submitted.
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Projection {
	private static List<MoneyValue> balance_data;
	
	// Private constructor prevents instantiation from other classes.
    private Projection() { 	
    	Projection.balance_data = new ArrayList<MoneyValue>();
    }
    
    /**
     * Calculates the data points to give to Charting.
     * 
     * @param startDate The date to begin the projection.
     * @param endDate The date to finish the projection.
     * @param interval The interval in # of days with which to calculate the data points.
     * @return The requested data for plotting the graphs in a List.
     */
    public List<MoneyValue> getData(Date startDate, Date endDate, long interval){
    	MoneyValue current;
    	ItemEntity item = new ItemEntity();						// placeholder
    	ItemAdjustment adjust = new ItemAdjustment();			// placeholder
    	Set<TransactionEntity> trans = item.getTransactions();
    	Iterator<TransactionEntity> it = trans.iterator();
    	
    	long period = endDate.getTime() - startDate.getTime();
    	
    	// The while loop iterates through the Transactions in the Item.
    	while (it.hasNext()){
    		current = it.next().getAmount();
    		
    		if (adjust.determineRecurrence(1, startDate, current))
    			adjust.projectTransactionAmount(1, current);
    	}
    	
    	return balance_data;
    }

	
}

