package com.eauction.buyer.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BidModel {

	@NotBlank(message = "{productId.null.message}")
	private String productId;
	
	@NotBlank(message = "{bidAmount.null.message}")
	private String bidAmount;
	
	@NotBlank(message = "{buyerId.null.message}")
	private String buyerId;
}
