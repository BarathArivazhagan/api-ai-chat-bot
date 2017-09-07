package com.barath.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by barath.arivazhagan on 9/7/2017.
 */

@Configuration
public class RestConfiguration {


    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {

                if(clientHttpResponse.getStatusCode().is2xxSuccessful()){
                    return false;
                }
                return true;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                System.out.println("REST CLIENT ERROR "+clientHttpResponse.getStatusText());
            }
        });


        return restTemplate;
    }
}
