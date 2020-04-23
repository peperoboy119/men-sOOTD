package miniproject.Service;

import javafx.scene.Parent;

public interface LoginService {
	public void openLogin(); 				 //로그인 창 띄우기
	public void loginProc(Parent loginForm); //로그인 하기
	public void logoutProc(Parent parent); 	 //로그아웃 하기
}
