package com.personal.report.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.personal.report.model.UploadResponse;
import com.personal.report.repo.ReportsRepo;

@Service
public class ReportServiceImpl {
	
	@Autowired
	private ReportsRepo reportsRepo;
	
	@Value("${file.path}")
	private String filePath;
	
	public JSONArray getData(String dataSource,String procedureName,List<String> parameters)
	throws SQLException,IOException, ParseException, Exception{
		 
		JSONArray data = (JSONArray) reportsRepo.fetchReportData(dataSource,procedureName,parameters);
		return data;
	}

	public UploadResponse getReport(String dataSource, String procedureName, List<String> parameters,
			String reportName) throws SQLException,IOException, ParseException, Exception{
		// TODO Auto-generated method stub
        JSONArray data = getData(dataSource, procedureName, parameters);
        if(data.length()>0) {
        	String fileName = saveReportInExcel(data, reportName);
        	
        	return new UploadResponse("SUCCESS", fileName, data.length(), new Date(), 
        			Base64.getEncoder().encodeToString((filePath + fileName).getBytes()));
        }

		return new UploadResponse("SUCCESS", null, 0, new Date(), null);
	}

	private String saveReportInExcel(JSONArray data, String reportName)  throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		String folderName = "";

	    if (reportName.contains("//")) {
	        String[] nameArray = reportName.split("//");
	        reportName = nameArray[nameArray.length - 1];
	        folderName = nameArray[nameArray.length - 2] + "//";
	    }
	    
	    String fileName = LocalDateTime.now().toString().replaceAll("[^0-9]", "") + "-" + reportName + ".xlsx";

	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet(fileName);
	    
	    int i = 0;
	    
	    for(int rowCount = 0; rowCount < data.length() +1; rowCount++) {
	    	JSONObject rowData = data.getJSONObject(i);
	    	Row row = sheet.createRow(rowCount);
	    	
	    	Iterator<String> keys = rowData.keys();
	    	
	    	int j = 0;
	    	while(keys.hasNext()) {
	    		String key = keys.next();
	    		if(rowCount == 0)
	    			row.createCell(j).setCellValue(key);
	    		
	    		if(rowCount > 0 && rowData.get(key) != null)
	    			row.createCell(j).setCellValue(rowData.get(key).toString());
	    		j++;
	    	}
	    	if(rowCount > 0)
	    		i++;
	    }
	    
	    try(FileOutputStream outputStream = new FileOutputStream(new File(filePath + fileName))){
	    	workbook.write(outputStream);
	    	workbook.close();
	    }
	    
		return folderName + fileName;
	}

}
