package miniproject.Service;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import miniproject.Controller;
import miniproject.DAO.DatabaseService;
import miniproject.DAO.DatabaseServiceImpl;

public class LoginServiceImpl implements LoginService {
	private DatabaseService dbServ = new DatabaseServiceImpl();
	private CommonService commonServ = new CommonServiceImpl();

	//로그인 창 띄우기
	@Override
	public void openLogin() {
		Stage loginForm = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../Login.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loginForm.setScene(new Scene(parent));

		Controller cntler = loader.getController();
		cntler.setLoginForm(parent);

		loginForm.show();
	}
	
	//로그인 하기
	public void loginProc(Parent root) {
      TextField userId = (TextField)root.lookup("#userID");
      TextField userPw = (TextField)root.lookup("#userPw");
      dbServ.Open();
      
      if(dbServ.Select(userId.getText(), userPw.getText())) {
         commonServ.ErrorMsg("로그인 성공", "로그인 성공", "홈페이지 메인 화면으로 돌아갑니다.");
         String id = ((TextField)root.lookup("#userID")).getText();
         dbServ.InsertLogIn(id);
         dbServ.UpdateStatus();
         
         commonServ.AutoClose(root);
         commonServ.openMain();
         
      }else {
         commonServ.ErrorMsg("로그인 실패", "로그인 실패", "아이디, 패스워드가 일치하지 않습니다.");
         userId.clear();
         userPw.clear();
         userId.requestFocus();
      }
	}
	
	//로그아웃 하기
	@Override
	public void logoutProc(Parent parent) {
		dbServ.Open();
		Button loginbtn = (Button)parent.lookup("#logoutbtn");
		dbServ.ClearStatus();
		commonServ.ErrorMsg("로그아웃", "로그아웃", "무사히 로그아웃 되셨습니다");
	}	
}
