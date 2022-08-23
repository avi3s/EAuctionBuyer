package com.eauction.buyer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "buyer_details")
public class BuyerEntity {

	@Id
    private String buyerId;
    private String emailId;
    private String password;
}