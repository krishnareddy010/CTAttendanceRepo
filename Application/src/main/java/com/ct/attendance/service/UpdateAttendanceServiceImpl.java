/**
 * 
 */
package com.ct.attendance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ct.attendance.exception.AttendanceException;
import com.ct.attendance.models.AttendanceRequest;
import com.ct.attendance.properties.GraphApiProperties;
import com.ct.attendance.util.UpdateAttendanceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author krishna Reddy
 *
 */
@Component
public class UpdateAttendanceServiceImpl implements IupdateAttendanceService {

	private static final Logger logger = Logger.getLogger(UpdateAttendanceServiceImpl.class);

	@Autowired
	private UpdateAttendanceUtil util;

	@Autowired
	private GraphApiProperties properties;

	@Override
	public String fetchDriveItemId(String name, String token) throws AttendanceException, JSONException {
		JSONArray valueArray = util.fetchSharedDriveResponse(token);
		String driveItemId = null;
		for (int i = 0; i < valueArray.length(); i++) {
			JSONObject json = (JSONObject) valueArray.get(i);
			if (json.getString("name").indexOf(name) > -1) {
				driveItemId = json.getString("id");
				break;
			}
		}
		return driveItemId;
	}

	@Override
	public List<Integer> updateAttendance(AttendanceRequest request, String token) throws AttendanceException {
		String coulmnName = updateCell(request,token);
		String sheetName = util.getSheetName(request.getDate(), properties.getFromDate(), properties.getToDate());
		List<Integer> statusList = new ArrayList<>();

		try {
			String batchRequest = util.prepareBatchRequest(coulmnName, sheetName, request.getDriveItemId(),	request.getAttendanceCode());
			logger.info("batchRequest==>>" + batchRequest);
			ResponseEntity<String> response = util.updateAttendanceCell(batchRequest, token);
			if (response != null && response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				String reponseBody = response.getBody();

				JSONObject jsonObj = new JSONObject(reponseBody);
				JSONArray jsonArr = jsonObj.getJSONArray("responses");

				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject innerJson = (JSONObject) jsonArr.get(i);
					statusList.add(Integer.parseInt(innerJson.get("status").toString()));
				}

			} else {
				logger.info("Response Status Code == >>" + response.getStatusCode() + "Body==>>");
				throw new AttendanceException("Response Body is Empty or status code is not OK" + response != null
						? response.getBody() : null);
			}
		} catch (JsonProcessingException | JSONException e) {
			logger.error("Exception is occured while updating the attendance" + e);
			throw new AttendanceException("Exception is occured while updating the attendance" + e);
		}
		return statusList;
	}

	public String updateCell(AttendanceRequest request, String token) {
		String excelDate = util.getExcelDate(request.getDate());
		String sheetName = util.getSheetName(request.getDate(), properties.getFromDate(), properties.getToDate());
		Map<String, Integer> cellMap = util.fetchUpdateCell(token, request.getEmpName(), excelDate,
				request.getDriveItemId(), sheetName);
		Map<Integer, String> rowMap = properties.getMapProp();
		String columeName = rowMap.get(cellMap.get(excelDate));
		columeName = columeName.trim() + cellMap.get(request.getEmpName());
		logger.info("column name==>>>" + columeName);
		return columeName;
	}

}
