package ubi.project.a06;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PollingService extends Service{
	
	@Override
	protected Task createTask() {
		Task task = new Task(){

			@Override
			protected Object call() throws Exception {
				
				return null;
			}
			
		};
		return task;
	}

}
