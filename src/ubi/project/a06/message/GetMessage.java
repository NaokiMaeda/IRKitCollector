package ubi.project.a06.message;

public class GetMessage{
	private	String	format;
	private	int		freq;
	private	int[]	data;
	
	public GetMessage(String format , int freq , int[] data){
		this.format = format;
		this.freq	= freq;
		this.data	= data;
	}
	
	public String getFormat(){
		return format;
	}
	
	public int getFreq(){
		return freq;
	}
	
	public int[] getData(){
		return data;
	}
	
}
