/**
 * 
 */
package com.ct.attendance.properties;

import java.util.Map;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author M Krishna Reddy
 * 
 *
 */
@Component
@PropertySource("classpath:graphapi.properties")
@ConfigurationProperties
public class GraphApiProperties {

	private String updateXLSheetUrl;
	
	private String updateCellFormatUrl;
	
	private String batchRequestApi;

	private Map<Integer,String> mapProp;
	
	private String xlsheetDetailInfoURL;
	
	private String graphApi;
	
	private String sharedApi;
	
	private String fromDate;
	
	private String toDate;
	
	private Map<String,String> colorCode;
	
	private String proxy;
	
	
	
	/**
	 * @return the colorCode
	 */
	public Map<String, String> getColorCode() {
		return colorCode;
	}

	/**
	 * @param colorCode the colorCode to set
	 */
	public void setColorCode(Map<String, String> colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the sharedApi
	 */
	public String getSharedApi() {
		return sharedApi;
	}

	/**
	 * @param sharedApi the sharedApi to set
	 */
	public void setSharedApi(String sharedApi) {
		this.sharedApi = sharedApi;
	}

	/**
	 * @return the graphApi
	 */
	public String getGraphApi() {
		return graphApi;
	}

	/**
	 * @param graphApi the graphApi to set
	 */
	public void setGraphApi(String graphApi) {
		this.graphApi = graphApi;
	}

	/**
	 * @return the xlsheetDetailInfoURL
	 */
	public String getXlsheetDetailInfoURL() {
		return xlsheetDetailInfoURL;
	}

	/**
	 * @param xlsheetDetailInfoURL the xlsheetDetailInfoURL to set
	 */
	public void setXlsheetDetailInfoURL(String xlsheetDetailInfoURL) {
		this.xlsheetDetailInfoURL = xlsheetDetailInfoURL;
	}

	/**
	 * @return the mapProp
	 */
	public Map<Integer, String> getMapProp() {
		return mapProp;
	}

	/**
	 * @param mapProp the mapProp to set
	 */
	public void setMapProp(Map<Integer, String> mapProp) {
		this.mapProp = mapProp;
	}

	/**
	 * @return the updateXLSheetUrl
	 */
	public String getUpdateXLSheetUrl() {
		return updateXLSheetUrl;
	}

	/**
	 * @param updateXLSheetUrl the updateXLSheetUrl to set
	 */
	public void setUpdateXLSheetUrl(String updateXLSheetUrl) {
		this.updateXLSheetUrl = updateXLSheetUrl;
	}

	/**
	 * @return the updateCellFormatUrl
	 */
	public String getUpdateCellFormatUrl() {
		return updateCellFormatUrl;
	}

	/**
	 * @param updateCellFormatUrl the updateCellFormatUrl to set
	 */
	public void setUpdateCellFormatUrl(String updateCellFormatUrl) {
		this.updateCellFormatUrl = updateCellFormatUrl;
	}

	/**
	 * @return the batchRequestApi
	 */
	public String getBatchRequestApi() {
		return batchRequestApi;
	}

	/**
	 * @param batchRequestApi the batchRequestApi to set
	 */
	public void setBatchRequestApi(String batchRequestApi) {
		this.batchRequestApi = batchRequestApi;
	}
	
	
}
