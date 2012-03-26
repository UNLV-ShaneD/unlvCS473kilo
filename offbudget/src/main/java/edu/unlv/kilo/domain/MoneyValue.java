package edu.unlv.kilo.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooSerializable
public class MoneyValue {

	/**
	 * Construct a MoneyValue from an amount in cents
	 * 
	 * @param cents
	 */
	public MoneyValue(long cents) {
		this.amount = cents;
		persist();
	}

	private static final long serialVersionUID = 1L;
	private long amount;

	/**
	 * Returns a friendly-formatted string of the value ex: $1.23
	 * 
	 * @return Value in user-readable format
	 */
	@Bean
	public String getPrintable() {
		java.text.DecimalFormat format = new java.text.DecimalFormat(
				"$0.00;($0.00)");

		return format.format(((double) amount) / 100.0);
	}
}
