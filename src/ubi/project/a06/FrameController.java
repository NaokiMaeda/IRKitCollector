package ubi.project.a06;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class FrameController implements Initializable{
	@FXML	private	Button					addIRKitButton;			//IRKit追加ボタン
	@FXML	private	ComboBox<String>		targetIRKitComboBox;	//既知のIRKitを選択するComboBox
	@FXML	private	Slider					pollingIntervalSlider;	//ポーリング間隔入力
	@FXML	private	TextArea				statusTextArea;			//通信結果表示欄
	@FXML	private	TextField				addIRKitTextField;		//IRKitを追加する場合のIPアドレス記入欄
	@FXML	private	ToggleButton			pollingButton;			//ポーリング開始・停止ボタン
	
			private	ObservableList<String>	targetIRKitList;		//ComboBoxのアイテムリスト
			private	PollingService			pollingService;
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		try {
			targetIRKitList = FXCollections.observableArrayList();
			targetIRKitComboBox.setItems(targetIRKitList);
			
			pollingButton.setDisable(true);
			
			InetAddress address = InetAddress.getLocalHost();
			statusTextArea.setText(address.getHostAddress().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public String getAddIRKitText(){
		String msg = addIRKitTextField.getText();
		addIRKitTextField.clear();
		return msg;
	}
	
	public String getComboBoxItem(){
		return targetIRKitComboBox.getValue();
	}
	
	public double getPollingInterval(){
		return pollingIntervalSlider.getValue();
	}
	
	private void setComboBoxItem(String item){
		targetIRKitList.add(item);
		if(targetIRKitList.size() != 0){
			pollingButton.setDisable(false);
			targetIRKitComboBox.getSelectionModel().select(targetIRKitList.size() - 1);
		}
	}
	
	@FXML
	private void addEnterEventHandler(KeyEvent event){
		if(event.getCode().equals(KeyCode.ENTER)){		//TextFieldでEnterキーを押した場合
			setComboBoxItem(getAddIRKitText());
		}
	}
	
	@FXML
	private void ClickEventHandler(ActionEvent e){
		setComboBoxItem(getAddIRKitText());
	}
	
	@FXML
	private void changePollingEventHandler(ActionEvent event){
		if(pollingButton.isSelected()){
			pollingButton.setText("Polling Stop");
			pollingService = new PollingService(new HTTPGet(getComboBoxItem()));
			pollingService.setPeriod(Duration.seconds(getPollingInterval()));
			pollingService.start();
		}else{
			pollingButton.setText("Polling Start");
			pollingService.cancel();
		}
	}
	
}
