package com.barath.app;

import ai.api.model.AIResponse;
import com.barath.app.model.InventoryDTO;
import com.barath.app.model.Order;
import com.barath.app.model.OrderDTO;
import com.barath.app.model.request.Parameters;
import com.barath.app.model.request.RequestModel;
import com.barath.app.model.response.Data;
import com.barath.app.model.response.ResponseModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class ApiAiChatBotApplication {

	private static  final Logger logger= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final ObjectMapper mapper=new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(ApiAiChatBotApplication.class, args);

	}

	@Value("${inventory.api.url}")
	private String inventoryUrl;


	@Value("${order.api.url}")
	private String orderUrl;

	@Autowired
	private RestTemplate restTemplate;



	@PostMapping(value="/")
	public ResponseModel receiveRequest1(@RequestBody RequestModel request) throws Exception{
		System.out.println("Request Recevied "+request.toString());
		System.out.println("Request Recevied "+new ObjectMapper().writeValueAsString(request));
		Map<String,Object> params=request.getResult().getParameters();
		String actionName=request.getResult().getAction();
		System.out.println("Action name is "+actionName);
		if("getOrder".equalsIgnoreCase(actionName)){
			return handleGetOrder(params);
		}else if("newOrder".equalsIgnoreCase(actionName)){
			return handleCreateOrderFlow(params);
		}else if("getProducts".equalsIgnoreCase(actionName)){
			return handleGetListOfProducts(params);
		}

		ResponseModel responseModel=new ResponseModel();

		responseModel.setSpeech("Sorry No action is found");
		return responseModel;



	}

	public ResponseModel handleGetOrder(Map<String,Object> params) throws Exception{


		System.out.println("PARAMS ARE "+params);
		ResponseModel responseModel=new ResponseModel();
		Long orderId=Long.parseLong((String)params.get("orderid"));

		String orderIdUrl=orderUrl+"/order/"+orderId;
		ResponseEntity<String> responseEntity=restTemplate.getForEntity(orderIdUrl,String.class);
		if(responseEntity.getStatusCode().is2xxSuccessful()){

			OrderDTO orderDto=mapper.readValue(responseEntity.getBody(),OrderDTO.class);
			String responseString=mapper.writeValueAsString(orderDto);

			String speechResponse="Order with order id is "+orderId+" is : product  "+orderDto.getProductName()
						+" from Location "+orderDto.getLocationName()+ "with Quantity "+orderDto.getStatus()+" price is"+
						orderDto.getAmount()+" And Order status is "+orderDto.getStatus();
			responseModel.setDisplayText(speechResponse);
			responseModel.setSpeech(speechResponse);
		}else{
			responseModel.setSpeech("No order with order id "+orderId+" is found. Please provide the correct order id");
			responseModel.setDisplayText("No order with order id "+orderId+" is found. Please provide the correct order id");
		}


		return responseModel;
	}



	public ResponseModel handleCreateOrderFlow(Map<String,Object> params) throws  Exception{

		ResponseModel responseModel=new ResponseModel();
		System.out.println("PARAMS ARE "+params);
		String productName=(String)params.get("product");
		int quantity=0;
		String locationName=(String)params.get("location");
		if(!StringUtils.isEmpty((String)params.get("quantity"))){
			quantity=Integer.parseInt((String)params.get("quantity"));
		}

		String confirmation=(String)params.get("confirm");
		if(StringUtils.isEmpty(productName)){
			responseModel.setSpeech("Please provide the product name that you would like to buy?");
			responseModel.setDisplayText("Please provide the product name that you would like to buy?");
			return responseModel;
		}else if(StringUtils.isEmpty(locationName)){
			responseModel.setSpeech("Please provide the location from which you would like to buy?");
			responseModel.setDisplayText("Please provide the location from which you would like to buy?");
			return responseModel;
		}else if( quantity <= 0){
			responseModel.setSpeech("Please provide the quantity that you would like to buy?");
			responseModel.setDisplayText("Please provide the quantity that you would like to buy?");
			return responseModel;
		}
		OrderDTO orderDTO=new OrderDTO(productName,locationName,2L,2L,quantity, Double.parseDouble("2000"));
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<Object> request=new HttpEntity<>(orderDTO,headers);
		if(logger.isInfoEnabled()){
			logger.info("url is {}",orderUrl+"/order");
			logger.info("request is {}",mapper.writeValueAsString(orderDTO));
		}
		ResponseEntity<String> responseEntity=restTemplate.exchange(orderUrl+"/order", HttpMethod.POST,request,String.class);
		if(responseEntity.getStatusCode().is2xxSuccessful()){
 			OrderDTO responseOrder=mapper.readValue(responseEntity.getBody(),OrderDTO.class);
			responseModel.setDisplayText("Order is placed successfully with order id "+responseOrder.getOrderID());
			responseModel.setSpeech("Order is placed successfully with order id "+responseOrder.getOrderID());
		}else{
			responseModel.setDisplayText("Something gone wrong with the order please try again");
			responseModel.setSpeech("Something gone wrong with the order please try again");
		}


		return responseModel;
	}

	public ResponseModel handleGetListOfProducts(Map<String,Object> params) throws  Exception{

		ResponseModel responseModel=null;
		ResponseEntity<String> responseEntity=restTemplate.getForEntity(inventoryUrl+"/inventory",String.class);
		if(responseEntity.getStatusCode().is2xxSuccessful()){
			String responseBody=responseEntity.getBody();
			List<InventoryDTO> inventoryDTOS=mapper.readValue(responseBody,new TypeReference<List<InventoryDTO>>(){});
			responseModel=new ResponseModel();
			inventoryDTOS.stream().forEach(System.out::println);
			List<String> products=inventoryDTOS.stream().map(InventoryDTO::getProductName).collect(Collectors.toList());
			responseModel.setSpeech(" List of products available "+mapper.writeValueAsString(products));
		}else{
			responseModel=new ResponseModel();
			responseModel.setSpeech("No Products are available at the moment");
			responseModel.setDisplayText("No Products are available at the moment");
			responseModel.setData(new Data());

		}

		return responseModel;
 	}
}
