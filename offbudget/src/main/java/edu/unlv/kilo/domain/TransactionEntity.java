package edu.unlv.kilo.domain;

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

    @ManyToOne
    private MoneyValue amount;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeof;

    @ManyToOne
    private TransactionDescription description;
    
    public String getTDset() {
    	return "	<td>" + getTimeof() + "</td>" +
				"	<td>" + getDescription().getDescription() + "</td>" +
				"	<td>" + getAmount().getPrintable() + "</td>";
    }

    /**
     * Checks if the description contains a substring
     * @param query substring to search for
     * @return
     */
	public boolean descriptionHasSubstring(String query) {
		return description.descriptionHasSubstring(query);
	}
}
