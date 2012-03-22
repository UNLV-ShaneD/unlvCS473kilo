package edu.unlv.kilo.web;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	int test;

	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public void post(@PathVariable Long id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
	}

	@RequestMapping
	public String index(ModelMap modelMap, HttpServletRequest request) {
		test = 3;
		modelMap.addAttribute("test", request.getParameter("_method") + request.getParameter("_id"));

		TaggingView taggingView = new TaggingView();
		taggingView.render(modelMap);
		
		return "tagging/index";
	}
}
