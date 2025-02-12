package com.report.SendMail.modal;

import java.util.List;

public class EmailVo {
	
	private String[] recipients;
	private List<String> carbonCopy;
	private List<String> bcc;
	private String subject;
	private String msgBody;
	private String attachment;
	private String templateId;
	private Templates templateParams;
	private String noDataReportRequired;
	
	public String[] getRecipients() {
		return recipients;
	}
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	public List<String> getCarbonCopy() {
		return carbonCopy;
	}
	public void setCarbonCopy(List<String> carbonCopy) {
		this.carbonCopy = carbonCopy;
	}
	public List<String> getBcc() {
		return bcc;
	}
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public Templates getTemplateParams() {
		return templateParams;
	}
	public void setTemplateParams(Templates templateParams) {
		this.templateParams = templateParams;
	}
	public String getNoDataReportRequired() {
		return noDataReportRequired;
	}
	public void setNoDataReportRequired(String noDataReportRequired) {
		this.noDataReportRequired = noDataReportRequired;
	}
	
	public class Templates{
		private String clientName;
		private String reportLink;
		private String msgBody;
		private String reportName;
		private String dataAsOn;
		private String replyTo;
		private String recordCount;
		private String var5;
		public String getClientName() {
			return clientName;
		}
		public void setClientName(String clientName) {
			this.clientName = clientName;
		}
		public String getReportLink() {
			return reportLink;
		}
		public void setReportLink(String reportLink) {
			this.reportLink = reportLink;
		}
		public String getMsgBody() {
			return msgBody;
		}
		public void setMsgBody(String msgBody) {
			this.msgBody = msgBody;
		}
		public String getReportName() {
			return reportName;
		}
		public void setReportName(String reportName) {
			this.reportName = reportName;
		}
		public String getDataAsOn() {
			return dataAsOn;
		}
		public void setDataAsOn(String dataAsOn) {
			this.dataAsOn = dataAsOn;
		}
		public String getReplyTo() {
			return replyTo;
		}
		public void setReplyTo(String replyTo) {
			this.replyTo = replyTo;
		}
		public String getRecordCount() {
			return recordCount;
		}
		public void setRecordCount(String recordCount) {
			this.recordCount = recordCount;
		}
		public String getVar5() {
			return var5;
		}
		public void setVar5(String var5) {
			this.var5 = var5;
		}
		
		
	}
	

}
