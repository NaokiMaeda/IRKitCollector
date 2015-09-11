package ubi.project.a06;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

public class HTTPPost {
	private	HttpTransport	httpTransport;
	private	Gson			gson;
	private	String			HOST;
	
	public HTTPPost(String HOST){
		this.gson = new Gson();
		this.HOST = HOST;
		httpTransport = new NetHttpTransport();
	}
	
	public void post(String msg) throws IOException{
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + HOST);
		HttpRequest request = requestFactory.buildPostRequest(url , 
				new InputStreamContent("text/plain" , new ByteArrayInputStream(msg.getBytes())));
		
		HttpResponse response = request.execute();
		try{
			System.out.println(response.parseAsString());
		}finally{
			response.disconnect();
		}
		httpTransport.shutdown();
	}
	
	public void post(Map<String , ?> map)throws IOException{
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + HOST);
		StringBuilder builder = new StringBuilder();
		
		for(String key : map.keySet()){
			builder.append(key + "=" + map.get(key) + "&");
		}
		builder.deleteCharAt(builder.length() - 1);
		
		HttpRequest request = requestFactory.buildPostRequest(url ,
				new InputStreamContent("text/plain" , new ByteArrayInputStream(builder.toString().getBytes())));
		
		HttpResponse response = request.execute();
		try{
			System.out.println(response.parseAsString());
		}finally{
			response.disconnect();
		}
		httpTransport.shutdown();
	}
	
	public void postJSON(Map<String , ?> map) throws IOException{
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + HOST);
		HttpRequest request = requestFactory.buildPostRequest(url , ByteArrayContent.fromString("application/json", gson.toJson(map)));
		HttpResponse response = request.execute();
		try{
			System.out.println(response.parseAsString());
		}finally{
			response.disconnect();
		}
		httpTransport.shutdown();
	}
}
