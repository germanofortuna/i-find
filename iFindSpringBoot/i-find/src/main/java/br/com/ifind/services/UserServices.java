package br.com.ifind.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.ifind.repositories.UserRepository;

@Service
public class UserServices implements UserDetailsService{
	
	private Logger logger = Logger.getLogger(UserServices.class.getName());
	
	@Autowired
	UserRepository repository;
	
	public UserServices(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by name " + username + "!");
		var user = repository.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return user;
	}
}
