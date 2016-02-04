package ubi.project.a06.mysql;

public class DBInfo {
	private String	host;
	private	int		port;
	private String	DBName;
	private String	commandTable;
	private	String	logTable;
	private String	user;
	private String	password;
	
	public DBInfo(){
		
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void setDBName(String DBName){
		this.DBName = DBName;
	}
	
	public void setcommandTable(String commandTable){
		this.commandTable = commandTable;
	}
	
	public void setlogTable(String logTable){
		this.logTable = logTable;
	}
	
	public void setUser(String user){
		this.user = user;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getHost(){
		return this.host;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public String getDBName(){
		return this.DBName;
	}
	
	public String getcommandTable(){
		return this.commandTable;
	}
	
	public String getlogTable(){
		return this.logTable;
	}
	
	public String getUser(){
		return this.user;
	}
	
	public String getPassword(){
		return this.password;
	}

}
