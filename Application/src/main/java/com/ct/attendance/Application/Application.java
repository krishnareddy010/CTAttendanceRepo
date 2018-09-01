package com.ct.attendance.Application;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ct.attendance.properties.GraphApiProperties;
import com.ct.attendance.service.Autowired;

/**
 * @author M Krishna Reddy
 * 
 *
 */
@SpringBootApplication
@ComponentScan("com.ct")
public class Application {

	private RestTemplate restTemplate;

	@Autowired
	private GraphApiProperties properties;

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException {

		if ("true".equals(properties.getProxy())) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("sjc1intproxy01.crd.ge.com", 8080));
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setProxy(proxy);
			SSLUtil.turnOffSslChecking();
			restTemplate = new RestTemplate(requestFactory);
			return restTemplate;
		} else {
			return new RestTemplate();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
