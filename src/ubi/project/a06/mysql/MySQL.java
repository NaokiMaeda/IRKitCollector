package ubi.project.a06.mysql;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ubi.project.a06.message.GetMessage;

public class MySQL {
	private Connection connection;
	
	//DB情報
	private	String		address;
	private	String		host;
	private	int			port;
	private String		DBName;
	private	String		commandTable;
	private	String		logTable;
	private String		user;
	private	String		password;
	private	Gson		gson;
	
	//SQLクエリ
	private	Statement			statement;
	private	String				sql;
	private	StringBuilder		columnBuilder;
	private	StringBuilder		valueBuilder;
	private	StringBuilder		deleteBuilder;
	private	StringBuilder		whereBuilder;
	private	ResultSet			resultSet;
	
	//DBデータ
	private ResultSetMetaData					rsmd;
	private	ArrayList<String>					column;
	private	LinkedHashMap<String , Object>		resultList;
	
	public MySQL(String configFile){
		this.column = new ArrayList<>();
		this.gson	= new Gson();
		setDBInfo(configFile);
	}
	
	public void ConnectionDB(){
		try {
			connection = DriverManager.getConnection(address , user , password);
			column = getColumnList(commandTable);
			System.out.println("接続完了");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DisconnectionDB(){
		if(!hasDB())	return;
		try {
			connection.close();
			System.out.println("切断完了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insert(String table , HashMap<String , Object> data){
		if(!hasDB())	return;
		try {
			statement = connection.createStatement();
			
			columnBuilder	= new StringBuilder();
			valueBuilder	= new StringBuilder();
			
			columnBuilder.append("(");
			valueBuilder.append("(");
			
			for (Map.Entry<String , Object> insertData : data.entrySet()) {
				columnBuilder.append(insertData.getKey());
				columnBuilder.append(",");
				valueBuilder.append(insertData.getValue());
				valueBuilder.append(",");
			}
			
			columnBuilder.deleteCharAt(columnBuilder.length() - 1);
			valueBuilder.deleteCharAt(valueBuilder.length() - 1);
			columnBuilder.append(")");
			valueBuilder.append(")");
			
			
			
			sql = "insert into " + table;
			sql += columnBuilder.toString() + " values" + valueBuilder.toString();
			
			statement = connection.createStatement();
			statement.executeQuery(sql);
			
			/*
			preparedStatement = connection.prepareStatement(sql);
			for(int i = 1; i < column.size(); i++){
				preparedStatement.setObject(i, data.get(column.get(i)));
			}
			preparedStatement.execute();
			*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedHashMap<String, Object> selectAll(String table){
		if(!hasDB())	return null;
		try {
			statement = connection.createStatement();
			sql = "select * from " + table;
			resultSet = statement.executeQuery(sql);
			resultList = new LinkedHashMap<>();
			while(resultSet.next()){
				for(int i = 0; i < column.size(); i++){
					resultList.put(column.get(i), resultSet.getString(column.get(i)));
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public LinkedHashMap<String , Object> select(String table , ArrayList<String> select){
		if(!hasDB())	return null;
		try {
			statement = connection.createStatement();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select ");
			for(int i = 0; i < select.size(); i++){
				stringBuilder.append(select.get(i));
				stringBuilder.append(", ");
			}
			stringBuilder.delete(stringBuilder.length() -2 , stringBuilder.length());
			stringBuilder.append(" from ");
			stringBuilder.append(table);
			
			sql = stringBuilder.toString();
			
			resultSet = statement.executeQuery(sql);
			resultList = new LinkedHashMap<String , Object>();
			while(resultSet.next()){
				for(int i = 0; i < select.size(); i++){
					resultList.put(select.get(i), resultSet.getObject(select.get(i)));
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public LinkedHashMap<String , Object> select(String table , ArrayList<String> select , HashMap<String, Object> terms){
		if(!hasDB())	return null;
		try {
			statement = connection.createStatement();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select ");
			for(int i = 0; i < select.size(); i++){
				stringBuilder.append(select.get(i));
				stringBuilder.append(", ");
			}
			stringBuilder.delete(stringBuilder.length() -2 , stringBuilder.length());
			stringBuilder.append(" from ");
			stringBuilder.append(table);
			
			sql = stringBuilder.toString();
			sql += createWhere(terms);
			
			resultSet = statement.executeQuery(sql);
			resultList = new LinkedHashMap<>();
			while(resultSet.next()){
				for(int i = 0; i < select.size(); i++){
					resultList.put(select.get(i), resultSet.getObject(select.get(i)));
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public LinkedHashMap<int[] , String> getCommandList(String table , ArrayList<String> select){
		if(!hasDB())	return null;
		LinkedHashMap<int[] , String> resultList = new LinkedHashMap<>();
		try {
			statement = connection.createStatement();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select ");
			for(int i = 0; i < select.size(); i++){
				stringBuilder.append(select.get(i));
				stringBuilder.append(", ");
			}
			stringBuilder.delete(stringBuilder.length() -2 , stringBuilder.length());
			stringBuilder.append(" from ");
			stringBuilder.append(table);
			
			sql = stringBuilder.toString();
			
			resultSet = statement.executeQuery(sql);
			Gson gson = new Gson();
			while(resultSet.next()){
				GetMessage message = gson.fromJson(resultSet.getString(select.get(1)) , GetMessage.class);
				resultList.put(message.getData(), resultSet.getString(select.get(0)));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public void delete(String table , HashMap<String , Object> deleteTerms){
		if(!hasDB())	return;
		try {
			statement = connection.createStatement();
			deleteBuilder = new StringBuilder();
			deleteBuilder.append("delete from ");
			deleteBuilder.append(table);
			
			sql = deleteBuilder.toString();
			sql += createWhere(deleteTerms);
			statement.executeUpdate(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAll(String table){
		if(!hasDB())	return;
		try {
			statement = connection.createStatement();
			sql = "delete from " + table;
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getColumn(){
		return this.column;
	}
	
	private String createWhere(HashMap<String , Object> terms){
		whereBuilder = new StringBuilder();
		whereBuilder.append(" where ");
		for (Map.Entry<String , Object> term : terms.entrySet()) {
			whereBuilder.append(term.getKey());
			whereBuilder.append(" = '");
			whereBuilder.append(term.getValue());
			whereBuilder.append("'");
		}
		return whereBuilder.toString();
	}
	
	private boolean hasDB(){
		if(connection == null)	return false;
		try {
			if(connection.isClosed())	return false;
			else						return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private ArrayList<String> getColumnList(String table){
		ArrayList<String> columnList = new ArrayList<>();
		try {
			statement = connection.createStatement();
			sql = "select * from " + table;
			ResultSet result = statement.executeQuery(sql);
			rsmd = result.getMetaData();
			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				columnList.add(rsmd.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnList;
	}
	
	private void setDBInfo(String configFile){
		try {
			DBInfo dbInfo = gson.fromJson(new FileReader(configFile) , DBInfo.class);
			this.host			= dbInfo.getHost();
			this.port			= dbInfo.getPort();
			this.DBName 		= dbInfo.getDBName();
			this.commandTable	= dbInfo.getcommandTable();
			this.logTable		= dbInfo.getlogTable();
			this.user			= dbInfo.getUser();
			this.password		= dbInfo.getPassword();
			address = "jdbc:mysql://" + host + ":" + port + "/" + DBName;
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getCommandTableName(){
		return commandTable;
	}
	
	public String getLogTableName(){
		return logTable;
	}
	
}
