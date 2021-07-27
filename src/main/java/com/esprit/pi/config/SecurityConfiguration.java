package com.esprit.pi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.esprit.pi.filters.JwtRequestFilter;

@EnableWebSecurity
@Configuration
@ComponentScan
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}


	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf()
				.disable()
				.authorizeRequests()
////				.antMatchers("/admin").hasRole("ADMIN")
//				.antMatchers("/user").hasRole("USER")
//				.antMatchers("/createadmin").hasRole("ADMIN")
				.antMatchers("/user/Adminput/{id}").anonymous()
				.antMatchers("/createuser").anonymous()
				.antMatchers("/createadmin").anonymous()
				.antMatchers("/authenticate").anonymous()
				.antMatchers("/getall").anonymous()
				.antMatchers("/user/Usersput/{id}").anonymous()
				.antMatchers("/user/Usersdel/{id}").anonymous()
				.anyRequest().authenticated()
				.and()
				.cors()
				.and()
				.exceptionHandling()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}
//	------------------------autre facon de configuration sp security--------------------------
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		// TODO Auto-generated method stub
//		auth.userDetailsService(userDetailsService);
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.antMatchers("/admin").hasRole("ADMIN")
//			.antMatchers("/user").hasAnyRole("ADMIN","USER")
//			.antMatchers("/").permitAll()
//			.and().formLogin();
//	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}
