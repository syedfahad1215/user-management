package com.nt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nt.entity.User;
import com.nt.entity.dto.AuthenticationDTO;
import com.nt.entity.dto.UserResponseDTO;
import com.nt.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
	
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	private final UserService authService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	
	
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) 
	{
        model.addAttribute("userObj", new User());
        
        model.addAttribute("isRegister", true);
        //returning the register page
        return "Register";
    }


	
	 @PostMapping(value = "/register")
	 public String registerNewUser(	Model model, @ModelAttribute("userObj") User user)
	 {
		 logger.info("Registering new user where user object is"+user.toString());
		 
		 authService.saveUser(user);
		 
		 return "/login";
	 }
	 
	 
	 @GetMapping("/login")
	    public String showLoginForm(Model model) {
	        model.addAttribute("userObj", new User());
	        //returning the login page
	        return "login";
	    }

	 

	 
	 
	 @PostMapping(value="/login", consumes= MediaType.APPLICATION_JSON_VALUE)
	 public String authentication(Model model, @ModelAttribute("userObj") User user )
	 {
		 
		 String authenticationResult = authService.authenticate(user);
		 
		 if(authenticationResult==null) {
			 logger.error("Invalid Credentials failed to login");
			 model.addAttribute("msg", "Invalid Credentials");
			 		 
			 return "login";
		 }
		 
		 
		 
		 logger.info("Log-in successfull The id= "+user.getId());
		 
//		 if(isAdmin) {
//			 return "/admin-home";
//		 }
		 
		 logger.info(authenticationResult+"!! now you are redirected to home page ");
		 return "/home";
		 
	 }
	 
	 @GetMapping("/home")
	 public String showHome(Model model) {
		 
		 User user = authService.getCurrentUserDetails();
		 
		 model.addAttribute("id", user.getId());
		 
		 boolean isAdmin = (user.getRole().equals("ADMIN")) ? true : false;
		 
		 model.addAttribute("isAdmin", isAdmin);
		 
		 logger.info("Navigating to home page. The id= "+user.getId()+", isAdmin= "+isAdmin);
		 
		 return "home";
	 }
	 
	 
	 
	/* 
	 	@GetMapping("/home")
	    public String showHome(Model model) {
		 
		 boolean isAdmin = authService.checkUserRole();
		 int id = authService.getUserId();
		 
		model.addAttribute("id", id);
		 
		 if(isAdmin) {
			 logger.info("Navigating user to admin-home page");
			 return "/admin-home";
		 }
		 logger.info("Navigating user to user-home page");
		 return "/user-home";
	        
	       
	  }
	 
	 @GetMapping("/admin-home")
	    public String showUserHome(Model model) {
	        
	        return "admin-home";
	  }
	 
*/
		/*
		 * @GetMapping("/user-home") public String showAdminHome(Model model) { int id =
		 * authService.getUserId(); logger.info("Navigating user to user-home page");
		 * model.addAttribute("id", "name"); return "user-home"; }
		 */


	 
	 
	 
	 
	  
	  
	 
	 /*
	  
	  //@PostMapping("/login")
	    public String login(Model model, @ModelAttribute("userObj") User user) {

		 logger.info("Start of the login method");
	        //Taking authentication token
	        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
	        try {
	            //Authenticating the user
	            Authentication authentication = authenticationManager.authenticate(token);
	            logger.info("authentication object is creadted");
	            //Condition to check user is authenticated or not
	            if (authentication.isAuthenticated()) {
	                System.out.println("Logged in successfully");
	                
	                //returning the dashboard page
	                logger.info("loggin successfull!! now you are redirected to home page");
	                return "/home";
	            }
	        } catch (Exception e) {
	            //If exception occurs
	        	logger.error("Invalid Login credentials!!. login fail");
	            System.out.println("Login failed: " + e.getMessage());
	        }
	        //If username or password is incorrect adding Invalid user (string) as a object in model class
	        model.addAttribute("msg", "Invalid Credentials");
	        //returning the login page
	        logger.info("loggin Fail!!. now you are redirected to login page");
	        return "login";
	    }
	 
	  
	  */
	 

}
