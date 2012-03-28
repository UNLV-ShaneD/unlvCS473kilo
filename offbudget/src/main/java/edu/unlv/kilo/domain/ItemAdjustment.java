package edu.unlv.kilo.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ItemAdjustment {

	/**
	 * A user-friendly name for this adjustment. Not necessarily unique.
	 */
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    /**
     * The first date for which this adjustment should be taken into account
     */
    private Date effectiveDate;
    
    @ManyToOne
    BudgetBranch branch = null;

	/**
	 * Calculates the new interval between the last transaction and the next projected transaction
	 * 
	 * This calculation is based upon the old interval (after other (earlier) adjustments (excluding this adjustment) have been made)
	 * 
	 * @param oldInterval The old number of days until the next transaction (adjusted by higher-priority adjustments first)
	 * @return The new number of days until the next transaction
	 */
    public int projectTransactionInterval(int oldInterval) {
        return oldInterval;
    }

	/**
	 * Calculates the next transaction amount(s)
	 * 
	 * This calculation is based upon the old amount (after earlier adjustments (excluding this adjustment) have been made)
	 * 
	 * @param iterationNumber The number n as in the statement: This is the *n*th projected transaction this adjustment is affecting. For the first call to this method, this parameter will be 1.
	 * @param oldAmount The projected transaction's amount before this adjustment is applied
	 * @return The next projected transaction's amount
	 */
    public MoneyValue projectTransactionAmount(int iterationNumber, MoneyValue oldAmount) {
        return oldAmount;
    }

	/**
	 * Determine whether this adjustment should be run again (recurring)
	 * 
	 * Returns true if we should keep using this adjustment for future transaction projections (recurring adjustment)
	 * Returns false if we should ignore this adjustment for this and future transaction projections (one-off adjustment)
	 * 
	 * @param iterationNumber The number n as in the statement: This is the *n*th projected transaction this adjustment is affecting. For the first call to this method, this parameter will be 1.
	 * @param nextDate The date of the next projected transaction (after being adjusted by this adjustment's projectTransactionInterval method)
	 * @param nextAmount is the amount of the next projected transaction (after being adjusted by this adjustment's projectTransactionAmount method)
	 * @return Whether or not this adjustment should be used in the next transaction projection
	 */
    public boolean determineRecurrence(int iterationNumber, Calendar nextDate, MoneyValue nextAmount) {
        return true;
    }
    
    /** Determines if this adjustment is still effective based on the date given.
     * Returns true if the date passed is before the effective date.
     * Returns false otherwise
     * 
     * @param date The date you want to compare to the effective date.
     * @return true if date < effective date; false otherwise
     */
    public boolean isEffective(Calendar date){
    	if (date.before(effectiveDate))
    		return true;
    	
    	return false;
    }
}
