package com.eauction.buyer.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginModel {

	@NotBlank(message = "{emailId.null.message}")
	private String emailId;
	
	@NotBlank(message = "{password.null.message}")
	private String password;
}