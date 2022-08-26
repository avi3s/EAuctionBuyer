package com.eauction.buyer.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.eauction.buyer.model.BidModel;
import com.eauction.buyer.model.LoginModel;
import com.eauction.buyer.service.AddBidService;
import com.eauction.buyer.service.LoginService;
import com.eauction.buyer.service.RegistrationService;
import com.eauction.buyer.service.UpdateBidService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class BuyerRouter implements WebFluxConfigurer {

	 @Bean
	 @RouterOperations({ 
		 // For Buyer's Registration
		 @RouterOperation(path = "/api/v1/buyer/registration", 
				 produces = {MediaType.APPLICATION_JSON_VALUE}, 
				 consumes = {MediaType.APPLICATION_JSON_VALUE},
				 method = RequestMethod.POST, 
				 beanClass = RegistrationService.class, 
				 beanMethod = "registerBuyer",
                 operation = @Operation(operationId = "registration",
                 responses = {@ApiResponse(responseCode = "200", description = "Registration Successful", content = @Content(schema = @Schema(implementation = String.class))),
               		 	  @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = String.class)))},
                 requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = LoginModel.class))))),
	 })
     public RouterFunction<ServerResponse> loginRoute(RegistrationService registrationService) {
        return route()
               .nest(path("/api/v1/buyer/registration"), builder ->
                       builder
                               .POST("", RequestPredicates.accept(MediaType.APPLICATION_JSON), registrationService::registerBuyer))
               				.build();
	 }
	 
	 @Bean
	 @RouterOperations({ 
		 // For Buyer's Login
		 @RouterOperation(path = "/api/v1/buyer/login", 
		   	     produces = {MediaType.APPLICATION_JSON_VALUE}, 
				 consumes = {MediaType.APPLICATION_JSON_VALUE},
				 method = RequestMethod.POST, 
				 beanClass = LoginService.class, 
				 beanMethod = "login",
                 operation = @Operation(operationId = "login",
                 responses = {@ApiResponse(responseCode = "200", description = "Login Successful", content = @Content(schema = @Schema(implementation = String.class))),
                		 	  @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = String.class)))},
                 requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = LoginModel.class))))),
	 })
     public RouterFunction<ServerResponse> loginRoute(LoginService loginService) {
        return route()
                .nest(path("/api/v1/buyer/login"), builder ->
                        builder
                                .POST("", RequestPredicates.accept(MediaType.APPLICATION_JSON), loginService::login))
                				.build();
	 }
	 
	 @Bean
	 @RouterOperations({ 
		// For Placing Bids
		@RouterOperation(path = "/api/v1/buyer/place-bid", 
				 produces = {MediaType.APPLICATION_JSON_VALUE}, 
				 consumes = {MediaType.APPLICATION_JSON_VALUE},
				 method = RequestMethod.POST, 
				 beanClass = AddBidService.class, 
				 beanMethod = "addBid",
			     operation = @Operation(operationId = "addBid",
			     responses = {@ApiResponse(responseCode = "201", description = "Bid Placed Successfully", content = @Content(schema = @Schema(implementation = String.class))),
			     		 	  @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = String.class)))},
			     requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = BidModel.class))))),
	 })
	 public RouterFunction<ServerResponse> addBidRoute(AddBidService addBidService) {
	    return route()
	                .nest(path("/api/v1/buyer/place-bid"), builder ->
	                        builder
	                                .POST("", RequestPredicates.accept(MediaType.APPLICATION_JSON), addBidService::addBid))
	                				.build();
		}
	 
	 @Bean
	 @RouterOperations({ 
		// For Updating Bids
		@RouterOperation(path = "/api/v1/buyer/update-bid/{productId}/{emailId}/{newBidAmount}",
				 produces = {MediaType.APPLICATION_JSON_VALUE}, 
				 method = RequestMethod.PUT, 
				 beanClass = UpdateBidService.class, 
				 beanMethod = "updateBid",
	             operation = @Operation(operationId = "updateBid",
	             responses = {@ApiResponse(responseCode = "200", description = "Bids Updated Successfully", content = @Content(schema = @Schema(implementation = String.class))),
	   				  		  @ApiResponse(responseCode = "404", description = "Product not found by given Id")},
	             parameters = {@Parameter(in = ParameterIn.PATH, name = "productId"),
	               		       @Parameter(in = ParameterIn.PATH, name = "emailId"),
	               		       @Parameter(in = ParameterIn.PATH, name = "newBidAmount") })),
	 })
	 public RouterFunction<ServerResponse> updateBidRoute(UpdateBidService updateBidService) {
	    return route()
	                .nest(path("/api/v1/buyer/update-bid"), builder ->
	                        builder
	                                .PUT("/{productId}/{emailId}/{newBidAmount}", updateBidService::updateBid))
	                				.build();
		}
}