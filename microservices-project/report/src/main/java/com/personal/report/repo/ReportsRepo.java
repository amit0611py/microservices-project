package com.personal.report.repo;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONArray;

public interface ReportsRepo {
	
	JSONArray fetchReportData(String dataSource, String procedureName, List<String> parameters) throws SQLException, ParseException, Exception;

}
