package edu.unlv.kilo.web;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unlv.kilo.domain.TransactionEntity;

/**
 * Used to translate string representations of commands into an enumeration for O(log(n)) command lookups
 * @author Shane
 *
 */
public enum TaggingAction {
	REMOVE("REMOVE"),
	DELETE("DELETE"),
	FILTER("FILTER"),
	REMOVEFILTER("REMOVEFILTER"),
	ITEMCREATE("ITEMCREATE"),
	ITEMUSE("ITEMUSE"),
	REMOVEALLFILTERS("REMOVEALLFILTERS");
	
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
	
}
