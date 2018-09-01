/**
 * 
 */
package com.ct.attendance.service;

import java.util.List;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.ct.attendance.exception.AttendanceException;
import com.ct.attendance.models.AttendanceRequest;

/**
 * @author Krishna Reddy
 *
 */
@Service
public interface IupdateAttendanceService {

	public List<Integer> updateAttendance(AttendanceRequest request, String token)throws AttendanceException;
	
	public String fetchDriveItemId(String name, String token)throws AttendanceException, JSONException;
}
