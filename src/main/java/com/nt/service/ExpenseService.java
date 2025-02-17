package com.nt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nt.controller.ExpenseController;
import com.nt.entity.Expenses;
import com.nt.entity.User;
import com.nt.exception.UserNotFoundException;
import com.nt.repo.ExpensesRepository;
import com.nt.repo.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService implements IExpenseService {

	private final ExpensesRepository expenseRepository;
	
	private final IUserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(ExpenseController.class);
	
	@Override
	public Expenses addExpense(Expenses expense) {
		
		
		User user = userService.getCurrentUserDetails();
		
		logger.info("The user "+user.getUsername()+" is trying to add expense");
		
		expense.setUser(user);
		
		logger.info("The user "+user.getUsername()+" is set to expense obj");
		
		Expenses savedExpense = expenseRepository.save(expense);
		
		logger.info("The user "+user.getUsername()+" expense is added");
		
		return savedExpense;
	}

	@Override
	public Page<Expenses> getAllExpense(Pageable pageable) 
	{
		User user = userService.getCurrentUserDetails();
		
		if(! user.getRole().equals("ADMIN")) {
			return expenseRepository.findByUser(user, pageable);
		}
		
		return expenseRepository.findAll(pageable);
	}
	
	
	@Override
	public boolean isAdmin() {
		return userService.checkUserRole();
	}

	@Override
	public Expenses getExpenseById(Integer id) {
		
		return expenseRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException());
	}

	@Override
	public int getCurrentUserId() {
		return userService.getUserId();
	}
	
/*
	@Override
	@Transactional
	public void dropExpense(Integer id) {
		logger.info("The expense who's id= "+id+" Is going to be deleted");
		
		
		User user = userService.getCurrentUserDetails();
		
//		boolean removeIf = user.getExpenses().removeIf( exp -> exp.equals(id));
		
		user.getExpenses().remove(expenseRepository.findById(id));
		
		userService.saveUser(user);
		
		boolean contains = user.getExpenses().contains(expenseRepository.findById(id));
		
		
		
		
		logger.info("User contains expense "+ contains);
		
	}

*/
	
	
	
}
