package edu.unlv.kilo.web;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unlv.kilo.domain.TransactionEntity;

public enum TaggingAction {
	REMOVE("REMOVE") {
		@Override
		public void execute(List<TransactionEntity> transactions, int index) {
			transactions.remove(index);
		};
	},
	DELETE("DELETE") {
		@Override
		public void execute(List<TransactionEntity> transactions, int index) {
			TransactionEntity transaction = transactions.get(index);
		};
	};
	
	TaggingAction(String textCommand) {
		this.textCommand = textCommand;
	}
	
	// Create static map for O(log(n)) lookups of commands from their string representation
	private static final Map<String,TaggingAction> lookup = new HashMap<String,TaggingAction>();
	static {
		for (TaggingAction action : EnumSet.allOf(TaggingAction.class)) {
			lookup.put(action.textCommand, action);
		}
	}
	public static TaggingAction get(String textCommand) {
		return lookup.get(textCommand);
	}

	// String representation of an action
	String textCommand;
	
	public abstract void execute(List<TransactionEntity> transactions, int index);
	
}
