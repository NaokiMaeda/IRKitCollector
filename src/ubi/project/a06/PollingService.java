package ubi.project.a06;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class PollingService extends ScheduledService<Void>{
	private HTTPGet	httpGet;
	
	public PollingService(HTTPGet httpGet) {
		this.httpGet = httpGet;
	}
	
	@Override
	protected Task createTask() {
		Task<Void> task = new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				//httpGet.get();
				System.out.println("Task Call");
				return null;
			}
		};
		return task;
	}

}
