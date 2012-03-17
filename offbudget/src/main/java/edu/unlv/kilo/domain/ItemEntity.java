package edu.unlv.kilo.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ItemEntity {

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions = new HashSet<TransactionEntity>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ItemAdjustment> adjustments = new HashSet<ItemAdjustment>();
}
