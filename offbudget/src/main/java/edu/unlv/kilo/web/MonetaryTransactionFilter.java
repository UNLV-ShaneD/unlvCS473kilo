/**
 * 
 */
package edu.unlv.kilo.web;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import edu.unlv.kilo.domain.TransactionEntity;

/**
 * @author Shane
 * A transaction filter represents some query by the user to narrow a list of monetary transactions
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public interface MonetaryTransactionFilter {
	public abstract boolean checkPasses(TransactionEntity transaction);
	public abstract String getUnsafePrettyDescription();
}
