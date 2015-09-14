package ubi.project.a06;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

public class HTTPGet {
	private	Gson			gson;
	private	HttpTransport	httpTransport;
	private	String 			HOST;
	
	public HTTPGet(String HOST){
		this.HOST = HOST;
		gson = new Gson();
		httpTransport = new NetHttpTransport();
	}
	
	public void get() throws IOException{
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + HOST);
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse response = request.execute();
		
		try{
			System.out.println(response.getStatusCode());
			System.out.println(response.getContentType());
			System.out.println(response.parseAsString());
		}finally{
			response.disconnect();
		}
		httpTransport.shutdown();
	}
	
	public String get(String param) throws IOException{
		String responseData = "";
		httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + HOST + "/" + param);
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse response = request.execute();
		
		try{
			responseData = response.parseAsString();
		}finally{
			response.disconnect();
		}
		httpTransport.shutdown();
		return responseData;
	}
	
	public void getMessage() throws IOException{
		String responseData = get("message");
		//GetMessage getMessage = gson.fromJson(get("message") , GetMessage.class);
		File file = new File(getCurrentTime() + ".json");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		pw.print(responseData);
		pw.close();
	}
	
	private String getCurrentTime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
		return sdf.format(date);
	}
	
}