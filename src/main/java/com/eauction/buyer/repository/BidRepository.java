package com.eauction.buyer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.domain.Sort;
import com.eauction.buyer.entity.BidEntity;

import reactor.core.publisher.Flux;

public interface BidRepository extends ReactiveMongoRepository<BidEntity, String> {

	Flux<BidEntity> findAllByProductId(String productId, Sort sort);
}