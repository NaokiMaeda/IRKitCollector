package ubi.project.a06;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PollingService extends Service{
	private HTTPGet	httpGet;
	
	public PollingService(HTTPGet httpGet) {
		this.httpGet = httpGet;
	}
	
	@Override
	protected Task createTask() {
		Task<Void> task = new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				httpGet.getMessage();
				return null;
			}
		};
		return task;
	}

}
