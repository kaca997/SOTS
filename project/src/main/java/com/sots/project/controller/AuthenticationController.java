package com.sots.project.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sots.project.model.User;
import com.sots.project.security.JwtAuthenticationRequest;
import com.sots.project.security.TokenUtils;
import com.sots.project.service.AuthenticationService;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	TokenUtils tokenUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthenticationService userDetailsService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws AuthenticationException, IOException {

		final Authentication authentication;
		try {
			this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		}
		catch(UsernameNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
		}
		catch(BadCredentialsException e) {
			return new ResponseEntity<String>("Wrong password, please try again.",HttpStatus.NOT_ACCEPTABLE);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthorities().get(0).getUserType());
		System.out.println(user);
		return new ResponseEntity<String>(jwt, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/studentTest", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	public ResponseEntity<String> testS(){
		return new ResponseEntity<>("Student", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/teacherTest", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	public ResponseEntity<String> testT(){
		return new ResponseEntity<>("Teacher", HttpStatus.OK);
	}
	
	@GetMapping( value = "/logout")
    public ResponseEntity<String> logoutUser() {
        	SecurityContextHolder.clearContext();

            return new ResponseEntity<>("You successfully logged out!", HttpStatus.OK);

    }
}