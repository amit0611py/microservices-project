package com.cmam.cameautomail.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import com.cmam.cameautomail.util.EmailIdFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReportTriggerService {

    @Value("${automail.reports.url}")
    private String reportsUrl;

    @Value("${automail.reports.key}")
    private String reportsKey;

    @Value("${automail.email.url}")
    private String emailUrl;

    @Value("${report.file.path}")
    private String filePath;

    @Autowired
    private ObjectMapper objectMapper;

    public ResponseEntity<String> triggerReport(String dataSource, String procedureName, String parametersString, String reportName,
                                               String recipients, String carbonCopy, String subject) throws Exception {

    	 List<String> parametersList = new ArrayList<>();
         String[] params = parametersString.replaceAll("\\[|\\]|\\s", "").split(",");
         for (String param: params) {
             parametersList.add(param);
         }


        // Create headers for the report generation API request
        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.setContentType(MediaType.APPLICATION_JSON);

        String json = "{ \n" +
                "  \"key\": \"" + reportsKey + "\", \n" +
                "  \"dataSource\": \"" + dataSource + "\", \n" +
                "  \"procedureName\": \"" + procedureName + "\", \n" +
                "  \"reportName\": \"" + reportName + "\", \n" +
                "  \"parameters\": " + parametersList + " \n" +
                "}";

        HttpEntity<String> reportRequestEntity = new HttpEntity<>(json, reportHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> reportResponse = restTemplate.exchange(reportsUrl + "/uploadReport", HttpMethod.POST, reportRequestEntity, String.class);

        if (reportResponse.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> reportResponseData = objectMapper.readValue(reportResponse.getBody(), Map.class);
            String reportLink = (String) reportResponseData.get("encodedValue");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
            String formattedDate = LocalDateTime.now().format(formatter);

            String emailJson = "{ \n" +
                    "  \"recipients\": " +recipients + ", \n" +
                    "  \"carbonCopy\": " + carbonCopy + ", \n" +
                    "  \"bcc\": [], \n" +
                    "  \"subject\": \"" + subject + "\", \n" +
                    "  \"msgBody\": \"This is the email body.\", \n" +
                    "  \"templateId\": \"email-template.html\", \n" +
                    "  \"attachment\": \"" + filePath + reportResponseData.get("reportName") + "\", \n" +
                    "  \"templateParams\": { \n" +
                    "    \"reportName\": \"" + subject + "\", \n" +
                    "    \"clientName\": \"Client Name\", \n" +
                    "    \"reportLink\": \"" + reportLink + "\", \n" +
                    "    \"msgBody\": \"This is the message body from template parameters.\", \n" +
                    "    \"dataAsOn\": \"" + formattedDate + "\", \n" +
                    "    \"replyTo\": \"\", \n" +
                    "    \"recordCount\": \"" + reportResponseData.get("recordCount") + "\", \n" +
                    "    \"var5\": \"\" \n" +
                    "  }, \n" +
                    "  \"noDataReportRequired\": \"Y\" \n" +
                    "}";
            
//            String emailJson = "{ \n" +
//                    "  \"recipients\": " + recipients + ", \n" +
//                    "  \"carbonCopy\": " + carbonCopy + ", \n" +
//                    "  \"bcc\": [], \n" +
//                    "  \"subject\": \"" + subject + "\", \n" +
//                    "  \"msgBody\": \"This is the email body.\", \n" +
//                    "  \"templateId\": \"email-template.html\", \n" +
//                    "  \"attachment\": \"" + filePath + reportResponseData.get("reportName") + "\", \n" +
//                    "  \"templateParams\": { \n" +
//                    "    \"reportName\": \"" + subject + "\", \n" +
//                    "    \"clientName\": \"Client Name\", \n" +
//                    "    \"reportLink\": \"" + reportLink + "\", \n" +
//                    "    \"msgBody\": \"This is the message body from template parameters.\", \n" +
//                    "    \"dataAsOn\": \"" + formattedDate + "\", \n" +
//                    "    \"replyTo\": \"\", \n" +
//                    "    \"recordCount\": \"" + reportResponseData.get("recordCount") + "\", \n" +
//                    "    \"var5\": \"\" \n" +
//                    "  }, \n" +
//                    "  \"noDataReportRequired\": \"Y\" \n" +
//                    "}";

            HttpHeaders emailHeaders = new HttpHeaders();
            emailHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> emailRequestEntity = new HttpEntity<>(emailJson, emailHeaders);

            return restTemplate.exchange(emailUrl + "/sendMailWithAttachment", HttpMethod.POST, emailRequestEntity, String.class);
        } else {
            return ResponseEntity.status(reportResponse.getStatusCode()).body("Failed to generate report: " + reportResponse.getBody());
        }
    }
}



//{
//	  "dataSource": "REPORT",
//	  "procedureName": "get_app_users_by_parameter",
//	  "parameters": "[\"1\"]",
//	  "reportName": "User_Data",
//	  "recipients": "deenusinghsh@gmail.com",
//	  "carbonCopy": "mitsingh7787@gmail.com", 
//	  "subject": "User Data Report"
//	}