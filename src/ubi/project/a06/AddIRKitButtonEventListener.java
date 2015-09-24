package ubi.project.a06;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AddIRKitButtonEventListener implements EventHandler<ActionEvent>{
	private FrameController	controller;
	
	public AddIRKitButtonEventListener(FrameController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(ActionEvent event) {
		controller.setComboBoxItem(controller.getAddIRKitText());
	}

}
