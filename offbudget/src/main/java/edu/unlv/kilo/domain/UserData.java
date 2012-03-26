package edu.unlv.kilo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserData {
    @ManyToMany(cascade = CascadeType.ALL)
	List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();
    @ManyToMany(cascade = CascadeType.ALL)
	List<ItemEntity> items = new ArrayList<ItemEntity>();
}
