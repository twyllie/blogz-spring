package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean isValidUsername = false, isValidPassword = false, passMatch = false;
		
		if (User.isValidUsername(username)){
			isValidUsername = true;
		}else{
			model.addAttribute("username", username);
			model.addAttribute("username_error", "This is not a valid username.");
		}
		
		if (User.isValidPassword(password)){
			isValidPassword = true;
		}else{
			model.addAttribute("username_error", "This is not a valid password.");
		}
		
		if (password.equals(request.getParameter("verify"))){
			passMatch = true;
		}else{
			model.addAttribute("username_error", "Your passwords do not match.");
		}
		
		
		if(isValidUsername && isValidPassword && passMatch){
			User user = new User(username, password);
			userDao.save(user);
			setUserInSession(request.getSession(), user);
			return "redirect:blog/newpost";
		}

		return "/signup";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		
		if (User.isValidUsername(username)){
			User user = userDao.findByUsername(username);
			if (user != null){
				if (user.isMatchingPassword(password)){
					setUserInSession(request.getSession(), user);
					return "redirect:blog/newpost";
				}
			}
		}else{
			model.addAttribute("error", "Either the username or password are incorrect.");
			model.addAttribute("username", username);
		}
		

		return "/login";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
