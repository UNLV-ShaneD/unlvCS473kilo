package edu.unlv.kilo.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.stereotype.Service;

import edu.unlv.kilo.domain.ItemAdjustment;
import edu.unlv.kilo.domain.ItemEntity;
import edu.unlv.kilo.domain.MoneyValue;
import edu.unlv.kilo.domain.TransactionEntity;

/**
 * @author Dane
 * The purpose of the Projection class is to calculate the user's budget based on the items submitted.
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Service("ProjectionProvider")
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
    public static List<MoneyValue> getGraphData(Calendar startDate, Calendar endDate, int interval){
    	// MoneyValue current = new MoneyValue();
    	Calendar oldDate = Calendar.getInstance();
    	int oldInterval = 0;
    	int iterationNumber = 0;
    	MoneyValue oldValue = new MoneyValue();
    	
    	/*		PLACEHOLDER		*/
    	// receive item
    	ItemEntity item = new ItemEntity();
    	/*						*/
    	
    	// initialize Calendar to a past date
    	oldDate.set(1970, 0, 1);
    	
    	// Iterate through the Transactions in the Item to get the last date.
    	for (TransactionEntity transaction : item.getTransactions()){
    	
    		if (oldDate.compareTo(dateToCalendar(transaction.getTimeof())) <= 0)
    			oldDate.setTime(transaction.getTimeof());
    		
    	}
    	
    	// get the item's interval
    	oldInterval = item.getBaseRecurrenceInterval();
    		
    	// add the interval to get the next projected transaction date
    	oldDate.add(Calendar.DAY_OF_MONTH, oldInterval);
    		
    	// get the base value of the item
    	oldValue = item.getBaseValue();
    	
    	// initialize new variables for iterating through the Adjustments
    	Calendar nextDate = oldDate;
    	MoneyValue nextValue = oldValue;
    	int newInterval = oldInterval;
    	
    	while (nextDate.compareTo(endDate) <= 0){
    		iterationNumber++;
    		
    		for (ItemAdjustment adjust : item.getAdjustments()){
    			// check if this adjustment should be skipped
    			if (!adjust.isEffective(oldDate))
    				continue;
    			
    			newInterval = adjust.projectTransactionInterval(oldInterval);
    			
    			// calculate the new date
    			nextDate.add(Calendar.DAY_OF_MONTH, newInterval);
    			
    			// calculate the new MoneyValue amount
    			nextValue = adjust.projectTransactionAmount(iterationNumber, oldValue);
    			
    			// check for recurrence
    			if (!adjust.determineRecurrence(iterationNumber, nextDate, nextValue)){
    				// revert back to the old values
    				newInterval = oldInterval;
    				nextValue = oldValue;
    			}
    			
    			// update old values for next iteration
    			oldInterval = newInterval;
    			oldValue = nextValue;
    		}
    		
    		balance_data.add(nextValue);
    	}

    	return balance_data;
    }
    
    /** Calculates the number of days between two dates.
     * 
     * @param startDate The beginning date.
     * @param endDate The end date.
     * @return The number of days in between the startDate and endDate.
     */
    public static int daysBetween(Calendar startDate, Calendar endDate) {  
    	  Calendar date = (Calendar) startDate.clone();  
    	  int daysBetween = 0;  
    	  while (date.before(endDate)) {  
    	    date.add(Calendar.DAY_OF_MONTH, 1);  
    	    daysBetween++;  
    	  }  
    	  
    	  return daysBetween;  
    }  
    
    /** Converts a Date type object to a Calendar type object.
     * 
     * @param date The Date object you want to convert to Calendar.
     * @return The Calendar object that you wanted to convert.
     */
    public static Calendar dateToCalendar(Date date){ 
    	  Calendar cal = Calendar.getInstance();
    	  cal.setTime(date);
    	  return cal;
    }
}