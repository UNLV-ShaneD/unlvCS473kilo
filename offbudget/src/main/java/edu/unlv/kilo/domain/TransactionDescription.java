package edu.unlv.kilo.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooSerializable
public class TransactionDescription {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6629381691976834814L;

	private String description;

    private String comment;
}
