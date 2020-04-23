package miniproject.Service;

import javafx.event.ActionEvent;
import javafx.scene.Parent;

public interface CommonService {
	//창 닫기
	public void WindowClose(ActionEvent event);
	public void AutoClose(Parent parent);
	
	//상품 아이디 갖고오기
	public String MenuItemGetText(ActionEvent event);
	public String ButtonGetText(ActionEvent event);
	
	//경고창 띄우기
	public void ErrorMsg(String title, String headerStr, String contentTxt);
	public void ErrorMsg(String headerStr, String contentTxt);
	public void ErrorMsg(String contentTxt);
	
	//메인 페이지 띄우기
	public void openMain();


}
