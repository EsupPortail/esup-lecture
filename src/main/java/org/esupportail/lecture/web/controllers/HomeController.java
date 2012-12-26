package org.esupportail.lecture.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class HomeController {

	@RenderMapping
	public String goHome() {
		return "home";
	}
	
}
