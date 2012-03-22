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

	// WTF: This never gets called when post data is submitted
	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public void post(@PathVariable Long id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
	}

	

	// WTF: This never gets called when post data is submitted
	@RequestMapping(method = RequestMethod.POST)
	public void pasta(ModelMap modelMap, HttpServletRequest request) {
//		modelMap.addAttribute("msg","</h4><h3>helloworld</h3><h4>");
		TaggingView taggingView = new TaggingView();
		taggingView.render(modelMap);

		test = 2;
		modelMap.addAttribute("test", test);
		
	}

	// WTF: This never gets called when post data is submitted
	@RequestMapping(method = RequestMethod.GET)
	public String get(ModelMap modelMap, HttpServletRequest request) {
//		modelMap.addAttribute("msg","</h4><h3>helloworld</h3><h4>");
		TaggingView taggingView = new TaggingView();
		taggingView.render(modelMap);

		test = 1;
		modelMap.addAttribute("test", test);
		
		return "tagging/index"; // this return does nothing
	}

	@RequestMapping
	public String index() {
		return "tagging/index";
	}
}
