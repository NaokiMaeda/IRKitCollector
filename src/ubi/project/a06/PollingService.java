package ubi.project.a06;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PollingService extends Service{
	private	long	pollingInterval;
	private HTTPGet	httpGet;
	
	public PollingService(HTTPGet httpGet , double pollingInterval) {
		this.pollingInterval = (long)pollingInterval * 1000;
		this.httpGet = httpGet;
	}
	
	@Override
	protected Task createTask() {
		Task<Void> task = new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				httpGet.getMessage();
				System.out.println("Get");
				Thread.sleep(pollingInterval);
				return null;
			}
		};
		return task;
	}

}
