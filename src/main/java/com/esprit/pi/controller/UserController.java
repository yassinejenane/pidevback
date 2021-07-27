package com.esprit.pi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.esprit.pi.config.MyuserDetailsService;
import com.esprit.pi.model.AuthenticationRequest;
import com.esprit.pi.model.User;
import com.esprit.pi.repository.UserRepository;
import com.esprit.pi.util.JwtUtil;
@RestController
@CrossOrigin(origins="http://localhost:4200" , maxAge=3600)
//@RequestMapping("/hello")
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyuserDetailsService userDetailsService;
	@Autowired
	public UserRepository userRepo;
	@GetMapping("/")
	public String home() {
		return ("<h1>welcome</h1>");	
	}
	@GetMapping("/user")
	public String user() {
		
		return ("<h1>welcome user </h1>");	
	}
	@GetMapping("/admin")
	public String admin() {
		return ("<h1>welcomeadmin </h1>");	
	}
	
	// Create user with admin role function (Only by admin profile) 
	@PreAuthorize("hasRole('ADMIN')") 
	@PostMapping("/createadmin")
	public User createUserAdmin( @RequestHeader(value = "Authorization", required=false) String Jwt,
			@Valid @RequestBody User user) throws Exception {
		Optional<User> isexistUser = null;
		isexistUser = userRepo.findByfirstname(user.getFirstname());
		//String role = "ROLE_ADMIN" ;
		//System.out.println("rooooooooole: "+ user.getRoles());
		if(isexistUser.isPresent()) {
			throw new Exception("Admin with this Username already exist");
			
		}else  {
//			if(isexistUser.get().getRole().equals("USER_ROLE"))
				return userDetailsService.signUpUserAdmin(user);				
		}		 
	}
	
	// Create user function
	@PreAuthorize("permitAll()")  
	@PostMapping("/createuser")
	public User createUser(@Valid @RequestBody User user ) throws Exception {
		Optional<User> isexistUser = null;
		isexistUser = userRepo.findByfirstname(user.getFirstname());
		if(isexistUser.isPresent()) {
			throw new Exception("User with this Username already exist");

		}else {
//			if(isexistUser.get().getRole().equals("USER_ROLE"))
				return userDetailsService.signUpUser(user);
//			
		} 
	}
	// Update user function
	//@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@PreAuthorize("permitAll()")
	@PutMapping("/user/Usersput/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long  UserId, @RequestHeader(value = "Authorization" , required=false) String Jwt,
			@Valid @RequestBody User UserDetails) throws Exception {
		User user = userRepo.findById(UserId)
				.orElseThrow(() -> new Exception("User not found with this id :: " + UserId));
		//user.setId(UserDetails.getId());
		//Optional<User> isexistUser = null;
		user.setPassword(UserDetails.getPassword());
		user.setFirstname(UserDetails.getFirstname());
		user.setLastname(UserDetails.getLastname());
		user.setName(UserDetails.getName());
		user.setMail(UserDetails.getMail());
		user.setPhone(UserDetails.getPhone());
		user.setAddress(UserDetails.getAddress());
		//isexistUser = userRepo.findByfirstname(user.getFirstname());
		//if(isexistUser.isPresent()) {
			//throw new Exception("User with this Username already exist");

		//}else {
//			if(isexistUser.get().getRole().equals("USER_ROLE"))
				//return userDetailsService.signUpUser(user);
				final User updatedUser = userDetailsService.signUpUser(user);
				return ResponseEntity.ok(updatedUser);
				//}
	}
	
	// Update user with Admin role function
		@PreAuthorize("hasRole('ADMIN')")
		@PutMapping("/user/Adminput/{id}")
		public ResponseEntity<User> updateUserAdmin(@PathVariable(value = "id") Long  UserId, @RequestHeader(value = "Authorization", required=false) String Jwt,
				@Valid @RequestBody User UserDetails) throws Exception {
			User user = userRepo.findById(UserId)
					.orElseThrow(() -> new Exception("User Admin not found with this id :: " + UserId));
			//user.setId(UserDetails.getId());
			user.setPassword(UserDetails.getPassword());
			user.setFirstname(UserDetails.getFirstname());
			user.setLastname(UserDetails.getLastname());
			user.setName(UserDetails.getName());
			user.setMail(UserDetails.getMail());
			user.setPhone(UserDetails.getPhone());
			user.setAddress(UserDetails.getAddress());
			final User updatedUser = userDetailsService.signUpUserAdmin(user);
			return ResponseEntity.ok(updatedUser);
		}
		
		// Getall users function
		@PreAuthorize("hasRole('ADMIN')") 
		@RequestMapping(value = "/getall", method = RequestMethod.GET)
		public List<User> listusers(@RequestHeader(value = "Authorization", required=false) String Jwt) {
			//ModelAndView model = new ModelAndView();
			List<User> listusers = userDetailsService.getAllUsers();
			List <User> response = listusers;
			
			return listusers;
		}
	
	// Delete user function
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/user/Usersdel/{id}")
	public Map<String, Boolean> deleteUsers(@PathVariable(value = "id") Long  UserId, @RequestHeader(value = "Authorization", required=false) String Jwt)
			throws Exception {
		User user = userRepo.findById(UserId)
				.orElseThrow(() -> new Exception("Username  not found for this id :: "  + UserId));

		userRepo.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("User " +user.getFirstname() +" with id ="+UserId+" deleted", Boolean.TRUE);
		return response;
	}
	
	
    @PreAuthorize("permitAll()")  
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public Map<String, String> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getFirstname(), authenticationRequest.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getFirstname());
		Optional <User> user = userRepo.findByfirstname(authenticationRequest.getFirstname());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		Map<String, String> response = new HashMap<>();
		//response.put("Username", (userDetails.getUsername()));
		response.put("User ", (userDetails.getUsername()) );
		response.put("id", Long.toString( user.get().getId()));
		response.put("Authorization", "Bearer "+jwt);
		response.put("role",  user.get().getRoles());
		response.put("lastname",  user.get().getLastname());
		response.put("mail",  user.get().getMail());
		response.put("name",  user.get().getName());
		response.put("phone",  user.get().getPhone());
		response.put("address",  user.get().getAddress());

		return response;
		
	}
}
