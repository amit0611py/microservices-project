package com.cmam.cameautomail.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import com.cmam.cameautomail.service.ReportTriggerService;
import com.cmam.cameautomail.util.EmailIdFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class autoMailController {
	
	@Autowired
    private ReportTriggerService reportTriggerService;

	
	 @GetMapping("/test")
	    public String hello() {
	        return "Welcome To Report Home Controller!";
	    }
	 
	 @PostMapping("/triggerReport")
	    public ResponseEntity<String> triggerReport(@RequestBody Map<String, Object> requestData) {
		 try {
	            // Extract parameters from the request body
	            String dataSource = (String) requestData.get("dataSource");
	            String procedureName = (String) requestData.get("procedureName");
	            String parameters = (String) requestData.get("parameters");
	            String reportName = (String) requestData.get("reportName");
	            String recipients = EmailIdFormatter.format((String) requestData.get("recipients"));
	            String carbonCopy = EmailIdFormatter.format((String) requestData.get("carbonCopy"));
	            String subject = (String) requestData.get("subject");

	            // Call the service to trigger the report
	            return reportTriggerService.triggerReport(dataSource, procedureName, parameters, reportName, recipients, carbonCopy, subject);

	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error triggering report: " + e.getMessage());
	        }
	    }
	}