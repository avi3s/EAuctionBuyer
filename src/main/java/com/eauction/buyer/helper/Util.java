package com.eauction.buyer.helper;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.eauction.buyer.model.BuyerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Component
public class Util {

	private static final Logger LOGGER = LogManager.getLogger(Util.class);

	@Autowired
	@Lazy
	private ModelMapper modelMapper;
	
	@Value("${privateKey}")
	private String privateKey;
	
	@Value("${publicKey}")
	private String publicKey;
	
	@Value("${timeout}")
	private String timeout;
	
//	public <T> T transform(Object from, Class<T> valueType) {
//
//		if (Objects.nonNull(from)) {
//			return modelMapper.map(from, valueType);
//		} else {
//			return null;
//		}
//	}
	
	public String createJwt(BuyerModel buyerModel) {

		LOGGER.info("createJWT Start");
	    String jwtToken = null;
		try {
			PrivateKey privateKey = getPrivateKey();
			Instant now = Instant.now();
		    jwtToken = Jwts.builder()
		            		.claim("name", buyerModel.getFirstName() + " " + buyerModel.getLastName())
		            		.claim("emailId", buyerModel.getEmailId())
		            		.setSubject("login")
		            		.setId(buyerModel.getBuyerId() + "-" +UUID.randomUUID().toString())
		            		.setIssuedAt(Date.from(now))
		            		.setExpiration(Date.from(now.plus(Long.valueOf(timeout), ChronoUnit.MINUTES)))
		            		.signWith(privateKey)
		            		.compressWith(CompressionCodecs.DEFLATE)
		            		.compact();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			exceptionToString(e);
		}
		
		LOGGER.info("createJWT End ==>> {}", jwtToken);
	    return jwtToken;
	}

	private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

	    String rsaPrivateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    PrivateKey privateKey = kf.generatePrivate(keySpec);
	    return privateKey;
	}
	
	private Jws<Claims> parseJwt(String jwtString) throws InvalidKeySpecException, NoSuchAlgorithmException {

		LOGGER.info("parseJwt Start ==>> {}", jwtString);
	    PublicKey publicKey = getPublicKey();

	    Jws<Claims> jwt = Jwts.parserBuilder()
	            			  .setSigningKey(publicKey)
	            			  .build()
	            			  .parseClaimsJws(jwtString);
	    LOGGER.info("parseJwt End ==>> {}", jwt);
	    return jwt;
	}

	private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		
	    String rsaPublicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
	    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    PublicKey publicKey = kf.generatePublic(keySpec);
	    return publicKey;
	}

	public String getUserIdFromToken(String jwt) {
		
		String buyerId = null;
		try {
			jwt = jwt.replace("Bearer ", "");
			buyerId = parseJwt(jwt).getBody().getId().split("-")[0];
		} catch (Exception e) {
			exceptionToString(e);
			buyerId = null;
		}
		
		LOGGER.info("getUserIdFromToken == {}", buyerId);
		return buyerId;
	}
	
	public static String getCurrentDateTime(LocalDateTime now) {
		
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formatDateTime = now.format(format);  
        return formatDateTime;
	}
	
	public void printLog(Object object, String message) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			LOGGER.info(message + " -- " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
		} catch (Exception e) {
			exceptionToString(e);
		}
	}

	public void exceptionToString(Exception e) {
		System.err.println(ExceptionUtils.getStackTrace(e));
	}
}