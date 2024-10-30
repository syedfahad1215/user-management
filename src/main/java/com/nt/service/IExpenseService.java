package com.nt.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nt.entity.Expenses;
import com.nt.entity.User;

public interface IExpenseService {
	
	Expenses addExpense(Expenses expense);

	Page<Expenses> getAllExpense(Pageable pageable);

	void dropExpense(Integer id);

	boolean isAdmin();

	User getUserById(Integer id);

	int getCurrentUserId();

}
