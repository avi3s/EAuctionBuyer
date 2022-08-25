package com.eauction.buyer.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.eauction.buyer.entity.BidEntity;
import com.eauction.buyer.helper.BidException;
import com.eauction.buyer.helper.Util;
import com.eauction.buyer.model.BidModel;
import com.eauction.buyer.repository.BidRepository;
import com.eauction.buyer.service.AddBidService;
import com.eauction.buyer.service.UpdateBidService;

import reactor.core.publisher.Mono;

@Service
public class BidServiceImpl implements AddBidService, UpdateBidService {

	@Autowired
	private Validator validator;
	
	@Autowired
    private Util util;
	
	@Autowired
	private BidRepository bidRepository;
	
	@Autowired
	@Lazy
	private ModelMapper modelMapper;
	
	@Value("${datetime.format}")
	private String datetimeFormat;
	
	@Value("${less.bid.amount}")
	private String lessBidAmount;
	
	@Value("${no.bid.amount}")
	private String noBidAmount;
	
	@Override
	public Mono<ServerResponse> addBid(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BidModel.class)
                .doOnNext(this::validate)
                .flatMap(bidModel -> bidRepository.save(transform(bidModel, BidEntity.class))
                .flatMap(bidEntity -> ServerResponse.status(HttpStatus.CREATED).bodyValue(bidEntity)));
	}
	
	@Override
	public Mono<ServerResponse> updateBid(ServerRequest serverRequest) {
		
		var productId = serverRequest.pathVariable("productId");
		var emailId = serverRequest.pathVariable("emailId");
		var newBidAmount = serverRequest.pathVariable("newBidAmount");
		return bidRepository.findAllByProductId(productId, Sort.by(Sort.Direction.DESC, "bidAmount")).collectList()
						   .flatMap(eb -> {
					               if(eb.isEmpty()) {
					            	   throw new BidException(noBidAmount);
					               } else {
					            	   if (Objects.nonNull(eb.get(0).getBidAmount()) &&  eb.get(0).getBidAmount() < Double.valueOf(newBidAmount)) {
					       				BidEntity bidEntity = new BidEntity();
					       				bidEntity.setBidAmount(Double.valueOf(newBidAmount));
					       				bidEntity.setBidDateTime(LocalDateTime.now());
					       				bidEntity.setBuyerId(util.getUserIdFromToken(serverRequest.headers().firstHeader("Authorization")));
					       				bidEntity.setProductId(productId);
					       				return Mono.just(bidEntity);
					       			} else {
					       				throw new BidException(lessBidAmount);
					       			}
					               }
					           })
						   .flatMap(bidRepository::save)
						   .flatMap(savedbidEntity -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedbidEntity))
						   .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(noBidAmount));
	}
	
	private void validate(BidModel bidModel) {

	       var constraintViolations = validator.validate(bidModel);
	       if (constraintViolations.size() > 0) {
	           var errorMessage = constraintViolations.stream()
	                   .map(ConstraintViolation::getMessage)
	                   .sorted()
	                   .collect(Collectors.joining(", "));
	           throw new BidException(errorMessage);
	       }
	}
	
	private BidEntity transform(BidModel from, Class<BidEntity> valueType) {
		
		if (Objects.nonNull(from)) {
			BidEntity bidEntity = modelMapper.map(from, valueType);
			bidEntity.setBidDateTime(LocalDateTime.now());
			return bidEntity;
		} else {
			return null;
		}
	}
}