package edu.unlv.kilo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/tagging/**")
@Controller
public class TaggingController {

	@RequestMapping(method = RequestMethod.POST, value = "{id}")
	public void post(@PathVariable Long id, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
	}

	@RequestMapping(method = RequestMethod.GET)
	public void get(ModelMap modelMap) {
		modelMap.addAttribute("msg","helloworld");
	}

	@RequestMapping
	public String index() {
		return "tagging/index";
	}
}
