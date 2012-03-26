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
public class BudgetBranch {
	public String description;
	
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<BudgetBranch> subbranches = new HashSet<BudgetBranch>();
    
    public BudgetBranch(String description) {
    	this.description = description;
    	persist();
    }
    
    public void addSubBranch(BudgetBranch subbranch) {
    	subbranches.add(subbranch);
    	persist();
    }
    
    public void removeSubBranch(BudgetBranch subbranch) {
    	subbranches.remove(subbranch);
    	persist();
    }
}
