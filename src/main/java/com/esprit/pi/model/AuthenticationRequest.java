package com.esprit.pi.model;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
	 private String firstname;
	 private String password;
	public AuthenticationRequest(String firstname, String password) {
		super();
		this.firstname = firstname;
		this.password = password;
	}
	public AuthenticationRequest() {
		super();
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	 

}
