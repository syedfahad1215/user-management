package com.nt.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nt.entity.User;
import com.nt.entity.dto.UserResponseDTO;
import com.nt.exception.UserNotFoundException;
import com.nt.service.IUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final IUserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	@GetMapping("/showUser")
	public String showUser(
			Model model,
			@RequestParam("id") Integer id
	){
		
		Optional<User> userById = userService.getUserById(id);
		
		User user = userById.orElseThrow(() -> new UserNotFoundException());
		
		model.addAttribute("user", user);
		
		return "show-user";
	}
	
	
	@GetMapping("/allUsers")
	public String showAllUsers(Model model,
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "username", required = false) String username
    ) {
		Page<User> page = userService.getAllUsers(pageable);
		
		model.addAttribute("list", page.getContent());
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		
		logger.info("From /allUser [api] - Navigating to all-user.html page");
		
		return "all-users";
	}
	
	@GetMapping("user/delete")
	public String deleteUser(
			@RequestParam Integer id,
			Model model
	){
		String msg = null;
		
		try {
			userService.dropUser(id);
			msg = "User record deleted successfully";
			logger.info(msg);
			
		}catch(Exception e) {
			msg = e.getMessage();
			logger.info(msg);
		}
		
		model.addAttribute("message", msg);
		
		boolean isAdmin = userService.checkUserRole();
		 
		if(isAdmin) {
			logger.info("From user/delete?id=("+id+") [api] - Navigating to /allUsers - api");
			return "redirect:/allUsers";
		}
		
		logger.info("From user/delete?id=(\"+id+\") [api] - Navigating to login as user record is deleted page");
		
		return "login";
		
		
	}

	@GetMapping("user/edit")
	public String showEdit(
			@RequestParam("id") Integer id,
			Model model) {
		String page = null;
		try {
			Optional<User> dbUser = userService.getUserById(id);
			
			User user = dbUser.orElseThrow(() -> new UserNotFoundException("User not found"));
			
			model.addAttribute("userObj", user);
			
			page = "edit-user";
			
		} catch (UserNotFoundException e) {
			model.addAttribute("message", e.getMessage());
			page = "home";
			e.printStackTrace();
		}
		
		logger.info("From user/edit?id=(\"+id+\") [api] - Navigating to "+page+".html ");
		return page;
	}
	
	
	@PostMapping("/update")
	public String updateUser(
					Model model,
					@ModelAttribute User user
	){
		
		UserResponseDTO saveUser = userService.saveUser(user);
		
		logger.info("user is updated successfully "+saveUser.toString());
		
		User currentUser = userService.getCurrentUserDetails();
		
		if(currentUser.getRole().equals("ADMIN")) {
			return "redirect:/allUsers";
		}
		
		return "redirect:/showUser?id="+currentUser.getId();
	}
}
