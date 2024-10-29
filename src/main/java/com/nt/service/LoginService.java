package com.nt.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nt.entity.User;
import com.nt.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService{
	
	private static Logger logger = LoggerFactory.getLogger(LoginService.class);
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Username posted by login form is "+username);
		if(username.isEmpty()) {
			logger.info("username is empty");
		}
		User user = userRepository.findByUsername(username);
		logger.info("User record is fetched");
        if (user == null) {
            System.out.println("User not found.! email is "+username);
            logger.info("User record is not available at Database");
            throw new UsernameNotFoundException(username);
        }
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(user.getRole())
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        logger.info("User record is fetched "+user.toString());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);

	}

}
