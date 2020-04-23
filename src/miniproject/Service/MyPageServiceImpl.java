package miniproject.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import miniproject.Controller;
import miniproject.Member;
import miniproject.OrderItem;
import miniproject.DAO.DatabaseService;
import miniproject.DAO.DatabaseServiceImpl;

public class MyPageServiceImpl implements MyPageService {
	private DatabaseService dbServ = new DatabaseServiceImpl();
	private CommonService commonServ = new CommonServiceImpl();
	final String[] txtFldIdArr = {"#userPw", "#userPwOk", "#phoneNum", "#userEmail", "#userAddr"};
	
	//마이페이지2에 있는 모든 TextField값 가져오기
	private Map<String, TextField> getTextFieldInfo(Parent myPageForm2) {
		Map<String, TextField> txtFldMap = new HashMap<String,TextField>();

		for (String txtFldId : txtFldIdArr) {
			TextField txtFld = (TextField) myPageForm2.lookup(txtFldId);
			txtFldMap.put(txtFldId, txtFld);
		}
		return txtFldMap;
	}
	//모든 정보가 입력되었는지 확인
	private boolean IsTxtFld(Map<String, TextField> txtFldMap) {
		for (String txtFldId : txtFldIdArr) {
			TextField txtFld = txtFldMap.get(txtFldId);
			
			if (txtFld.getText().isEmpty()) {
				commonServ.ErrorMsg("회원수정 문제", "필수정보", "요구하는 내용을 기입하세요");
				txtFld.requestFocus();
				return true;
			}
		}
		
		TextField pwTxtFld = txtFldMap.get(txtFldIdArr[0]);
		TextField pwOkTxtFld = txtFldMap.get(txtFldIdArr[1]);
		String pw = pwTxtFld.getText();
		String pwOk = pwOkTxtFld.getText();
		System.out.println(pw);
		System.out.println(pwOk);
		
		TextField phoneNumTxtFld = txtFldMap.get(txtFldIdArr[2]);
		String phoneNum = phoneNumTxtFld.getText();
		
		String pwPattern = "(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,}$";
		// 비밀번호에 영문 대소문자, 숫자, 특수기호, 8자 이상의 조건
		String phoneNumPattern = "^[0-9]*$";
		// 전화번호는 숫자만 할당 받도록

		Matcher matcher = Pattern.compile(pwPattern).matcher(pw);
		Matcher matcher3 = Pattern.compile(phoneNumPattern).matcher(phoneNum);
		
		//전화번호 부분
		if (!matcher3.matches()) {
			commonServ.ErrorMsg("전화번호를 다시 입력하세요, 전화번호는 숫자만 사용 가능합니다.");
			phoneNumTxtFld.clear();
			phoneNumTxtFld.requestFocus();
			return true;
		}
		if (phoneNum.contains(" ")) {
			commonServ.ErrorMsg("전화번호를 다시 입력해 주세요", "전화번호에는 공백이 들어갈 수 없습니다");
			phoneNumTxtFld.clear();
			phoneNumTxtFld.requestFocus();
			return true;
		}
		
		//비밀번호 부분		
		if (!matcher.matches()) {
			commonServ.ErrorMsg("비밀번호를 다시 입력해 주세요", "비밀번호는 8자이상의 영문, 숫자, 특수문자 조합으로 만들어야 합니다.");
			pwTxtFld.clear();
			pwOkTxtFld.clear();
			pwTxtFld.requestFocus();
			return true;
		}
		if (pw.contains(" ")) {
			commonServ.ErrorMsg("비밀번호를 다시 입력해 주세요", "비밀번호에는 공백이 들어갈 수 없습니다");
			pwTxtFld.clear();
			pwOkTxtFld.clear();
			pwTxtFld.requestFocus();
			return true;
		}
		if (!pw.equals(pwOk)) {
			commonServ.ErrorMsg("비밀번호가 다릅니다", "다시 확인하세요");
			pwTxtFld.clear();
			pwOkTxtFld.clear();
			pwTxtFld.requestFocus();
			return true;
		}
		return false;
	}
	
	//회원정보 수정하기
	public void updateInfo(Parent myPageForm2) {
		Label userName = (Label)myPageForm2.lookup("#userName");
		Label userId = (Label)myPageForm2.lookup("#userId");
		Label userAge = (Label)myPageForm2.lookup("#userAge");
		
		TextField userPw = (TextField)myPageForm2.lookup("#userPw");
		TextField userPwOk = (TextField)myPageForm2.lookup("#userPwOk");
		TextField phoneNum = (TextField)myPageForm2.lookup("#phoneNum");
		TextField userEmail = (TextField)myPageForm2.lookup("#userEmail");
		TextField userAddr = (TextField)myPageForm2.lookup("#userAddr");
		
		Map<String, TextField> txtFieldMap = getTextFieldInfo(myPageForm2);
		boolean check = IsTxtFld(txtFieldMap);
		
		if (check == true) {
			return;
		}
		
		dbServ.Open();
		Member member = new Member();
		member.setId(userId.getText());
		member.setPw(userPw.getText());
		member.setPhoneNum(phoneNum.getText());
		member.setEmail(userEmail.getText());
		member.setAddress(userAddr.getText());
		dbServ.UpdateMemInfo(member);
		commonServ.ErrorMsg("회원정보 수정", "회원정보 수정 성공", "모든 정보가 수정되었습니다.");
	}

