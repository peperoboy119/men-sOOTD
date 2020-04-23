package miniproject.DAO;

import java.util.List;

import miniproject.Member;
import miniproject.OrderItem;

public interface DatabaseService {
	//데이터베이스 연결
	public boolean Open();
	//데이터베이스에서 로그인 기록 업데이트
	public boolean UpdateStatus();
	//데이터베이스에서 로그인 기록 초기화
	public void ClearStatus();
	
	public void Insert(Member member);//회원가입 -- 새로운 회원 만들기
	public boolean CheckId(String id);//회원가입 -- 아이디 중복확인

	public boolean Select(String id, String pw); //로그인
	
	public Member CheckLoginId(); //로그인 후 -- 로그인상태 확인
	public void InsertLogIn(String id); //로그인 후 -- 로그인상태 데이터베이스에 저장하기
	
	public Member SelectAll(String id); //로그인 후 -- Member에 회원 정보 저장
	
	public List<OrderItem> DisplayAllItem(String category); //상품 -- 카테고리안에 있는 모든 상품 표시
	public OrderItem DisplayOneItem(String item); // 상품 -- 하나의 상품만 표시
	
	public void InsertPreOrder(String id, String name, String quantity, String price);//장바구니
	public OrderItem GetPreOrder(); //장바구니
	public void DeletePreOrder(); //장바구니
	
	public void OrderItem(OrderItem item, Member member); // 주문하기
	public void UpdateOrderStatus(String orderId, String userId); //주문상태 업데이트

	public OrderItem DisplayOrderItem(String item);  //주문 상세 페이지에서 상품 정보 표시
	public Member DisplayOrderId(); //주문 번호 표시하기
	
	public List<OrderItem> GetUserOrder(String id); //마이페이지에서 회원의 주문 정보들 표시
	public void UpdateMemInfo(Member member);
	public OrderItem SelectOrderStatus(String status, String userId); //주문상태에 따라 표시
}
