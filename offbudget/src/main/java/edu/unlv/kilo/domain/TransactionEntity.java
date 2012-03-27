package edu.unlv.kilo.domain;

import java.text.SimpleDateFormat;
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
public class TransactionEntity {

	private boolean recurring = false;

	@ManyToOne
	private MoneyValue amount = null;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeof = null;

	@ManyToOne
	private TransactionDescription description = null;

	public TransactionEntity(boolean recurring, MoneyValue amount, Date timeof,
			TransactionDescription description) {
		this.recurring = recurring;
		this.amount = amount;
		this.timeof = timeof;
		this.description = description;
	}

	/**
	 * Returns the timeof in user-readable format
	 * 
	 * @return
	 */
	public String getPrettyTimeof() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy' 'MMM' 'd");
		return format.format(timeof);
	}

	/**
	 * Checks if the description contains a substring
	 * 
	 * @param query
	 *            substring to search for
	 * @return
	 */
	public boolean descriptionHasSubstring(String query) {
		return description.descriptionHasSubstring(query);
	}
}
