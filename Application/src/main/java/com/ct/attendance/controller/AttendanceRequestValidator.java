/**
 * 
 */
package com.ct.attendance.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ct.attendance.models.AttendanceRequest;

/**
 * @author M Krishna Reddy
 *
 */
@Component("attendanceRequestValidator")
public class AttendanceRequestValidator implements Validator {

	private static final String EMPTY_VALUE = "emptyvalue";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AttendanceRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AttendanceRequest request = (AttendanceRequest)target;
		
		if(request.getEmpName()==null || request.getEmpName().isEmpty()){
			errors.rejectValue("empName", EMPTY_VALUE, new Object[]{"empName"}, "employee name can't be empty or null");
		}
		if(request.getEmail()==null || request.getEmail().isEmpty()){
			errors.rejectValue("email",EMPTY_VALUE, new Object[]{"email"}, "Email can't be empty or null");
		}
		if(request.getDriveItemId()==null || request.getDriveItemId().isEmpty()){
			errors.rejectValue("driveItemId", EMPTY_VALUE, new Object[]{"driveItemId"}, "Drive Item Id can't be empty or null");
		}
		if(request.getAttendanceCode()==null || request.getAttendanceCode().isEmpty()){
			errors.rejectValue("attendanceCode", EMPTY_VALUE, new Object[]{"attendanceCode"}, "Attendance code  can't be empty or null");
		}
		if(request.getDate()==null || request.getDate().isEmpty()){
			errors.rejectValue("date", EMPTY_VALUE, new Object[]{"date"}, "Attendance Date  can't be empty or null");
		}
	}

}
