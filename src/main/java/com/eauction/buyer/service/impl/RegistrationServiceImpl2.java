package com.eauction.buyer.service.impl;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.eauction.buyer.entity.BuyerEntity;
import com.eauction.buyer.helper.RegistrationException;
import com.eauction.buyer.helper.Util;
import com.eauction.buyer.model.BuyerModel;
import com.eauction.buyer.repository.BuyerRepository;
import com.eauction.buyer.service.RegistrationService;

import reactor.core.publisher.Mono;

@Service
public class RegistrationServiceImpl2 implements RegistrationService {

	@Autowired
	private Validator validator;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
    private Util util;
	
	@Autowired
	@Lazy
	private ModelMapper modelMapper;
	
	@Value("${duplicate.user}")
	private String duplicateUser;
	
	@Override
	public Mono<ServerResponse> registerBuyer(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BuyerModel.class)
                .doOnNext(this::validate)
                .doOnNext(this::validateAlreadyPresent)
                .flatMap(bidModel -> buyerRepository.save(transform(bidModel, BuyerEntity.class)))
                .flatMap(buyerEntity -> ServerResponse.status(HttpStatus.CREATED).bodyValue(buyerEntity));
	}
	
	private BuyerEntity transform(BuyerModel buyerModel, Class<BuyerEntity> valueType) {
		
		if (Objects.nonNull(buyerModel)) {
			BuyerEntity buyerEntity = modelMapper.map(buyerModel, valueType);
			return buyerEntity;
		} else {
			return null;
		}
	}

	private void validate(BuyerModel buyerModel) {

       var constraintViolations = validator.validate(buyerModel);
       if (constraintViolations.size() > 0) {
           var errorMessage = constraintViolations.stream()
                   .map(ConstraintViolation::getMessage)
                   .sorted()
                   .collect(Collectors.joining(", "));
           throw new RegistrationException(errorMessage);
       } else {
    	   util.printLog(buyerModel, "Incoming Request");
       }
	}
	
	private void validateAlreadyPresent(BuyerModel buyerModel) {
	   var buyer = buyerRepository.findBuyerByEmailId(buyerModel.getEmailId());
  	   buyer.flatMap(bm ->  {
  		   if(Objects.nonNull(bm)) {
  			   throw new RegistrationException(duplicateUser);
  		   } else {
  			   return Mono.just(buyerModel);
  		   }
  	   });
	}
}