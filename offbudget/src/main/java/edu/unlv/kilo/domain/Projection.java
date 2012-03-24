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
	private static List<MoneyValue> current_balance;
	
	// Private constructor prevents instantiation from other classes.
    private Projection() { 	
    	Projection.current_balance = null;
    }
    
    /**
     * Calculates the data points to give to Charting.
     * 
     * @param startDate The date to begin the projection.
     * @param endDate The date to finish the projection.
     * @param interval The interval with which to calculate the data points (i.e. weekly, monthly, etc).
     * @return The requested data for plotting the graphs in a List.
     */
    public List<MoneyValue> getData(Date startDate, Date endDate, int interval){
    	
    	
    	
    	return current_balance;
    }

	
}

