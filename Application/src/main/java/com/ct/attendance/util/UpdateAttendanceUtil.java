/**
 * 
 */
package com.ct.attendance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ct.attendance.exception.AttendanceException;
import com.ct.attendance.models.BatchRequestBody;
import com.ct.attendance.models.Headers;
import com.ct.attendance.models.Request;
import com.ct.attendance.properties.GraphApiProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @authorM Krishna Reddy
 *
 */
@Component
public class UpdateAttendanceUtil {

	private static final Logger logger = Logger.getLogger(UpdateAttendanceUtil.class);

	@Autowired
	private GraphApiProperties graphProperties;

	@Autowired
	private RestTemplate restTemplate;

	private ObjectMapper mapper = new ObjectMapper();

	public JSONArray fetchSharedDriveResponse(String token) throws AttendanceException {

		HttpEntity<String> entity = new HttpEntity<>(setHeaders(token));
		ResponseEntity<String> response;
		JSONArray valueArray = null;
		try {
			Map<String, String> params = new HashMap<>();
			params.put("graphApi", graphProperties.getGraphApi());
			response = restTemplate.exchange(graphProperties.getSharedApi(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<String>() {
					}, params);
			if (response != null && response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				System.out.println("respone ==>>" + response.getBody());
				org.json.JSONObject jsonObj = new org.json.JSONObject(response.getBody());
				valueArray = (JSONArray) jsonObj.get("value");
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new AttendanceException("Exception is occured while fetching Drive item id from shred drive");
		}
		return valueArray;
	}

	public ResponseEntity<String> updateAttendanceCell(String batchRequest, String token) throws AttendanceException {
		HttpEntity<String> entity = new HttpEntity<>(batchRequest, setHeaders(token));
		ResponseEntity<String> response;
		try {
			Map<String, String> params = new HashMap<>();
			params.put("graphApi", graphProperties.getGraphApi());
			response = restTemplate.exchange(graphProperties.getBatchRequestApi(), HttpMethod.POST, entity,
					new ParameterizedTypeReference<String>() {
					}, params);

		} catch (Exception e) {
			logger.error("Exception is occured while updating the XL sheet==>>>" + e);
			throw new AttendanceException("Exception is occured while updating the time sheet" + e);
		}
		return response;
	}

	public Map<String, Integer> fetchUpdateCell(String token, String name, String date, String driverItemId,
			String sheetName) {
		logger.info("name==>>" + name + "==sheetName===>>" + sheetName);
		ResponseEntity<String> response;
		Map<String, Integer> cellMap = new HashMap<>();
		try {
			Map<String, String> params = new HashMap<>();
			params.put("graphApi", graphProperties.getGraphApi());
			params.put("drive-item-id", driverItemId);
			params.put("sheet-name", sheetName);

			HttpEntity<String> entity = new HttpEntity<>(setHeaders(token));
			response = restTemplate.exchange(graphProperties.getXlsheetDetailInfoURL(), HttpMethod.GET, entity,
					new ParameterizedTypeReference<String>() {
					}, params);
			org.json.JSONObject obj = new org.json.JSONObject(response.getBody());
			JSONArray forObj = (JSONArray) obj.get("formulas");

			int count = 0;
			for (int i = 0; i < forObj.length(); i++) {
				JSONArray innerObj = (JSONArray) forObj.get(i);
				if (count < 3) {
					for (int j = 0; j < innerObj.length(); j++) {
						if (date.equals(String.valueOf(innerObj.get(j)))) {
							cellMap.put(String.valueOf(innerObj.get(j)), j);
							count++;
							break;
						} else if (name.equals(String.valueOf(innerObj.get(j)))) {
							cellMap.put(String.valueOf(innerObj.get(j)), i + 1);
							count++;
							break;
						}
					}
				}
			}
			logger.info("cellMap==>>" + cellMap);
		} catch (RestClientException | JSONException e) {
			logger.error("Exception is occured while fetching update cell from xl sheet==>>" + e);
		}
		return cellMap;
	}

	public String prepareBatchRequest(String columnName, String sheetName, String driveItemId, String attendanceCode)
			throws JSONException, JsonProcessingException {
		Headers headerJson = new Headers();
		headerJson.setContentType("application/json");
		BatchRequestBody requestBody = new BatchRequestBody();
		List<Request> requestList = new ArrayList<>();
		Map<String, String> colorCode = graphProperties.getColorCode();
		Request firstRequest = new Request();
		firstRequest.setId("1");
		firstRequest.setHeaders(headerJson);
		firstRequest.setMethod("PATCH");
		firstRequest.setUrl(graphProperties.getUpdateXLSheetUrl().replace("{drive-item-id}", driveItemId)
				.replace("{sheet-name}", sheetName).replace("{updatedCell}", "'" + columnName + "'"));
		List<List<String>> firstUriBody = new ArrayList<>();
		firstUriBody.add(Arrays.asList(attendanceCode));
		JSONObject firstUriJson = new JSONObject();
		firstUriJson.put("formulasR1C1", firstUriBody);
		firstRequest.setBody(firstUriJson);
		requestList.add(firstRequest);
		firstRequest = new Request();
		firstRequest.setId("2");
		firstRequest.setHeaders(headerJson);
		firstRequest.setMethod("PATCH");
		firstRequest.setUrl(graphProperties.getUpdateCellFormatUrl().replace("{drive-item-id}", driveItemId)
				.replace("{sheet-name}", sheetName).replace("{updatedCell}", "'" + columnName + "'"));
		JSONObject secondUriJson = new JSONObject();
		secondUriJson.put("color", colorCode.get(attendanceCode));
		firstRequest.setBody(secondUriJson);
		requestList.add(firstRequest);
		requestBody.setRequests(requestList);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		return mapper.writeValueAsString(requestBody);
	}

	public String getExcelDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat(graphProperties.getFromDate());
		try {

			Date date = formatter.parse(dateString);
			Double excelDate = DateUtil.getExcelDate(date);
			logger.info("Excel Date===>>" + excelDate.intValue());
			return String.valueOf(excelDate.intValue());
		} catch (ParseException e) {
			logger.error("Exception is occured while converting intp excel date==>>" + e);
		}
		return null;
	}

	public String getSheetName(String date, String givenformat, String resultformat) {
		String sheetName = "";
		SimpleDateFormat sdf;
		SimpleDateFormat sdf1;

		try {
			sdf = new SimpleDateFormat(givenformat);
			sdf1 = new SimpleDateFormat(resultformat);
			sheetName = sdf1.format(sdf.parse(date));
		} catch (Exception e) {
			logger.error("Exception is occured while fetching the sheet name from date====>>" + e);
		}
		logger.info("SheetnName==>>sheetName" + "==date==>>" + date);
		return sheetName;
	}

	public static HttpHeaders setHeaders(String UAAToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + UAAToken);

		return headers;
	}
}
