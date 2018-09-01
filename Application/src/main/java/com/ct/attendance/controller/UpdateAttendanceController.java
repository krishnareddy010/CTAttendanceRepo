/**
 * 
 */
package com.ct.attendance.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ct.attendance.exception.AttendanceException;
import com.ct.attendance.models.AttendanceRequest;
import com.ct.attendance.models.AttendanceResponse;
import com.ct.attendance.service.IupdateAttendanceService;

/**
 * @author M Krishna Reddy
 *
 */
@RestController
public class UpdateAttendanceController {

	private static final Logger logger = Logger.getLogger(UpdateAttendanceController.class);
	
	@Autowired
	@Qualifier("attendanceRequestValidator")
	private Validator validator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	@Autowired
	private IupdateAttendanceService service;

	@PostMapping(value = "/ct/updateattendance")
	public ResponseEntity<AttendanceResponse> updateAttendance(@RequestHeader(value="token") String token, @Validated @RequestBody AttendanceRequest request, BindingResult bindingResult) {
		AttendanceResponse response = new AttendanceResponse();
		if(bindingResult.hasErrors()){
			List<String> fieldErrors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
			response.setMessage("failure");
			response.setStatus(false);
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setErrors(fieldErrors);
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}	
		try {
			List<Integer> statusList = service.updateAttendance(request,token);
			logger.info("StatusList===>>>"+statusList);
			if(statusList.size()==statusList.stream().filter(i->i==200).collect(Collectors.toList()).size()){
				response.setMessage("success");
				response.setResponse("Attendance is updated successfully");
				response.setStatus(true);
				response.setStatusCode(HttpStatus.OK.value());
				return new ResponseEntity<>(response,HttpStatus.OK);
			}else{
				logger.info("status list===>>"+statusList);
				response.setMessage("failure");
				response.setResponse("Attendance is not updated");
				response.setStatus(false);
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
			}
		} catch (Exception  e) {
			logger.error("updateAttendance Exception==="+e);
			response.setStatus(false);
			response.setResponse(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(e.getMessage());
			List<String> list = new ArrayList<>();
			list.add(e.getMessage());
			response.setErrors(list);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/ct/getdriveitemid")
	public ResponseEntity<AttendanceResponse> fetchDriveItemId(@RequestParam String name, @RequestHeader(value="token") String token) {
		AttendanceResponse response = new AttendanceResponse();
		try {
			if(name!=null && !name.isEmpty()){
				String driveItemId = service.fetchDriveItemId(name, token);
				response.setStatus(true);
				response.setResponse(driveItemId!=null?driveItemId:"Name is not found in the Shared Drive");
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}else{
				response.setStatus(false);
				response.setResponse(null);
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("failure");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (AttendanceException | JSONException e) {
			logger.error(e);
			response.setStatus(false);
			response.setResponse(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
