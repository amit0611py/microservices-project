package com.personal.report.model;

import java.util.List;

public class UploadRequest {
	
	private String key;
	private String dataSource;
	private String procedureName;
	private String reportName;
	private List<String> parameters;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		return "UploadRequest [key=" + key + ", dataSource=" + dataSource + ", procedureName=" + procedureName
				+ ", reportName=" + reportName + ", parameters=" + parameters + "]";
	}
	
	

}
