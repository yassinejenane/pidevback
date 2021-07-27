package com.esprit.pi.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.esprit.pi.model.User;
import com.esprit.pi.repository.UserRepository;
import com.esprit.pi.service.MyUserDetails;

@Service
public class MyuserDetailsService implements UserDetailsService{
	@Autowired
	UserRepository userRepository ;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String firstname) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> user =  userRepository.findByfirstname(firstname) ;
		
		user.orElseThrow(()-> new UsernameNotFoundException("not found username"));
		
		return user.map(MyUserDetails::new).get();
		}
	public User signUpUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		//String encoder = bCryptPasswordEncoder.encode(user.getPassword());
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
		// set default active statue user
		user.setRoles("ROLE_USER");
		user.setActive(true);
		final User createdUser = userRepository.save(user);
		return createdUser;
//		final ConfirmationToken confirmationToken = new ConfirmationToken(user);
//
//		sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());

	}
	

	public User signUpUserAdmin(User user) {

		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// set default active statue user
		user.setRoles("ROLE_ADMIN");
		user.setActive(true);
		final User createdUser = userRepository.save(user);
		return createdUser;


	}
	
	public List<User> getAllUsers() {
		
		return  userRepository.findAll();
	}
}

