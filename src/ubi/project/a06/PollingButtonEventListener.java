package ubi.project.a06;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class PollingButtonEventListener implements EventHandler<ActionEvent>{
	private	FrameController	controller;
	private	PollingService	pollingService;
	
	public PollingButtonEventListener(FrameController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(controller.getPollingButton()){
			controller.setPollingButton("Polling Stop");
			
			pollingService = new PollingService(new HTTPGet(controller.getComboBoxItem()));
			pollingService.setPeriod(Duration.seconds(controller.getPollingInterval()));
			pollingService.start();
			pollingService.setOnScheduled(e -> {
				//resultTextArea.appendText(pollingService.getLastValue());
				System.out.println(pollingService.getLastValue());
			});
		}else{
			controller.setPollingButton("Polling Start");
			if(pollingService.isRunning())	pollingService.cancel();
		}
	}

}
