package ubi.project.a06;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class PollingButtonEventListener implements EventHandler<ActionEvent>{
	private	FrameController				controller;
	private	ArrayList<PollingService>	pollingServiceArray;
	private	PollingService	pollingService;
	
	public PollingButtonEventListener(FrameController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(controller.isSelectedPollingButton()){
			controller.setPollingButton("Polling Stop");
			
			pollingServiceArray = new ArrayList<>(controller.getComboBoxSize());
			for(int i = 0; i < pollingServiceArray.size(); i++){
				pollingServiceArray.add(new PollingService(new HTTPGet(controller.getComboBoxItem(i))));
				pollingServiceArray.get(i).setPeriod(Duration.seconds(controller.getPollingInterval()));
				pollingServiceArray.get(i).start();
				pollingServiceArray.get(i).setOnScheduled(e -> {
					controller.setResultTextArea(pollingService.getLastValue());
				});
			}
			/*
			pollingService = new PollingService(new HTTPGet(controller.getComboBoxItem()));
			pollingService.setPeriod(Duration.seconds(controller.getPollingInterval()));
			pollingService.start();
			pollingService.setOnScheduled(e -> {
				controller.setResultTextArea(pollingService.getLastValue());
			});
			*/
		}else{
			controller.setPollingButton("Polling Start");
			for(int i = 0; i < pollingServiceArray.size(); i++){
				if(pollingServiceArray.get(i).isRunning()){
					pollingServiceArray.get(i).cancel();
				}
			}
			//if(pollingService.isRunning())	pollingService.cancel();
		}
	}

}
