package com.personal.report.repo;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.RuntimeErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.validator.GenericValidator;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportsImpl implements ReportsRepo {

    @Autowired
    @Qualifier("report")
    private JdbcTemplate reportJdbcTemplate;

    @Override
    public JSONArray fetchReportData(String dataSource, String procedureName, List<String> parameters) throws SQLException, ParseException, Exception {
        JSONArray result = new JSONArray(); // Initialize result
        Connection connection = null;
        
        try {
        	connection = getConnection(dataSource);
        	List<String> input = new ArrayList<>(parameters);
        	String query = null;
        	Collections.fill(input,"?");
        	if(parameters.size() > 0)
        		query = String.format("{call %s(%s, ?)}", procedureName,String.join(",", input));
        	else
        		query = String.format("{call %s(?)}", procedureName);
        	
        	CallableStatement statement = connection.prepareCall(query);
        	
        	 for (int i = 0; i < parameters.size(); i++) {
        		 String parameter = parameters.get(i);
        		 if (GenericValidator.isDate(parameter, "dd/MM/yyyy", true)) {
        			 java.util.Date date = new SimpleDateFormat("dd/MM/yyyy").parse(parameter);
                     statement.setDate(i + 1, new java.sql.Date(date.getTime()));
        		 }else {
                     statement.setString((i + 1), parameter);
                 }
        	 }
        	 
        	 statement.registerOutParameter(parameters.size() + 1, OracleTypes.REF_CURSOR);
        	 statement.execute();

             ResultSet set = (ResultSet) statement.getObject(parameters.size() + 1);
             
             if (set != null) {
            	 
            	 result = getJSONArray(set);
//                 result = getJSONArray(set,parameters, result);
             }
            
             
             return result;
        }finally {
        	connection.close();
        }



    }

    private Connection getConnection(String dataSource) throws Exception{
		// TODO Auto-generated method stub
    	Connection connection = null;
    	switch(dataSource) {
    	case "REPORT":
    		connection = reportJdbcTemplate.getDataSource().getConnection();
    		break;
    	default: 
    		throw new Exception("Invalid dataSource. Value is not matched");
    	
    	}
    	
		return connection;
	}
    
    private JSONArray getJSONArray(ResultSet set) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData md = set.getMetaData();
        int numCols = md.getColumnCount();
        List<String> colNames = new ArrayList<>();
        for (int i = 1; i <= numCols; i++) {
            colNames.add(md.getColumnName(i));
        }

        while (set.next()) {
            JSONObject row = new JSONObject();
            for (String colName: colNames) {
                row.put(colName, set.getObject(colName));
            }
            jsonArray.put(row);
        }
        
//        System.out.println("getJSONArray result: " + jsonArray.toString(2)); // Use toString(2) for pretty printing

        return jsonArray;
    }
//
//	private JSONArray getJSONArray(ResultSet set, List<String> parameters, JSONArray result) throws SQLException {
//    	ResultSetMetaData md = set.getMetaData();
//    	int numCols = md.getColumnCount();
//    	List<String> colNames = IntStream.range(0, numCols).mapToObj(i->{
//    		try{
//    			return md.getColumnName(i+1);
//    		}catch (SQLException e) {
//    			e.printStackTrace();
//    			return "?";
//    		}
//    	}).collect(Collectors.toList());
//    	
//    	result = new JSONArray();
//    	 while (set.next()) {
//    		 JSONObject row = new JSONObject() {
//    			 @Override
//    			 public JSONObject put(String key,Object value) throws JSONException{
//    				 try {
//    					 Field map = JSONObject.class.getDeclaredField("map");
//    					 map.setAccessible(true);
//    					 Object mapValue = map.get(this);
//    					 if(!(mapValue instanceof LinkedHashMap)) {
//    						 map.set(this, new LinkedHashMap<>());
//    					 } 
//    				 }catch(NoSuchFieldException | IllegalAccessException e) {
//    						 throw new RuntimeException(e);
//    					 }
//    					 return super.put(key, value);
//    				 }
//    			 };
//    			 colNames.forEach(cn -> {
//    				 try {
//    					 row.put(cn,  set.getObject(cn) != null ? set.getObject(cn) : "");
//    				 }catch(JSONException | SQLException e) {
//    					 e.printStackTrace();
//    				 }
//    			 });
//    			 result.put(row);
//    		 }
//    		 return result;
//    }
}