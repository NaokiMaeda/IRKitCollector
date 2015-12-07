package ubi.project.a06;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import ubi.project.a06.message.GetMessage;
import ubi.project.a06.mysql.MySQL;

public class PollingService extends ScheduledService<String>{
	private	final	String	DATE_FORMAT			= "yyyy_MM_dd__HH_mm_ss";
	private	final	String	logConfigFile		= "LogDBInfo.json";
	private final	String	commandConfigFile	= "commandDBInfo.json";
	
	private			Gson	gson;
	private			HTTPGet	httpGet;
	private			MySQL	logDB;
	private			MySQL	commandDB;
	
	private			GetMessage							message;
	private			HashMap<String , Object>			recode;
	private			ArrayList<HashMap<String , Object>>	selectResult;
	
	private			ByteArrayInputStream	bais;
	private			ObjectInputStream		ois;
	private			ByteArrayOutputStream	baos;
	private			ObjectOutputStream		oos;
	private			byte[]					byteData;
	private			int[]					intData;
	
	public PollingService(HTTPGet httpGet) {
		this.httpGet = httpGet;
		gson = new Gson();										//JSON改行無し
		logDB		= new MySQL(logConfigFile);
		commandDB	= new MySQL(commandConfigFile);
		logDB.ConnectionDB();
		commandDB.ConnectionDB();
	}
	
	@Override
	protected Task<String> createTask() {
		Task<String> task = new Task<String>(){
			@Override
			protected String call() throws Exception {
				String response = httpGet.get("messages");
				if(response.length() == 0)	return "";
				GetMessage message = gson.fromJson(response , GetMessage.class);
				recode = new HashMap<>();
				recode.put("time" , new Date());
				recode.put("ir_signal" , response);
				
				/*
				selectResult = commandDB.select(sql);
				for(int i = 0; i < selectResult.size(); i++){
					HashMap<String , Object> result = selectResult.get(i);
					System.out.println(result.get("command_name").toString());
				}
				*/
				//recode.put("format" , message.getFormat());
				//recode.put("freq" , message.getFreq());
				//recode.put("data" , convertByteArray(message.getData()));
				//SaveJSON(response);
				///logDB.insert(recode);
				///return response;
				return "";
			}
		};
		return task;
	}
	
	private String getCurrentDate(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
	
	private byte[] convertByteArray(int[] data){
		try {
			baos	= new ByteArrayOutputStream();
			oos		= new ObjectOutputStream(baos);
			oos.writeObject(data);
			byteData = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteData;
	}
	
	private int[] convertIntArray(byte[] data){
		try {
			bais	= new ByteArrayInputStream(data);
			ois		= new ObjectInputStream(bais);
			intData	= (int[])ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return intData;
	}
	
	private void SaveJSON(String saveData){
		if(saveData == null)	return;
		GetMessage getMessage = gson.fromJson(saveData , GetMessage.class);
		String data = gson.toJson(getMessage);
		try {
			File file = new File(getCurrentDate() + ".json");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(data);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
