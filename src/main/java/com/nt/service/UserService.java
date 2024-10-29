package com.nt.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nt.controller.AuthenticationController;
import com.nt.entity.User;
import com.nt.entity.dto.UserResponseDTO;
import com.nt.exception.UserNotFoundException;
import com.nt.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;

	@Override
	public UserResponseDTO saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		logger.info("new user password encoded successfully, for inserting new user record");
		user.setRole(user.getRole().toUpperCase());
		user =  userRepository.save(user);
		
		UserResponseDTO dto = new UserResponseDTO(
																		user.getId(),
																		user.getUsername(), 
																		user.getEmail(), 
																		user.getRole());
		
		return dto;
	}

	@Override
	public String authenticate(User dto) {
		
		logger.info("start to perform authentication operation"); 
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
		logger.info("token is generated for authentication process");
		 try {
			 
			 logger.info("Username ="+token.getPrincipal()+", password= "+token.getCredentials());
	            Authentication authentication = authenticationManager.authenticate(token);
	            if (authentication.isAuthenticated()) {
	                System.out.println("Logged in successfully");
	                logger.info("successfully login !!! You are a valid user");
	                return "Successfully login";
	            }
	            else {
	            	logger.error("Authentication failed");
	            }
	        } catch (Exception e) {
	            System.out.println("Login failed: " + e.getMessage());
	            logger.error("Invalid user credentials ");
	        }

		return null;
	}

	@Override
	public Page<User> getAllUsers(Pageable pageable) {
			
		return userRepository.findAll(pageable);
	}
	
	public boolean isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authentication != null && authentication.isAuthenticated() 
	           && !(authentication instanceof AnonymousAuthenticationToken);
	}

	@Override
	public void dropUser(Integer id) {
		User user = this.getUserById(id).orElseThrow(() -> new  UserNotFoundException("User not found "));
		userRepository.delete(user);
		
	}
	
	@Override
	public Optional<User> getUserById(Integer id) {
		return userRepository.findById(id);
	}

	
	
	
	@Override
	public boolean checkUserRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		for (GrantedAuthority authority : authentication.getAuthorities()) {
	        String role = authority.getAuthority();
	        logger.info("The role is "+role);
	        // Check if the user has a specific role
	        if (role.equals("ADMIN")) {
	        	 logger.info("Check role result is  admin");
	            return true;
	        }
	    }
        return false;
    }
	
	
	
	@Override
	public int getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		
		User userByName = this.getUserByName(name);
		logger.info("The User detials are "+userByName.toString());
		
        return userByName.getId();
    }

	@Override
	public User getUserByName(String username) {
		 return userRepository.findByUsername(username);
		
	}
	
	
	@Override
	public User getCurrentUserDetails() {
		int id = this.getUserId();
		
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException());
		
	}
	
	

}
