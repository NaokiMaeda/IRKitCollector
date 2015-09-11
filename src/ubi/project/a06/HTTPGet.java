package ubi.project.a06;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class HTTPGet {
	private	HttpTransport httpTransport;
	private	String HOST;
	
	public HTTPGet(String HOST){
		this.HOST = HOST;
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
	
	public void get(String param){
		httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + HOST + "/" + param);
	}
	
}