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
@Document(collection = "product_details")
public class ProductEntity {

	@Id
    private String productId;
	private String productCategoryId;
	private String productCategoryName;
	private String sellerId;
	//private List<BidEntity> bidEntities;
    private String productName;
    private String shortDescription;
    private String detailedDescription;
    private Double startingPrice;
    private LocalDateTime bidEndDateTime;
}