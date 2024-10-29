package com.nt.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nt.service.LoginService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
	private final LoginService loginService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		logger.info("Start of security filter chain bean");
		return http
				
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(request -> request
						.requestMatchers(HttpMethod.POST, "/register").permitAll()
						.requestMatchers(HttpMethod.POST, "/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/register").permitAll()
						.requestMatchers(HttpMethod.GET, "/login").permitAll()
						.anyRequest().authenticated()

				)
				.formLogin( form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/home", true) // Redirect to /dashboard after successful login
                        .permitAll()
				)
				.httpBasic(Customizer.withDefaults())
				.logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                        .permitAll()
                )

				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		logger.info("AuthenticationManager object is creating");
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		logger.info("Start of AuthenticationProvider bean");
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		logger.info("AuthenticationProvider object is created ");
		authProvider.setUserDetailsService(loginService);
		authProvider.setPasswordEncoder(passwordEncoder());
		logger.info("UserDetailsService, PasswordEncoder is set for AuthenticationProvider object");
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		logger.info("BCryptPasswordEncoder object is creating");
		return new BCryptPasswordEncoder();
	}

}
