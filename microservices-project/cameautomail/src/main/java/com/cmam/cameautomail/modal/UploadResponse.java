package com.cmam.cameautomail.modal;

import java.util.Date;

public class UploadResponse {
	
	private String status;
    private String reason;
    private String reportName;
    private int recordCount;
    private Date uploadTime;
    private String encodedValue;
    private String result;
    
    public UploadResponse() {
    	
    }
    
    public UploadResponse(String status, String reportName, int recordCount, Date uploadTime, String encodedValue) {
    	super();
    	this.status = status;
    	this.reportName = reportName;
    	this.recordCount = recordCount;
    	this.uploadTime = uploadTime;
    	this.encodedValue = encodedValue;
    }
    
    public UploadResponse(String status, String reason) {
    	super();
    	this.status = status;
    	this.reason = reason;
    }

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
    
    
    

}
