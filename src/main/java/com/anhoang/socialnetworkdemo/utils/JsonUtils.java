package com.anhoang.socialnetworkdemo.utils;

import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
	
//	public String objectToJson(Object obj) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writeValueAsString(obj);
//    }
//
//	public CheckoutServiceResponse checkoutServiceJsonToObject(Object obj){
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			String serviceJson = objectToJson(obj);
//			CheckoutServiceResponse serviceResponse = objectMapper.readValue(serviceJson, CheckoutServiceResponse.class);
//			return serviceResponse;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public CheckoutFeeResponse checkoutFeeJsonToObject(Object obj){
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			String feeJson = objectToJson(obj);
//			CheckoutFeeResponse feeResponse = objectMapper.readValue(feeJson, CheckoutFeeResponse.class);
//			return feeResponse;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public CheckoutTimeResponse checkoutTimeJsonToObject(Object obj){
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			String timeJson = objectToJson(obj);
//			CheckoutTimeResponse timeResponse = objectMapper.readValue(timeJson, CheckoutTimeResponse.class);
//			return timeResponse;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}
