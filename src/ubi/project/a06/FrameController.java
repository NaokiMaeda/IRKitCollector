package ubi.project.a06;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FrameController implements Initializable{
	@FXML		private	Button							addIRKitButton;				//IRKit追加ボタン
	@FXML		private	ComboBox<Object>	targetIRKitComboBox;	//既知のIRKitを選択するComboBox
	@FXML		private	TextArea						statusTextArea;				
	@FXML		private	TextField						addIRKitTextField;			//IRKitを追加する場合のIPアドレス記入欄

	@Override
	public void initialize(URL location, ResourceBundle resources){
		try {
			InetAddress address = InetAddress.getLocalHost();
			statusTextArea.setText(address.getHostAddress().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private String getAddIRKitText(){
		String msg = addIRKitTextField.getText();
		addIRKitTextField.clear();
		return msg;
	}
	
	@FXML
	public void setOnKeyPressed(KeyEvent event){
		if(event.getCode().equals(KeyCode.ENTER)){		//TextFieldでEnterキーを押した場合
			System.out.println(getAddIRKitText());
		}
	}

	
}
