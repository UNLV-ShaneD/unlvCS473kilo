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
public class Branch {
	public String description;
	
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Branch> subbranches = new HashSet<Branch>();
    
    public Branch(String description) {
    	this.description = description;
    	persist();
    }
    
    public void addSubBranch(Branch subbranch) {
    	subbranches.add(subbranch);
    	persist();
    }
    
    public void removeSubBranch(Branch subbranch) {
    	subbranches.remove(subbranch);
    	persist();
    }
}
