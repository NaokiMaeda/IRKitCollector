package ubi.project.a06;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import ubi.project.a06.message.GetMessage;

public class PollingService extends ScheduledService<String>{
	private	final	String	DATE_FORMAT = "yyyy_MM_dd__HH_mm_ss";
	private			Gson	gson;
	private			HTTPGet	httpGet;
	
	public PollingService(HTTPGet httpGet) {
		this.httpGet = httpGet;
		gson = new Gson();
	}
	
	@Override
	protected Task<String> createTask() {
		Task<String> task = new Task<String>(){
			@Override
			protected String call() throws Exception {
				String response = httpGet.get("messages");
				System.out.println(response);
				SaveJSON(response);
				return response;
			}
		};
		return task;
	}
	
	private String getCurrentDate(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
	
	private void SaveJSON(String saveData){
		GetMessage getMessage = gson.fromJson(saveData , GetMessage.class);
		String data = gson.toJson(getMessage);
		try {
			File file = new File(getCurrentDate() + ".json");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(data);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
