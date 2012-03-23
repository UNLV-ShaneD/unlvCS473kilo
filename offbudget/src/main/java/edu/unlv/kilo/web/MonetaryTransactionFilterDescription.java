/**
 * 
 */
package edu.unlv.kilo.web;

import edu.unlv.kilo.domain.TransactionEntity;

/**
 * @author Shane
 * A transaction filter that matches transactions by a substring match in the description
 */
public class MonetaryTransactionFilterDescription implements
		MonetaryTransactionFilter {
	
	String query;
	boolean exclusive;
	
	public MonetaryTransactionFilterDescription(String query, boolean exclusive) throws Exception {
		if (query.length() == 0)
		{
			throw new Exception("Cannot create a MonetaryTransactionFilterDescription with an empty query");
		}
		
		this.query = query;
		this.exclusive = exclusive;
	}

	/* (non-Javadoc)
	 * @see edu.unlv.kilo.web.MonetaryTransactionFilter#checkPasses(edu.unlv.kilo.domain.TransactionEntity)
	 */
	@Override
	public boolean checkPasses(TransactionEntity transaction) {
		return transaction.descriptionHasSubstring(query) != exclusive;
	}

}
