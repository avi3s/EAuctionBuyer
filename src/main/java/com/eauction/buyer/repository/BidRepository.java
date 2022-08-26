package com.eauction.buyer.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.eauction.buyer.entity.BidEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BidRepository extends ReactiveMongoRepository<BidEntity, String> {

	Flux<BidEntity> findAllByProductId(String productId, Sort sort);
	
	Mono<BidEntity> findByProductIdAndBuyerId(String productId, String buyerId);
}