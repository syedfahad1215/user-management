package com.nt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nt.entity.Expenses;

public interface IExpenseService {
	
	Expenses addExpense(Expenses expense);

	Page<Expenses> getAllExpense(Pageable pageable);

	boolean isAdmin();

	Expenses getExpenseById(Integer id);

	int getCurrentUserId();
	
//	void dropExpense(Integer id);

}
