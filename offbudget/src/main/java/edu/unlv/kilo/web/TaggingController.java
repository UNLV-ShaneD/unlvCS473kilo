package edu.unlv.kilo.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.unlv.kilo.domain.MoneyValue;
import edu.unlv.kilo.domain.TransactionDescription;
import edu.unlv.kilo.domain.TransactionEntity;

@RequestMapping("/tagging/**")
@Controller
public class TaggingController {
	List<TransactionEntity> transactions = new ArrayList<TransactionEntity>();

	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public void post(@PathVariable Long id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
	}
	
	private void addDummyTransactions(List<TransactionEntity> transactions) {

		{
			TransactionEntity a = new TransactionEntity();
			a.setAmount(new MoneyValue(300));
			a.setTimeof(new Date(2012,03,01));
			TransactionDescription desc = new TransactionDescription();
			desc.setDescription("Test transaction A");
			a.setDescription(desc);
			
			transactions.add(a);
		}
		
		{
			TransactionEntity a = new TransactionEntity();
			a.setAmount(new MoneyValue(300));
			a.setTimeof(new Date(2012,03,05));
			TransactionDescription desc = new TransactionDescription();
			desc.setDescription("Test transaction B");
			a.setDescription(desc);
			
			transactions.add(a);
		};
	}

	@RequestMapping
	public String index(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		if (transactions.size() == 0) {
			transactions = new ArrayList<TransactionEntity>();
			addDummyTransactions(transactions);
			session.setAttribute("transactions", transactions);
		}
		
		try {
			String command = request.getParameter("method");
			TaggingAction action = TaggingAction.get(command);
			
			String id = request.getParameter("id");
			int index = Integer.parseInt(id);
			
			action.execute(transactions, index);
		} catch (NumberFormatException e) {
			// Do nothing
		} catch (IndexOutOfBoundsException e) {
			// Do nothing
		}

		TaggingView taggingView = new TaggingView(transactions);
		taggingView.render(modelMap);
		
		
		return "tagging/index";
	}
	
	/**
	 * INTERNAL: Process requests to remove a transaction from selection, delete a transaction from the system, etc.
	 * @param command textual representation of action
	 * @param id index of affected transaction from current session's transactions list
	 */
//	private void processCommand(TaggingAction action, TransactionEntity transaction) {
//		action.execute(transaction);
//	}
}
