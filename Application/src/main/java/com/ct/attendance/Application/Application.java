package com.ct.attendance.Application;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ct.attendance.properties.ProxyProperties;

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
	private ProxyProperties properties;

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException {

		if ("true".equals(properties.getProxyset())) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(properties.getHost(), Integer.parseInt(properties.getPort())));
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
