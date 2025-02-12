package com.personal.report.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UploadResponse implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String reason;
	private String reportName;
	private int recordCount;
	private Date uploadTime;
	private String encodedValue;
	private String result;
	
	
	
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getEncodedValue() {
		return encodedValue;
	}

	public void setEncodedValue(String encodedValue) {
		this.encodedValue = encodedValue;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UploadResponse(){
		
	}
	
//	SUCCESS EXCEL
	public UploadResponse(String status, String reportName, int recordCount, Date uploadTime, String encodedValue){
		super();
		this.status = status;
		this.reportName = reportName;
		this.recordCount = recordCount;
		this.uploadTime = uploadTime;
		this.encodedValue = encodedValue;
	}
	
//	SUCCESS DATA
	public UploadResponse(String status, int recordCount, String result){
		super();
		this.status = status;
		this.recordCount = recordCount;
		this.result = result;
	}
	
	
	
@Override
	public String toString() {
		return "UploadResponse [status=" + status + ", reason=" + reason + ", reportName=" + reportName
				+ ", recordCount=" + recordCount + ", uploadTime=" + uploadTime + ", encodedValue=" + encodedValue
				+ ", result=" + result + "]";
	}

	//	FAILURE
	public UploadResponse(String status, String reason){
		super();
		this.status = status;
		this.reason = reason;
	}
	
}
