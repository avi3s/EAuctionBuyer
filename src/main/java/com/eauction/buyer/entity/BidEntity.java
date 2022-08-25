package com.eauction.buyer.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bid_details")
public class BidEntity {

	@Id
    private String bidId;
    private String productId;
	private String buyerId;
    private Double bidAmount;
    private LocalDateTime bidDateTime;
}