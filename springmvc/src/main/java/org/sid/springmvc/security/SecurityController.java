package org.sid.springmvc.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
	  
	  @GetMapping("/error")
	  public String error() {
		  return "notAuthorized";
	  }
	  
	  @GetMapping("/login")
	  public String login() {
		  return "login";
	  }
	 
}
