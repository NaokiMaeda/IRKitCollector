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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
	private			ArrayList<String>					inquiryCulums;
	private			HashMap<String , Object>			inquiryTerms;
	private			LinkedHashMap<String , Object>		selectResult;
	
	//現在時刻取得
	private			SimpleDateFormat		sdf;
	private			Date					date;
	
	private			ByteArrayInputStream	bais;
	private			ObjectInputStream		ois;
	private			ByteArrayOutputStream	baos;
	private			ObjectOutputStream		oos;
	private			byte[]					byteData;
	private			int[]					intData;
	
	public PollingService(HTTPGet httpGet) {
		this.httpGet = httpGet;
		gson 		= new Gson();					//JSON改行無し
		//gson		= new GsonBuilder().setPrettyPrinting().create();	//JSON改行有り
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
				//recode.put("ir_signal" , response);
				
				inquiryCulums	= new ArrayList<>();
				inquiryTerms	= new HashMap<>();
				inquiryCulums.add("command_name");
				inquiryTerms.put("ir_signal" , response);
				selectResult =  commandDB.select(inquiryCulums,inquiryTerms);
				for(int i = 0; i < selectResult.size(); i++){
					System.out.println(selectResult.get("command_name"));
				}
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
				SaveJSON(Arrays.toString(message.getData()));
				///logDB.insert(recode);
				///return response;
				return response;
			}
		};
		return task;
	}
	
	private String getCurrentDate(){
		sdf		= new SimpleDateFormat(DATE_FORMAT);
		date	= new Date(System.currentTimeMillis());	
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
		BufferedWriter bw = null;
		GetMessage getMessage = gson.fromJson(saveData , GetMessage.class);
		String data = gson.toJson(getMessage);
		try {
			//File file = new File(getCurrentDate() + ".json");
			File file = new File(getCurrentDate() + ".txt");
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(data);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
