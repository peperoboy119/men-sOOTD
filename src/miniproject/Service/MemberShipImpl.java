package miniproject.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import miniproject.Controller;
import miniproject.Member;
import miniproject.DAO.DatabaseService;
import miniproject.DAO.DatabaseServiceImpl;

public class MemberShipImpl implements MemberShip {
	final String[] txtFldIdArr = { "#userName", "#userID", "#userPw", "#userPwOk", "#phoneNum", "#email", "#addr1",
			"#addr2", "#addr3" };

	//회원가입 창 띄위기
	@Override
	public void openMember() {
		Stage membershipForm = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../Membership.fxml"));
		Parent parent = null;
		
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		membershipForm.setScene(new Scene(parent));

		Controller cntler = loader.getController();
		cntler.setMembershipForm(parent);

		membershipForm.show();

	}
	
	//회원가입 창에 있는 모든 텍스트필드 정보 받아오기
	private Map<String, TextField> getTextFieldInfo(Parent membershipForm) {
		Map<String, TextField> txtFldMap = new HashMap<String,TextField>();

		for (String txtFldId : txtFldIdArr) {
			TextField txtFld = (TextField) membershipForm.lookup(txtFldId);
			txtFldMap.put(txtFldId, txtFld);
			//아이디 : 아이디 텍스트필드
		}
		return txtFldMap;
	}

	//모든 텍스트필드가 비어있는지 아닌지 확인
	//아이디, 비밀번호 등 입력할 때 확인되어야 하는 부분들 여기서 체크
	//아이디의 길이, 비밀번호의 복잡성 등등
	private boolean IsTxtFld(Map<String, TextField> txtFldMap) {
		CommonService commonSrv = new CommonServiceImpl();
		
		for (String txtFldId : txtFldIdArr) {
			TextField txtFld = txtFldMap.get(txtFldId);
			if (txtFld.getText().isEmpty()) {
				commonSrv.ErrorMsg("회원가입 문제", "필수정보", "요구하는 내용을 기입하세요");
				txtFld.requestFocus();
				return true;
			}
		}
		
		TextField pwTxtFld = txtFldMap.get(txtFldIdArr[2]);
		TextField pwOkTxtFld = txtFldMap.get(txtFldIdArr[3]);
		String pw = pwTxtFld.getText();
		String pwOk = pwOkTxtFld.getText();

		TextField phoneNumTxtFld = txtFldMap.get(txtFldIdArr[4]);
		String phoneNum = phoneNumTxtFld.getText();

		TextField idTxtFld = txtFldMap.get(txtFldIdArr[1]);
		String id = idTxtFld.getText();

		String pwPattern = "(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,}$";
		// 비밀번호에 영문 대소문자, 숫자, 특수기호, 8자 이상의 조건
		String phoneNumPattern = "^[0-9]*$";
		//전화번호는 숫자만 할당 받도록
		String idPattern = "[a-zA-Z0-9]{6,}$";
		//아이디에는 영어 대소문자, 숫자로만 최소 6자 이상

		Matcher matcher = Pattern.compile(pwPattern).matcher(pw);
		Matcher matcher2 = Pattern.compile(idPattern).matcher(id);
		Matcher matcher3 = Pattern.compile(phoneNumPattern).matcher(phoneNum);
		
		//전화번호 부분
		if (!matcher3.matches()) {
			commonSrv.ErrorMsg("전화번호를 다시 입력하세요, 전화번호는 숫자만 사용 가능합니다.");
			phoneNumTxtFld.clear();
			phoneNumTxtFld.requestFocus();
			return true;
		}
		if (phoneNum.contains(" ")) {
			commonSrv.ErrorMsg("전화번호를 다시 입력해 주세요", "전화번호에는 공백이 들어갈 수 없습니다");
			phoneNumTxtFld.clear();
			phoneNumTxtFld.requestFocus();
			return true;
		}

		//아이디 부분
		if (!matcher2.matches()) {
			commonSrv.ErrorMsg("아이디를 다시 입력해 주세요", "아이디는 6자이상의 영문, 숫자조합으로 만들어야 합니다.");
			idTxtFld.clear();
			idTxtFld.requestFocus();
			return true;
		}
		if (id.contains(" ")) {
			commonSrv.ErrorMsg("아이디를 다시 입력해 주세요", "아이디에는 공백이 들어갈 수 없습니다");
			idTxtFld.clear();
			idTxtFld.requestFocus();
			return true;
		}

		//비밀번호 부분
		if (!matcher.matches()) {
			commonSrv.ErrorMsg("비밀번호를 다시 입력해 주세요", "비밀번호는 8자이상의 영문, 숫자, 특수문자 조합으로 만들어야 합니다.");
			pwTxtFld.clear();
			pwOkTxtFld.clear();
			pwTxtFld.requestFocus();
			return true;
		}
		if (pw.contains(" ")) {
			commonSrv.ErrorMsg("비밀번호를 다시 입력해 주세요", "비밀번호에는 공백이 들어갈 수 없습니다");
			pwTxtFld.clear();
			pwOkTxtFld.clear();
			pwTxtFld.requestFocus();
			return true;
		}
		if (!pw.equals(pwOk)) {
			commonSrv.ErrorMsg("비밀번호가 다릅니다", "다시 확인하세요");
			pwTxtFld.clear();
			pwOkTxtFld.clear();
			pwTxtFld.requestFocus();
			return true;
		}
		
		//모두 요구조건 Ok
		return false;
	}

