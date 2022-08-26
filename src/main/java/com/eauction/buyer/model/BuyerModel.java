package com.eauction.buyer.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BuyerModel {

	private String buyerId;
	
	@NotBlank(message = "{emailId.null.message}")
	@Email(message = "{emailId.not.valid}")
	private String emailId;
	
	@NotBlank(message = "{firstName.null.message}")
	@Min(value = 5, message = "{firstName.less.message}")
    @Max(value = 30, message = "{firstName.greater.message}")
	private String firstName;
	
	@NotBlank(message = "{lastName.null.message}")
	@Min(value = 3, message = "{lastName.less.message}")
    @Max(value = 25, message = "{lastName.greater.message}")
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