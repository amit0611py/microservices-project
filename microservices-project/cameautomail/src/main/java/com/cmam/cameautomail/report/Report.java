package com.cmam.cameautomail.report;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.cmam.cameautomail.modal.UploadResponse;
import com.cmam.cameautomail.util.EmailIdFormatter;


@Component
public class Report extends RouteBuilder {
	
	@Value("${automail.reports.url}")
	private String reportsUrl;
	
	@Value("${automail.reports.key}")
	private String reportsKey;
	
	@Value("${automail.email.url}")
	private String emailUrl;
	
	@Value("${report.file.path}")
	private String filePath;
	
	@Value("${report.first.cron}")
	private String rrfCcron;
	
	@Value("${report.first.to}")
	private String rrfCto;
	
	@Value("${report.first.cc}")
	private String rrfCcc;
	
	@Value("${report.first.subject}")
	private String rrfCSubject;
	

	@Override
	public void configure() throws Exception {
	    log.info("Initializing Camel routes...");
	    
//	        from("timer:testTimer?period=10000") // Trigger every 10 seconds
	    from("cron:rrp?schedule=" + rrfCcron)
	        .log("Timer triggered. Sending request to report service...")
	            .log("Cron job triggered. Sending request to report service...")
	            .log("Reports URL: " + reportsUrl)
	            .log("Email URL: " + emailUrl)
	            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
	            .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
	            .setBody().simple("{ \n" +
	                "  \"key\": \"" + reportsKey + "\", \n" +
	                "  \"dataSource\": \"REPORT\", \n" +
	                "  \"procedureName\": \"get_app_users_by_parameter\", \n" +
	                "  \"reportName\": \"User_Data\", \n" +
	                "  \"parameters\": [\"1\"] \n" +
	                "}")
	            .log("Sending request to report service with body: ${body}")
	            .to(reportsUrl + "/uploadReport")
	            .log("Received response from report service: ${body}")
	            .unmarshal(new JacksonDataFormat(UploadResponse.class))
	            .log("Unmarshalled response: ${body}")
	            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
	            .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
	            .setBody().simple(
	                "{ \r\n" +
	                    "  \"recipients\": " + EmailIdFormatter.format(rrfCto) +", \r\n" +
	                    "  \"carbonCopy\": " + EmailIdFormatter.format(rrfCcc) + ", \r\n" +
	                    "  \"bcc\":[], \n" +
	                    "  \"subject\": \"" + rrfCSubject + "\", \n" +
	                    "  \"msgBody\": \"This is the email body.\", \n" +
	                    "  \"templateId\": \"email-template.html\", \n" +
	                    "  \"attachment\": \"" + filePath + "${body.reportName}\", \n" +
	                    "  \"templateParams\": { \n" +
	                    "    \"reportName\": \"" + rrfCSubject + "\", \n" +
	                    "    \"clientName\": \"Client Name\", \n" +
	                    "    \"reportLink\": \"${body.encodedValue}\", \n" +
	                    "    \"msgBody\": \"This is the message body from template parameters.\", \n" +
	                    "    \"dataAsOn\": \"${date:now:dd-MMM-yy}\", \n" +
	                    "    \"replyTo\": \"\", \n" +
	                    "    \"recordCount\": \"${body.recordCount}\", \n" +
	                    "    \"var5\": \"\" \n" +
	                    "  }, \n" +
	                    "  \"noDataReportRequired\": \"Y\" \n" +
	                    "}")
	            .log("Sending email with body: ${body}")
	            .toD(emailUrl + "/sendMailWithAttachment")
	            .log("Email status:: ${body}");
	        
	        
	}
	

}

