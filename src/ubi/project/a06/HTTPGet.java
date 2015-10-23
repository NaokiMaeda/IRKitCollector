package ubi.project.a06;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
	private	String 			host;
	private	String			ipAddress;
	private	String			hostName;
	
	public HTTPGet(String host){
		this.host 		= host;
		gson 			= new Gson();
		httpTransport	= new NetHttpTransport();
		try {
			InetAddress ia	= InetAddress.getByName(host);
			ipAddress = ia.getHostAddress();
			hostName = ia.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void get() throws IOException{
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://" + host);
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
		GenericUrl url = new GenericUrl("http://" + host + "/" + param);
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
	
	public String gethostName(){
		if(hostName == null || hostName.length() == 0)	return null;
		return hostName;
	}
	
	public String getIPAddress(){
		if(ipAddress == null || ipAddress.length() == 0)	return null;
		return ipAddress;
	}
	
}