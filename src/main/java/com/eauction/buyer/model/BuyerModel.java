package com.eauction.buyer.model;

import lombok.Data;

@Data
public class BuyerModel {

	private String buyerId;
	private String emailId;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String pincode;
	private String mobileNo;
	private String jwt;
}