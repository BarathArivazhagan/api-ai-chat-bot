package com.barath.app;

import ai.api.model.AIResponse;
import com.barath.app.model.Order;
import com.barath.app.model.request.Parameters;
import com.barath.app.model.request.RequestModel;
import com.barath.app.model.response.ResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApiAiChatBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAiChatBotApplication.class, args);
	}



	@PostMapping(value="/")
	public ResponseModel receiveRequest1(@RequestBody RequestModel request) throws Exception{
		System.out.println("Request Recevied "+request.toString());
		System.out.println("Request Recevied "+new ObjectMapper().writeValueAsString(request));
		Parameters params=request.getResult().getParameters();
		Order order=new Order(params.getProduct(),params.getLocation(),params.getQuantity());
		order.getOrderId().set(1);
		System.out.println("ORDER CREATED "+new ObjectMapper().writeValueAsString(order));

		ResponseModel response=new ResponseModel();
		response.setSpeech("Order is placed successfully");
		response.setDisplayText("Order is placed");
		return response;
	}
}
