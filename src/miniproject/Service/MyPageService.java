package miniproject.Service;

import javafx.scene.Parent;

public interface MyPageService {
	public void updateInfo(Parent myPageForm2);		//회원 정보 수정하기
	public boolean checkLogin(Parent mainForm);		//로그인 상태인지 확인하기
	public void openMyPage();						//마이페이지 1 열기
	public void openMyPage2();						//마이페이지 2 열기
}
