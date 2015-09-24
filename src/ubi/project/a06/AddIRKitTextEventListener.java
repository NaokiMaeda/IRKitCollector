package ubi.project.a06;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AddIRKitTextEventListener implements EventHandler<KeyEvent>{
	private FrameController controller;
	
	public AddIRKitTextEventListener(FrameController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ENTER)){		//TextFieldでEnterキーを押した場合
			controller.setComboBoxItem(controller.getAddIRKitText());
		}
	}

}
