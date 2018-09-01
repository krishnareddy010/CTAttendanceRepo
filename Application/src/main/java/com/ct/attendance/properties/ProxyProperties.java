/**
 * 
 */
package com.ct.attendance.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author m Krishna Reddy
 *
 */
@Component
@PropertySource("classpath:proxy.properties")
@ConfigurationProperties(prefix="proxy")
public class ProxyProperties {

	private String proxyset;
	
	private String host;
	
	private String port;

	
	/**
	 * @return the proxyset
	 */
	public String getProxyset() {
		return proxyset;
	}

	/**
	 * @param proxyset the proxyset to set
	 */
	public void setProxyset(String proxyset) {
		this.proxyset = proxyset;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	
}
