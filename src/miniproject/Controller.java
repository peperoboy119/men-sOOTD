package miniproject;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import miniproject.Service.CommonService;
import miniproject.Service.CommonServiceImpl;
import miniproject.Service.LoginService;
import miniproject.Service.LoginServiceImpl;
import miniproject.Service.MemberShip;
import miniproject.Service.MemberShipImpl;
import miniproject.Service.MyPageService;
import miniproject.Service.MyPageServiceImpl;
import miniproject.Service.Order;
import miniproject.Service.OrderImpl;

public class Controller implements Initializable {
	private LoginService loginServ;
	private CommonService commonServ;
	private MemberShip membershipServ;
	private Order orderServ;
	private MyPageService myPageServ;
	
	private Parent mainForm;
	private Parent detailForm1;
	private Parent membershipForm;
	private Parent loginForm;
	private Member member;
	private Parent detailForm2;
	private Parent orderForm;
	private Parent orderDetailForm;
	private Parent myPageForm;
	private Parent myPageForm2;
	
	@FXML private Button loginbtn;
	@FXML private Button mini;
	@FXML private MenuButton topMenu;

	public void setMainForm(Parent mainForm) {
		this.mainForm = mainForm;
	}
	public void setLoginForm(Parent loginForm) {
		this.loginForm = loginForm;
	}
	public void setMembershipForm(Parent membershipForm) {
		this.membershipForm = membershipForm;
	}
	public void setDetailForm1(Parent detailForm1) {
		this.detailForm1 = detailForm1;
	}
	public void setDetailForm2(Parent detailForm2) {
		this.detailForm2 = detailForm2;
	}
	public void setOrderForm(Parent orderForm) {
		this.orderForm = orderForm;
	}
	public void setOrderDetailForm(Parent orderDetailForm) {
		this.orderDetailForm = orderDetailForm;
	}
	public void setMyPageForm(Parent myPageForm) {
		this.myPageForm = myPageForm;
	}
	public void setMyPageForm2(Parent myPageForm2) {
		this.myPageForm2 = myPageForm2;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginServ = new LoginServiceImpl();
		commonServ = new CommonServiceImpl();
		membershipServ = new MemberShipImpl();
		orderServ = new OrderImpl();
		member = new Member();
		myPageServ = new MyPageServiceImpl();
	}
	
   //회원가입
   public void MemberShipProc() {
	   membershipServ.MembershipProc(membershipForm);
   }
	
	//로그인
	public void openLogin(ActionEvent event) {
		commonServ.WindowClose(event);
		loginServ.openLogin();
	}
	public void LoginProc() {
		loginServ.loginProc(loginForm);
	}
	
	//로그아웃
    public void logoutProc(ActionEvent event) {
      loginServ.logoutProc(orderDetailForm);
      this.cancelProc(event);
	}
	
	//회원가입
	public void openMember(ActionEvent event) {
		commonServ.WindowClose(event);
		membershipServ.openMember();
	}
    public void cancelProc(ActionEvent event) {
        commonServ.WindowClose(event);
        commonServ.openMain();
	}
    public void cancelSimple(ActionEvent event) {
    	commonServ.ErrorMsg("닫기", "창을 답습니다", "창을 닫습니다");
        commonServ.WindowClose(event);
	}
    
    //상품 상세 페이지1
    public void openDetail1(ActionEvent event) {
    	orderServ.openDetail1(commonServ.MenuItemGetText(event));   // 디테일1열고		
		try {
			Stage stage = (Stage)topMenu.getScene().getWindow();
			stage.close();
		}catch(Exception e) {
		 e.printStackTrace();
		}
    }
    
    //상품 상세 페이지2
    public void openDetail2(ActionEvent event) {
    	orderServ.openDetail2(commonServ.ButtonGetText(event));
	    commonServ.WindowClose(event);
    }

   //주문하기
   public void orderProc(ActionEvent event) {
	  if(orderServ.checkLogin(detailForm2) == false) {
		  loginServ.openLogin();
	  }else {
		  orderServ.preOrderItem(detailForm2);
		  orderServ.orderProc(commonServ.ButtonGetText(event));
	  }
      commonServ.WindowClose(event);
   }
   
   //주문 후 주문내역 보여주기
   public void orderdetail(ActionEvent event) {
      orderServ.orderDetail(commonServ.ButtonGetText(event));
      commonServ.WindowClose(event);
   }
   
   //마이페이지1 --회원 정보 창 (구매 정보 등등) 띄우기
   //만약 로그인 상태가 아니면 로그인창을 대신 띄우기
   public void openMyPage(ActionEvent event) {
	   if (myPageServ.checkLogin(mainForm) == false) {
		   loginServ.openLogin();
	   }else {
		   myPageServ.openMyPage();
	   }
	   commonServ.WindowClose(event);
   }
   
   //마이페이지2 -- 회원정보 수정 띄우기
   public void openMyPage2() {
	   myPageServ.openMyPage2();
   }
   
   //회원정보 수정하기
   public void updateInfo(ActionEvent event) {
	   myPageServ.updateInfo(myPageForm2);
   }
   
   //창 최소화 시키기
   public void minimize() {
      mini.setOnAction(e -> {
         ((Stage) ((Button) e.getSource()).getScene().getWindow()).setIconified(true);
      });
   }
}
