package edu.unlv.kilo.web;

import edu.unlv.kilo.domain.TransactionDescription;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/transactiondescriptions")
@Controller
@RooWebScaffold(path = "transactiondescriptions", formBackingObject = TransactionDescription.class)
public class MainController {
}
