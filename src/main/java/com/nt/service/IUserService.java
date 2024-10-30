package com.nt.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nt.entity.User;
import com.nt.entity.dto.AuthenticationDTO;
import com.nt.entity.dto.UserResponseDTO;

public interface IUserService {
	
	public UserResponseDTO saveUser(User user);
	
	public String authenticate(User user);

	public Page<User> getAllUsers(Pageable pageable);

	public void dropUser(Integer id);
	
	Optional<User> getUserById(Integer id);
	
	public boolean isAuthenticated();

	public boolean checkUserRole();

	int getUserId();
	
	User getUserByName(String username);

	User getCurrentUserDetails();

	

}
