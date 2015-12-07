package ubi.project.a06;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class FrameController implements Initializable{
	@FXML	private	Button					addIRKitButton;			//IRKit追加ボタン
	@FXML	private	ComboBox<String>		targetIRKitComboBox;	//既知のIRKitを選択するComboBox
	@FXML	private	Slider					pollingIntervalSlider;	//ポーリング間隔入力
	@FXML	private	TextArea				statusTextArea;			//クライアント状態表示欄
	@FXML	private	TextArea				resultTextArea;			//通信結果表示欄
	@FXML	private	TextField				addIRKitTextField;		//IRKitを追加する場合のIPアドレス記入欄
	@FXML	private	ToggleButton			pollingButton;			//ポーリング開始・停止ボタン
	
			private	ObservableList<String>	targetIRKitList;		//ComboBoxのアイテムリスト
			
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		try {
			targetIRKitList = FXCollections.observableArrayList();
			targetIRKitComboBox.setItems(targetIRKitList);
			
			pollingButton.setDisable(true);
			
			pollingButton.setOnAction(new PollingButtonEventListener(this));
			addIRKitButton.setOnAction(new AddIRKitButtonEventListener(this));
			addIRKitTextField.setOnKeyPressed(new AddIRKitTextEventListener(this));
			
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
	
	public String getComboBoxItem(int x){
		return targetIRKitComboBox.getItems().get(x);
	}
	
	public Integer getComboBoxSize(){
		return targetIRKitComboBox.getItems().size();
	}
	
	public Boolean isSelectedPollingButton(){
		return pollingButton.isSelected();
	}
	
	public double getPollingInterval(){
		return pollingIntervalSlider.getValue();
	}
	
	public void setComboBoxItem(String item){
		if(targetIRKitList.contains(item))	return;
		targetIRKitList.add(item);
		if(targetIRKitList.size() != 0){
			pollingButton.setDisable(false);
			targetIRKitComboBox.getSelectionModel().select(targetIRKitList.size() - 1);
		}
	}
	
	public void setPollingButton(String msg){
		pollingButton.setText(msg);
	}
	
	public void setResultTextArea(String msg){
		resultTextArea.appendText(msg);
	}
	
}
