package com.personal.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.personal.report.model.UploadRequest;
import com.personal.report.model.UploadResponse;
import com.personal.report.service.ReportServiceImpl;

@RestController
public class ReportController {
	
	@Autowired
	private ReportServiceImpl repoServ;
	
	@Value("${app.accesskey}")
	private String accessKey;
	
	 @GetMapping("/test")
	    public String hello() {
	        return "Welcome To Report Home Controller!";
	    }
	 
	 @PostMapping("/getData")
	 public ResponseEntity<?> getData(@RequestBody UploadRequest request) {
	     if (!request.getKey().equals(this.accessKey)) {
	    	 System.out.println("Error: "+request.getKey());
	    	 System.out.println("Error:: "+this.accessKey);
	    	 System.out.println("Error");
	         return new ResponseEntity<>(new UploadResponse("Fail", "Invalid access key"), HttpStatus.UNAUTHORIZED); // Correct
	     }

	     try {
	         JSONArray jsonArr = repoServ.getData(request.getDataSource(), request.getProcedureName(), request.getParameters());
	         return new ResponseEntity<>(new UploadResponse("SUCCESS", jsonArr.length(), jsonArr.toString()), HttpStatus.OK); // Correct
	     } catch (Exception e) { // Catch any exceptions
	         return new ResponseEntity<>(new UploadResponse("Error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR); // Handle errors
	     }
	 }
	 
	 @PostMapping("/testJson")
	 public ResponseEntity<Object> testJson(@RequestBody UploadRequest request) {
	     return ResponseEntity.ok().body(Map.of("message", "This is a test JSON response"));
	 }
	 
	 @PostMapping("/uploadReport")
	 public ResponseEntity<?> updateReport(@RequestBody UploadRequest request) {
	     if (!request.getKey().equals(this.accessKey)) {
	         System.out.println("Error: " + request.getKey());
	         System.out.println("Error:: " + this.accessKey);
	         System.out.println("Error:::");
	         return new ResponseEntity<>(new UploadResponse("Fail", "Invalid access key"), HttpStatus.UNAUTHORIZED);
	     }

	     try {
	         return new ResponseEntity<>(repoServ.getReport(
	                 request.getDataSource(),
	                 request.getProcedureName(),
	                 request.getParameters(),
	                 request.getReportName()), HttpStatus.OK);
//	     try {
//	            UploadResponse uploadResponse = repoServ.getReport(
//	                    request.getDataSource(),
//	                    request.getProcedureName(),
//	                    request.getParameters(),
//	                    request.getReportName());
//
//	            // Manually create a JSON object
//	            JSONObject jsonResponse = new JSONObject();
//	            jsonResponse.put("status", uploadResponse.getStatus());
//	            jsonResponse.put("reportName", uploadResponse.getReportName());
//	            jsonResponse.put("recordCount", uploadResponse.getRecordCount());
//	            jsonResponse.put("uploadTime", uploadResponse.getUploadTime());
//	            jsonResponse.put("encodedValue", uploadResponse.getEncodedValue());
//
//	            // Return the JSON object as a String
//	            return ResponseEntity.ok()
//	                  .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//	                  .body(jsonResponse.toString());
	     } catch (SQLException e) {
	         System.out.println("SQL Error: " + e);
	         return new ResponseEntity<>(new UploadResponse("Error", "SQL Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	     } catch (IOException e) {
	         System.out.println("IO Error: " + e);
	         return new ResponseEntity<>(new UploadResponse("Error", "IO Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	     } catch (ParseException e) {
	         System.out.println("Parse Error: " + e);
	         return new ResponseEntity<>(new UploadResponse("Error", "Parse Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	     } catch (IllegalArgumentException e) {
	         System.out.println("Illegal Argument Error: " + e);
	         return new ResponseEntity<>(new UploadResponse("Error", "Illegal Argument Error: " + e.getMessage()), HttpStatus.BAD_REQUEST);
	     } catch (Exception e) {
	         System.out.println("Error Came: " + e);
	         return new ResponseEntity<>(new UploadResponse("Error", "An unexpected error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	 }
	 

}
