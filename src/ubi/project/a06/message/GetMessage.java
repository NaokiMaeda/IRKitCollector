package ubi.project.a06.message;

public class GetMessage{
	public String	format;
	public int		freq;
	public int[]	data;
	
	public GetMessage(String format , int freq , int[] data){
		this.format = format;
		this.freq	= freq;
		this.data	= data;
	}
	
}
