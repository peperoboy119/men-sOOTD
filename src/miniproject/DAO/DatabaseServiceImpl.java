package miniproject.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import miniproject.Member;
import miniproject.OrderItem;

public class DatabaseServiceImpl implements DatabaseService {
	final static String DRIVER = "oracle.jdbc.driver.OracleDriver";
	final static String URL = "jdbc:oracle:thin:@192.168.0.52:1521:xe";

	//회원가입
	final String INSERTSQL = "insert into project_member(userid,userpw,name,age,email,tel,address)"
			+ " values(?,?,?,?,?,?,?)";
	final String CHECKSQL = "select count(userid) from project_member where userid = ?";

	//로그인, 로그인상태 확인
	final String LOGINSQL = "select count(userid) from project_member where userid = ? AND userpw = ?";
	final String INSERTLOGINSQL = "insert into project_member_login(userid, status) values(?, ?)";
	final String CHECKLOGINSQL = "select * from project_member_login, project_member where project_member_login.userid = project_member.userid AND project_member_login.status = 'true'";
	
	//로그인 했을 경우 회원의 개인정보 찾기
	final String SELECTALL = "select * from project_member where userid = ?";

	//로그인 상태/기록 업데이트, 초기화
	final String UPDATESTATUS = "update project_member set status = 'true' where userid = (select userid from project_member_login where status = 'true')";
	final String CLEARSTATUS = "update project_member_login set status = 'false'";
	
	private Connection dbConn;
	
