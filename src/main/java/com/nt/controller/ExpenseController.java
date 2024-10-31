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

import com.nt.entity.Expenses;
import com.nt.entity.User;
import com.nt.entity.dto.UserResponseDTO;
import com.nt.exception.UserNotFoundException;
import com.nt.service.IExpenseService;
import com.nt.service.IUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpenseController {
	
	
	private final IExpenseService expenseService;
	
//	private final IUserService userService;

	private static Logger logger = LoggerFactory.getLogger(ExpenseController.class);
	
	
	
	@GetMapping("/expenses")
	public String showExpenses(Model model) {
		// model.addAttribute("expenseObj", new Expenses());
		logger.info("The request is /expense - [api] | Navigating to expense.html ");
		
		int id = expenseService.getCurrentUserId();
		
		model.addAttribute("id", id);
		
		return "expense";
	}

	@GetMapping("/addExpense")
	public String addExpenses(Model model) 
	{
		model.addAttribute("expenseObj", new Expenses());
		
		logger.info("The request is /addExpense - [api] | Navigating to add-expense.html ");

		return "add-expense";
	}

	@PostMapping(value = "/addExpense")
	public String registerNewUser(Model model, @ModelAttribute("expenseObj") Expenses expense) 
	{

		logger.info("Posted Expense to add, where expense name= " + expense.getExpenseName());

		expenseService.addExpense(expense);
		
		int id = expenseService.getCurrentUserId();
		
		model.addAttribute("id", id);

		logger.info("The request is /addExpense - [api] | Navigation redired too expense.html ");

		return "redirect:/expenses";
	}

	@GetMapping("/all/expense")
	public String allExpense(
			Model model,
			@PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "expenseName", required = false) String expenseName
	){

		logger.info("The request is /all/expense - [api] | Navigating to all-expenses.html ");

		Page<Expenses> list = expenseService.getAllExpense(pageable);
		
		model.addAttribute("list", list.getContent());

		model.addAttribute("page", list);
		return "all-expenses";
	}

	

	@GetMapping("expense/edit")
	public String showEdit(@RequestParam("id") Integer id, Model model) {
		
		try {
			Expenses expense = expenseService.getExpenseById(id);
			
			logger.info("The expenses are "+expense.toString());	

			model.addAttribute("expenseObj", expense);
			
			model.addAttribute("isEdit", true);
			
			logger.info("From user/edit?id=(\"+id+\") [api] - Navigating to edit-expense/ add-expense.html ");

			return "add-expense";

		} catch (UserNotFoundException e) {
			model.addAttribute("message", e.getMessage());
			
			e.printStackTrace();
		}

		logger.info("From user/edit?id=(\"+id+\") [api] - Navigating to home.html ");
		return "home";
	}
	
	
	@GetMapping("/search/expense")
	public void showEdit(@RequestParam("name") String name, Model model) 
	{
		
		logger.info("Search by expense name= "+name);
	}
		
	
	/*
	 
	 @GetMapping("expense/delete")
	public String deleteUser(@RequestParam Integer id, Model model) {
		String msg = null;

		try {
			expenseService.dropExpense(id);
			msg = "Expense record deleted successfully";
			logger.info(msg);

		} catch (Exception e) {
			msg = e.getMessage();
			logger.info(msg);
		}

		model.addAttribute("message", msg);

		
		logger.info("From user/delete?id=("+id+") [api] - Navigating allExpenses - [api]");

		return "redirect:/all/expense";

	}
	 
	 
	 */

/*
	@PostMapping("expense/update")
	public String updateUser(Model model, @ModelAttribute User user) {

		UserResponseDTO saveUser = userService.saveUser(user);

		logger.info("user is updated successfully " + saveUser.toString());

		User currentUser = userService.getCurrentUserDetails();

		if (currentUser.getRole().equals("ADMIN")) {
			return "redirect:/allUsers";
		}

		return "redirect:/showUser?id=" + currentUser.getId();
	}
	
	*/

}
