package com.nt.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.entity.Expenses;
import com.nt.entity.User;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
	
	public Page<Expenses> findByUser(User user, Pageable pageable);


}
