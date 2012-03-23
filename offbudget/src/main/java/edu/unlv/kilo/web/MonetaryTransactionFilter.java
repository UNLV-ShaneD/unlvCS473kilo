/**
 * 
 */
package edu.unlv.kilo.web;

import edu.unlv.kilo.domain.TransactionEntity;

/**
 * @author Shane
 * A transaction filter represents some query by the user to narrow a list of monetary transactions
 */
public interface MonetaryTransactionFilter {
	public abstract boolean checkPasses(TransactionEntity transaction);
}
