/**
 * 
 */
package com.ct.attendance.models;

import javax.validation.constraints.NotNull;

/**
 * @author M Krishna Reddy
 *
 */
public class AttendanceRequest {
	
	private String empName;
	
	private String email;
	
	private String date;
	
	private String driveItemId;
	
	private String attendanceCode;
	
	/**
	 * @return the attendanceCode
	 */
	public String getAttendanceCode() {
		return attendanceCode;
	}

	/**
	 * @param attendanceCode the attendanceCode to set
	 */
	public void setAttendanceCode(String attendanceCode) {
		this.attendanceCode = attendanceCode;
	}

	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}

	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the driveItemId
	 */
	public String getDriveItemId() {
		return driveItemId;
	}

	/**
	 * @param driveItemId the driveItemId to set
	 */
	public void setDriveItemId(String driveItemId) {
		this.driveItemId = driveItemId;
	}
	
}
