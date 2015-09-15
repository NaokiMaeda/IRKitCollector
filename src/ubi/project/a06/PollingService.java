package ubi.project.a06;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class PollingService extends ScheduledService<String>{
	private HTTPGet	httpGet;
	
	public PollingService(HTTPGet httpGet) {
		this.httpGet = httpGet;
	}
	
	@Override
	protected Task createTask() {
		Task<String> task = new Task<String>(){
			@Override
			protected String call() throws Exception {
				String msg = httpGet.get("");
				//System.out.println("Task Call");
				return msg;
			}
		};
		return task;
	}

}