	static {
		try {
			//Driver연결 확인
			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//데이터베이스 연결
	@Override
	public boolean Open() {
		try {
			String id = "kgitbank";
			String pw = "itbank";
			dbConn = DriverManager.getConnection(URL, id, pw);
			System.out.println("오라클 연결 성공");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//로그인을 하면 회원 상태 업데이트
	public boolean UpdateStatus() {
		try {
			PreparedStatement ps = dbConn.prepareStatement(UPDATESTATUS);
			ps.executeUpdate();
			System.out.println("업데이트 완료");
			return true;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//모든 창이 종료되면 로그인 상태 모두 초기화
	public void ClearStatus() {
		try {
			PreparedStatement ps = dbConn.prepareStatement(CLEARSTATUS);
			PreparedStatement ps1 = dbConn.prepareStatement("update project_member set status = 'false'");
			
			ps.executeUpdate();
			ps1.executeUpdate();
		
			System.out.println("초기화");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//회원등록
	@Override
	public void Insert(Member member) {
		try {
			PreparedStatement ps = dbConn.prepareStatement(INSERTSQL);
			ps.setString(1, member.getId());
			ps.setString(2, member.getPw());
			ps.setString(3, member.getName());
			ps.setString(4, member.getAge());
			ps.setString(5, member.getEmail());
			ps.setString(6, member.getPhoneNum());
			ps.setString(7, member.getAddress());
			System.out.println("회원 등록 완료");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//회원가입시 아이디 중복 확인
	@Override
	public boolean CheckId(String id) {
		boolean result = true;
		try {
			PreparedStatement ps = dbConn.prepareStatement(CHECKSQL);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) == 0) {
					//count(id)가 0이면 아이디 중복 아님
					result = false;
				}
				rs.close();
				ps.close();
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	//로그인 
	@Override
	public boolean Select(String id, String pw) {
		boolean result = true;
		try {
			PreparedStatement ps = dbConn.prepareStatement(LOGINSQL);
			ps.setString(1, id);
			ps.setString(2, pw);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) == 0 ) {
					//count(id)가 0이면 로그인 실패
					result = false;
				}
				rs.close();
				ps.close();
			}
			
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	//로그인 후 로그인된 상태를 데이터베이스에 기록
	@Override
	public void InsertLogIn(String id) {
		try {
			PreparedStatement ps = dbConn.prepareStatement(INSERTLOGINSQL);
			ps.setString(1, id);
			ps.setString(2, "true");
			System.out.println("회원 로그인 기록");
			ps.executeUpdate();

		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//로그인을 한 회원일 경우 그 회원의 아이디를 Member를 사용해서 저장해놓기
	@Override
	public Member CheckLoginId() {
		Member member = new Member();
		
		try {
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery(CHECKLOGINSQL);
			
			if(rs.next()) {
				System.out.println("실행");
				String id = rs.getString("userid");
				member.setId(id);
				return member;
			}
			stmt.close();
			rs.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}
	
	//회원의 모든 정보를 Member에 저장
	@Override
	public Member SelectAll(String id) {
		Member member = new Member();
		try {
			PreparedStatement ps = dbConn.prepareStatement(SELECTALL);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String userId = rs.getString("userid");
				String userPw = rs.getString("userpw");
				String userName = rs.getString("name");
				String userAge = rs.getString("age");
				String userTel  = rs.getString("tel");
				String userEmail = rs.getString("email");
				String userAddress = rs.getString("address");
				
				member.setId(userId);
				member.setPw(userPw);
				member.setName(userName);
				member.setAge(userAge);
				member.setPhoneNum(userTel);
				member.setEmail(userEmail);
				member.setAddress(userAddress);
			}
				rs.close();
				ps.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}
	
	//데이터베이스에서 해당되는 category에 있는 모든 상품들을 각 OrderItem에 저장
	@Override
	public List<OrderItem> DisplayAllItem(String category) {
        List<OrderItem> list = new ArrayList<OrderItem>();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select * from project_item where category = ?");
			ps.setString(1, category);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {				
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rs.getString("itemId"));
				orderItem.setName(rs.getString("itemName"));
				orderItem.setQty(rs.getString("quantity"));
				orderItem.setPrice(rs.getString("price"));
				orderItem.setImageUrl(rs.getString("imagename"));
				list.add(orderItem);
			}
				rs.close();
				ps.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//상품 상세페이지2에서 선택된 하나의 상품만 보여주기
	@Override
	public OrderItem DisplayOneItem(String item) {
		OrderItem orderItem = new OrderItem();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select * from project_item where itemname = ?");
			ps.setString(1, item);

			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String itemName = rs.getString("itemname");
				String itemPrice = rs.getString("price");
				String itemQty = rs.getString("quantity");
				String itemImgName  = rs.getString("imagename");
				
				orderItem.setName(itemName);
				orderItem.setPrice(itemPrice);
				orderItem.setQty(itemQty);
				orderItem.setImageUrl(itemImgName);
			}
				rs.close();
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderItem;
	}

	//장바구니에 상품 이름, 가격, 수량을 저장
	public void InsertPreOrder(String id, String name, String quantity, String price) {
		int totalPrice = (Integer.parseInt(price) * Integer.parseInt(quantity));
		
		try {			
			PreparedStatement ps = dbConn.prepareStatement("insert into pre_order values(?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, quantity);
			ps.setInt(3, totalPrice);
			ps.setString(4, id);
			
			System.out.println("주문 Pre Order 완료");
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//장바구니에서 상품이름, 가격, 수량 가져오기
	public OrderItem GetPreOrder() {
		OrderItem orderItm = new OrderItem();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select * from pre_order");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				orderItm.setName(rs.getString("itemname"));
				orderItm.setPrice(rs.getString("price"));
				orderItm.setQty(rs.getString("quantity"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return orderItm;
	}
	
	@Override
	public void DeletePreOrder() {
		try {
			PreparedStatement ps = dbConn.prepareStatement("delete from pre_order");
			ps.executeUpdate();
			System.out.println("Pre Order 모두 삭제");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//상품 주문하기
	@Override
	public void OrderItem(OrderItem item, Member member) {
		try {
			PreparedStatement ps = dbConn.prepareStatement("insert into project_order(userid, name, email, tel, address, itemname, quantity, price) values(?,?,?,?,?,?,?,?)");
			ps.setString(1, member.getId());
			ps.setString(2, member.getName());
			ps.setString(3, member.getEmail());
			ps.setString(4, member.getPhoneNum());
			ps.setString(5, member.getAddress());
			ps.setString(6, item.getName());
			ps.setString(7, item.getQty());
			ps.setString(8, item.getPrice());

			System.out.println("주문 등록 완료");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//주문하고 주문 상태를 주문완료로 업데이트 하기
	@Override
	public void UpdateOrderStatus(String orderId, String userId) {
		try {
			PreparedStatement ps = dbConn.prepareStatement("insert into order_status(orderid, userid) values(?,?)");
			ps.setString(1, orderId);
			ps.setString(2, userId);
			System.out.println("주문 상태 업데이트 완료");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//주문이 끝나고 주문 상세 페이지에서 주문한 상품에 대한 정보들 표시하기
	public OrderItem DisplayOrderItem(String item) {
		OrderItem orderItem = new OrderItem();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select * from project_order where itemname = ? order by rownum desc");
			ps.setString(1, item);

			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String itemName = rs.getString("itemname");
				String itemPrice = rs.getString("price");
				String itemQty = rs.getString("quantity");
				
				orderItem.setName(itemName);
				orderItem.setPrice(itemPrice);
				orderItem.setQty(itemQty);
			}
				rs.close();
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderItem;
	}

	//회원 주문 아이디 가지고 오기
	@Override
	public Member DisplayOrderId() {
		Member member = new Member();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select * from (select orderid from project_order order by orderid desc) where rownum = 1");
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String orderId = rs.getString("orderid");
				member.setOrderId((orderId));
			}
				rs.close();
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}

	//마이페이지에서 회원의 주문 정보들 표시하기
	@Override
	public List<OrderItem> GetUserOrder(String id) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select * from project_order where userid = ? order by orderid desc");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {				
				OrderItem orderItem = new OrderItem();
				orderItem.setName(rs.getString("itemname"));
				orderItem.setQty(rs.getString("quantity"));
				orderItem.setPrice(rs.getString("price"));
				list.add(orderItem);
			}
				rs.close();
				ps.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//마이페이지에서 회원정보 업데이트 하기
	@Override
	public void UpdateMemInfo(Member member) {
		try {
			PreparedStatement ps = dbConn.prepareStatement("update project_member set userpw=?, email=?, tel=?, address=? where userid = ?");
			ps.setString(1, member.getPw());
			ps.setString(2, member.getEmail());
			ps.setString(3, member.getPhoneNum());
			ps.setString(4, member.getAddress());
			ps.setString(5, member.getId());
			System.out.println("회원 정보 수정 완료");
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//마이페이지에 표시할 주문 상태 갖고오기
	public OrderItem SelectOrderStatus(String status, String userId) {
		OrderItem totalOrderItm = new OrderItem();
		try {
			PreparedStatement ps = dbConn.prepareStatement("select count(orderid) AS totalcnt from order_status where userid = ? AND status = ?");
			ps.setString(1, userId);
			ps.setString(2, status);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				totalOrderItm.setItemCount(rs.getString("totalcnt"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalOrderItm;
	}
}