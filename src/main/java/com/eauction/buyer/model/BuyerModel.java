package com.eauction.buyer.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BuyerModel {

	private String buyerId;
	
	@NotBlank(message = "{emailId.null.message}")
	@Email(message = "{emailId.not.valid}")
	private String emailId;
	
	@NotBlank(message = "{firstName.null.message}")
	@Size(min = 5, max = 30, message = "{firstName.invalid.message}")
	private String firstName;
	
	@NotBlank(message = "{lastName.null.message}")
	@Size(min = 3, max = 25, message = "{lastName.invalid.message}")
	private String lastName;
	
	@NotBlank(message = "{password.null.message}")
	private String password;
	
	private String address;
	private String city;
	private String state;
	private String pincode;
	
	@NotBlank(message = "{mobileNo.null.message}")
	@Digits(message = "{mobileNo.invalid.message}", fraction = 0, integer = 10)
	private String mobileNo;
	
	private String jwt;
}