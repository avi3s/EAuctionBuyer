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
import com.eauction.buyer.helper.LoginException;
import com.eauction.buyer.helper.Util;
import com.eauction.buyer.model.BuyerModel;
import com.eauction.buyer.model.LoginModel;
import com.eauction.buyer.repository.BuyerRepository;
import com.eauction.buyer.service.LoginService;

import reactor.core.publisher.Mono;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private Validator validator;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
    private Util util;
	
	@Autowired
	@Lazy
	private ModelMapper modelMapper;
	
	@Value("${invalid.user}")
	private String invalidUser;
	
	@Override
	public Mono<ServerResponse> login(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(LoginModel.class)
			   .doOnNext(this::validate)
			   .flatMap(buyer -> transform(buyerRepository.findBuyerByEmailIdAndPassword(buyer.getEmailId(), buyer.getPassword()), BuyerModel.class))
			   .flatMap(buyerModel ->  ServerResponse.status(HttpStatus.OK).bodyValue(buyerModel))
			   .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(invalidUser));
	}
	
	private void validate(LoginModel loginModel) {

	       var constraintViolations = validator.validate(loginModel);
	       if (constraintViolations.size() > 0) {
	           var errorMessage = constraintViolations.stream()
	                   .map(ConstraintViolation::getMessage)
	                   .sorted()
	                   .collect(Collectors.joining(", "));
	           throw new LoginException(errorMessage);
	       } else {
	    	   util.printLog(loginModel, "Login Incoming Request");
	       }
	}
	
	private Mono<BuyerModel> transform(Mono<BuyerEntity> from, Class<BuyerModel> valueType) {

		return from.flatMap(be -> {
								util.printLog(be, "Coming From Database");
								if (Objects.nonNull(be)) {
									BuyerModel buyerModel = modelMapper.map(be, valueType);
									buyerModel.setJwt(util.createJwt(buyerModel));
									return Mono.just(buyerModel);
								} else {
									return null;
								}
							});
	}
}