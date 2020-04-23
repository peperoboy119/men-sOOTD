package miniproject.Service;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import miniproject.Controller;
import miniproject.DAO.DatabaseService;
import miniproject.DAO.DatabaseServiceImpl;

public class CommonServiceImpl implements CommonService {
	private DatabaseService dbServ = new DatabaseServiceImpl();
	
	//창 닫기
	public void WindowClose(ActionEvent event) {
		Parent parent = (Parent) event.getSource();
		Stage stage = (Stage) parent.getScene().getWindow();
		stage.close();
	}
	//자동으로 창 닫기
	public void AutoClose(Parent parent) {
		Stage stage = (Stage)parent.getScene().getWindow();
		stage.close();
	}
	
	//MenuItem을 클릭시 어떤 카테고리가 눌렸는지 그 아이디값을  전달
	public String MenuItemGetText(ActionEvent event) {
		String menu = ((MenuItem)event.getSource()).getText();
		return menu;
	}
	
	//버튼 클릭시 어떤 상품의 버튼인지 아이디 전달
	public String ButtonGetText(ActionEvent event) {
		String menu = ((Button)event.getSource()).getText();
		return menu;
	}
			
	//경고창 띄우기
	@Override
	public void ErrorMsg(String title, String headerStr, String contentTxt) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerStr);
		alert.setContentText(contentTxt);
		alert.showAndWait();
	}
	public void ErrorMsg(String headerStr, String contentTxt) {
		ErrorMsg("에러", headerStr, contentTxt);
	}
	public void ErrorMsg(String contentTxt) {
		ErrorMsg("에러", "문제가 발생했습니다.", contentTxt);
	}
	
	//메인 홈페이지 띄우기
	@Override
	public void openMain() {
		Stage MainForm = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../Main.fxml"));
		Parent parent = null;
		
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MainForm.setScene(new Scene(parent));
		MainForm.setTitle("Main");
		
		Controller ctrler = loader.getController();
		ctrler.setMainForm(parent);
		MainForm.show();
		
		//메인 화면을 띄울 때 로그인 상태면 회원의 아이디 값을 
		//로그인된 상태가 아니면 비회원이라고
		//버튼에 표시
		dbServ.Open();
		Button loginbtn = (Button)parent.lookup("#loginbtn");
		String id = dbServ.CheckLoginId().getId();
		if (id == null) {
			loginbtn.setText("비회원");
		} else {
			loginbtn.setText(id);
			loginbtn.setDisable(true);
		}
	}
}
