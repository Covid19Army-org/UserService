package com.covid19army.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.covid19army.UserService.client.TokenServiceClient;
import com.covid19army.core.common.clients.OtpServiceClient;
import com.covid19army.core.exceptions.GlobalExceptionHandler;
import com.covid19army.core.extensions.HttpServletRequestExtension;
import com.covid19army.core.mex.rabbitmq.RabbitMQConfig;
import com.covid19army.core.mex.rabbitmq.RabbitMQListenerConfig;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {OtpServiceClient.class, TokenServiceClient.class})
@Import({RabbitMQConfig.class, RabbitMQListenerConfig.class, GlobalExceptionHandler.class})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	@Bean
	public HttpServletRequestExtension httpServletRequestExtension() {
		return new HttpServletRequestExtension();
	}
	
	@Bean
	public RabbitMQSender otpExchangeSender(
			@Value("${covid19army.rabbitmq.mobileotpexchange}") final String otpexchange,
			@Value("${covid19army.rabbitmq.mobileotpexchange.routingkey:}") final String routingkey) {
		return new RabbitMQSender(otpexchange, routingkey);
		
	}
}