	//나이
	private String getAge(Parent membershipForm) {
		RadioButton upAge = (RadioButton) membershipForm.lookup("#ageUp");
		if (upAge.isSelected()) {
			return "14세 이상";
		}
		return "14세 미만";
	}

	//입력된 모든 정보들을 Member로 저장
	private Member getMember(Parent membershipForm) {
		Map<String, TextField> txtFldMap = getTextFieldInfo(membershipForm);
		
		if (IsTxtFld(txtFldMap)) {
			return null;
		}

		Member member = new Member();
		member.setName(((TextField) membershipForm.lookup(txtFldIdArr[0])).getText());
		member.setId(((TextField) membershipForm.lookup(txtFldIdArr[1])).getText());
		member.setPw(((TextField) membershipForm.lookup(txtFldIdArr[2])).getText());
		member.setPhoneNum(((TextField) membershipForm.lookup(txtFldIdArr[4])).getText());
		member.setEmail(((TextField) membershipForm.lookup(txtFldIdArr[5])).getText());
		member.setAge(getAge(membershipForm));
		String address = (((TextField) membershipForm.lookup(txtFldIdArr[6])).getText()
				+ ((TextField) membershipForm.lookup(txtFldIdArr[7])).getText()
				+ ((TextField) membershipForm.lookup(txtFldIdArr[8])).getText());
		member.setAddress(address);

		return member;
	}

	//회원가입 하기
	@Override
	public void MembershipProc(Parent membershipForm) {
		Member member = getMember(membershipForm);
		CommonService commonServ = new CommonServiceImpl();
		LoginService loginServ = new LoginServiceImpl();
		
		if (member == null) {
			return;
		}

		DatabaseService dbServ = new DatabaseServiceImpl();
		dbServ.Open();
		
		if (dbServ.CheckId(member.getId()) == false) {
			//아이디가 중복되지 않음
			dbServ.Insert(member);
			commonServ.ErrorMsg("회원가입", "회원가입 성공", "회원가입 되셨습니다.");
			Stage stage = (Stage)membershipForm.getScene().getWindow();
			stage.close();
			loginServ.openLogin();
		} else {
			//아이디가 중복됨
			commonServ.ErrorMsg("회원가입 문제", "아이디 중복", "아이디가 중복되었습니다.");
			TextField textField = (TextField)membershipForm.lookup("#userID");
			textField.clear();
			textField.requestFocus();
			return;
		}
	}
	
}
