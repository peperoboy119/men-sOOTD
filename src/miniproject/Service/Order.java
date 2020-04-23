package miniproject.Service;

import javafx.scene.Parent;

public interface Order {
	
	public void openDetail1(String category); //상세페이지1 띄우기
	public void openDetail2(String item); //상세페이지2 띄우기
	
	public void preOrderItem(Parent detailForm2); //장바구니처럼 데이터베이스에 잠시 상품, 가격, 수량을 저장해놓기
	
	//주문과 관련된 메소드 표시할때 
	public void orderProc(String item); //주문하기
	public void orderDetail(String item); //주문 상세내역 보여주기
	
	public boolean checkLogin(Parent orderForm); //로그인 상태 확인
}