	//로그인상태 확인하기
	//회원만 마이페이지를 열 수 있도록
	@Override
    public boolean checkLogin(Parent mainForm) {
	    String id = ((Button)mainForm.lookup("#loginbtn")).getText();
	    if (id.equals("비회원") || id.equals("로그인")) {
		    return false;
	    }else {
	    	return true;
	    }
	}
	
	//마이페이지1 띄우기
	@Override
	public void openMyPage() {
		Stage myPageForm = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../MyPage.fxml"));
		Parent parent = null;
		
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myPageForm.setScene(new Scene(parent));
		Controller cntler = loader.getController();
		cntler.setMyPageForm(parent);
		myPageForm.show();	
		
		displayAllInfo(parent);
	}
	
	//회원 정보, 구매 정보 띄우기
	private void displayAllInfo(Parent parent) {
		dbServ.Open();
		String id = dbServ.CheckLoginId().getId();
				
		Label idLabel = (Label)parent.lookup("#userId");
		idLabel.setText(id);
		
		OrderItem orderItem;
		ArrayList<Label> itemNames = new ArrayList<Label>();
		ArrayList<Label> itemPrices = new ArrayList<Label>();
		ArrayList<Label> itemQtys = new ArrayList<Label>();

		Label itemName1 = (Label)parent.lookup("#name1");
		Label itemName2 = (Label)parent.lookup("#name2");
		Label itemPrice1 = (Label)parent.lookup("#price1");
		Label itemPrice2 = (Label)parent.lookup("#price2");
		Label itemQty1 = (Label)parent.lookup("#qty1");
		Label itemQty2 = (Label)parent.lookup("#qty2");
		
		itemNames.add(itemName1);
		itemNames.add(itemName2);
		itemPrices.add(itemPrice1);
		itemPrices.add(itemPrice2);
		itemQtys.add(itemQty1);
		itemQtys.add(itemQty2);
		
		List<OrderItem> userOrders = dbServ.GetUserOrder(id);
		int count = 1;
		if (!userOrders.isEmpty()) {
			if(count == userOrders.size()){
				for (int i= 0; i < userOrders.size(); i++) {
					orderItem = new OrderItem();
					orderItem = (OrderItem)userOrders.get(i);
					itemNames.get(i).setText(orderItem.getName());
					itemPrices.get(i).setText(orderItem.getPrice());
					itemQtys.get(i).setText(orderItem.getQty());
				}
			}else if(count < userOrders.size()) {
				for (int i= 0; i < 2; i++) {
					orderItem = new OrderItem();
					orderItem = (OrderItem)userOrders.get(i);
					itemNames.get(i).setText(orderItem.getName());
					itemPrices.get(i).setText(orderItem.getPrice());
					itemQtys.get(i).setText(orderItem.getQty());
				}
			}			
		}else {
			for (int i = 0; i < 2; i++) {
				itemNames.get(i).setText("");
				itemPrices.get(i).setText("");
				itemQtys.get(i).setText("");
			}
		}
		
		OrderItem num = dbServ.SelectOrderStatus("주문완료", id);
		OrderItem num1 = dbServ.SelectOrderStatus("배송준비중", id);
		OrderItem num2 = dbServ.SelectOrderStatus("배송중", id);
		OrderItem num3 = dbServ.SelectOrderStatus("배송완료", id);
		Label orderFinished = (Label)parent.lookup("#orderFinished");
		Label readyStatus = (Label)parent.lookup("#readyStatus");
		Label sentStatus = (Label)parent.lookup("#sentStatus");
		Label FinishedStatus = (Label)parent.lookup("#FinishedStatus");
		
		if ((num == null)) {
			orderFinished.setText("0");
		}else {
			orderFinished.setText(num.getItemCount());
		}
		if (num1 == null) {
			readyStatus.setText("0");
		}else {
			readyStatus.setText(num1.getItemCount());
		}
		if (num2 == null) {
			sentStatus.setText("0");
		}else {
			sentStatus.setText(num2.getItemCount());
		}
		if (num3 == null) {
			FinishedStatus.setText("0");
		}else {
			FinishedStatus.setText(num3.getItemCount());
		}
	}

	//마이페이지2 띄우기
	@Override
	public void openMyPage2() {
		Stage myPageForm2 = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../MyPage2.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myPageForm2.setScene(new Scene(parent));
		Controller cntler = loader.getController();
		cntler.setMyPageForm2(parent);
		myPageForm2.show();		
		
		displayMemInfo(parent);
	}
	
	//회원 정보 띄우기
	private void displayMemInfo(Parent parent) {
		dbServ.Open();
		String id = dbServ.CheckLoginId().getId();
		
		Label userName = (Label)parent.lookup("#userName");
		Label userId = (Label)parent.lookup("#userId");
		Label userAge = (Label)parent.lookup("#userAge");
		
		TextField phoneNum = (TextField)parent.lookup("#phoneNum");
		TextField userEmail = (TextField)parent.lookup("#userEmail");
		TextField userAddr = (TextField)parent.lookup("#userAddr");
		
		Member mem = dbServ.SelectAll(id);
		userId.setText(mem.getId());
		userName.setText(mem.getName());
		
		if (mem.getAge().equals("UP")) {
			userAge.setText("14세 이상");
		}else if (mem.getAge().equals("DOWN")) {
			userAge.setText("14세 미만");
		}else {
			userAge.setText(mem.getAge());
		}
		
		phoneNum.setText(mem.getPhoneNum());
		userEmail.setText(mem.getEmail());
		userAddr.setText(mem.getAddress());
	}
}
