/**
 * 
 */
package com.ct.attendance.models;

/**
 * @author M Krishna Reddy
 *
 */
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "method", "headers", "url", "body" })
public class Request {

	@JsonProperty("id")
	private String id;
	@JsonProperty("method")
	private String method;
	@JsonProperty("headers")
	private Headers headers;
	@JsonProperty("url")
	private String url;
	@JsonProperty("body")
	private JSONObject body;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("method")
	public String getMethod() {
		return method;
	}

	@JsonProperty("method")
	public void setMethod(String method) {
		this.method = method;
	}

	@JsonProperty("headers")
	public Headers getHeaders() {
		return headers;
	}

	@JsonProperty("headers")
	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty("body")
	public JSONObject getBody() {
		return body;
	}

	@JsonProperty("body")
	public void setBody(JSONObject body) {
		this.body = body;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
