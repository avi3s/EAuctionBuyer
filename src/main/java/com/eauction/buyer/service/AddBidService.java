package com.eauction.buyer.service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface AddBidService {

	Mono<ServerResponse> addBid(ServerRequest serverRequest);
}
