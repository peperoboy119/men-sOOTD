package miniproject.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import miniproject.Controller;
import miniproject.Member;
import miniproject.OrderItem;
import miniproject.DAO.DatabaseService;
import miniproject.DAO.DatabaseServiceImpl;

public class OrderImpl implements Order {
	private DatabaseService dbServ = new DatabaseServiceImpl();

	//상품 상세페이지1 띄우기
	public void openDetail1(String category) {
		Stage detail1Form = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../DetailPage1.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		detail1Form.setScene(new Scene(parent));
		Controller cntler = loader.getController();
		cntler.setDetailForm1(parent);
		detail1Form.show();
		
		detail1DisplayItemsInfo(parent, category);
	}
	
	//상세페이지1 상품들 정보 띄우기
	private void detail1DisplayItemsInfo(Parent parent, String category) {
		dbServ.Open();
		
		Button label = (Button)parent.lookup("#category");
		label.setText(category);
		Button loginbtn = (Button)parent.lookup("#loginbtn");

		ArrayList<ImageView> imageView = new ArrayList<ImageView>();
		ImageView image1 = (ImageView)parent.lookup("#image1");
		ImageView image2 = (ImageView)parent.lookup("#image2");
		ImageView image3 = (ImageView)parent.lookup("#image3");
		ImageView image4 = (ImageView)parent.lookup("#image4");
		imageView.add(image1);
		imageView.add(image2);
		imageView.add(image3);
		imageView.add(image4);

		ArrayList<Button> imageBtn = new ArrayList<Button>();
		Button imagebtn1 = (Button)parent.lookup("#imagebtn1");
		Button imagebtn2 = (Button)parent.lookup("#imagebtn2");
		Button imagebtn3 = (Button)parent.lookup("#imagebtn3");
		Button imagebtn4 = (Button)parent.lookup("#imagebtn4");
		imageBtn.add(imagebtn1);
		imageBtn.add(imagebtn2);
		imageBtn.add(imagebtn3);
		imageBtn.add(imagebtn4);
		
		String id = dbServ.CheckLoginId().getId();
		if (id == null) {
			loginbtn.setText("비회원");
		} else {
			loginbtn.setText(id);
		}
		
		String sqlCategory = "";
		switch(category) {
			case "맨투맨":
				sqlCategory = "mantoman";
				break;
			case "셔츠":
				sqlCategory = "shirt";
				break;
			case "니트":
				sqlCategory = "knit";
				break;
			case "청바지":
				sqlCategory = "jean";
				break;
			case "골덴바지":
				sqlCategory = "corded_velveteen";
				break;
			case "슬랙스":
				sqlCategory = "slacks";
				break;
			case "숏패딩":
				sqlCategory = "short_padding";
				break;
			case "라이더 자켓":
				sqlCategory = "rider_jacket";
				break;
			case "울코트":
				sqlCategory = "wool_coat";
				break;
			case "로퍼":
				sqlCategory = "loafer";
				break;
			case "어글리슈즈":
				sqlCategory = "ugly_shoes";
				break;
			case "단화":
				sqlCategory = "flatties";
				break;
		}
		
		List<OrderItem> items = dbServ.DisplayAllItem(sqlCategory);
		OrderItem orderItem;
		for (int i = 0; i < items.size(); i++) {
			orderItem = new OrderItem();
			orderItem = (OrderItem) items.get(i);
			imageView.get(i).setImage(new Image("/miniproject/img/" + orderItem.getImageUrl() + ".jpg"));
			imageBtn.get(i).setText(orderItem.getName());
		}		
	}
	
	//상세페이지2 띄우기
	@Override
	public void openDetail2(String item) {
		Stage detail2Form = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../DetailPage2.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		detail2Form.setScene(new Scene(parent));
		detail2Form.setTitle("Detail2");
		Controller cntler = loader.getController();
		cntler.setDetailForm2(parent);
		detail2Form.show();
		
		detail2DisplayItemInfo(parent, item);
	}
	
	//상세페이지2에 상품 정보 띄우기
	private void detail2DisplayItemInfo(Parent parent, String item) {
		dbServ.Open();
		
		Button loginbtn = (Button)parent.lookup("#loginbtn");
		String id = dbServ.CheckLoginId().getId();
		if (id == null) {
			loginbtn.setText("비회원");
		} else {
			loginbtn.setText(id);
		}
		
		OrderItem items = dbServ.DisplayOneItem(item);
		Button orderButton = (Button)parent.lookup("#orderbtn");
		orderButton.setText(items.getName() + " 주문하기");
				
		ImageView imgView = (ImageView)parent.lookup("#item_image");
		Label itemName = (Label)parent.lookup("#item_name");
		Label itemPrice = (Label)parent.lookup("#item_price");
		TextField itemQuantity = (TextField)parent.lookup("#item_quantity");
		itemQuantity.requestFocus();
		itemName.setText(items.getName());
		itemPrice.setText(items.getPrice());
		imgView.setImage(new Image("/miniproject/img/" + items.getImageUrl() + ".jpg"));		
	}

	//장바구니처럼 데이터베이스에 상품 이름, 가격, 수량을 잠시 저장해놓기
	public void preOrderItem(Parent detailForm2) {
		OrderItem orderItm = new OrderItem();
		Label itemName = (Label)detailForm2.lookup("#item_name");
		Label itemPrice = (Label)detailForm2.lookup("#item_price");
		TextField itemQuantity = (TextField)detailForm2.lookup("#item_quantity");
		
		String itemQty = itemQuantity.getText();
		orderItm.setName(itemName.getText());
		orderItm.setPrice(itemPrice.getText());
		orderItm.setQty(itemQty);
		
		dbServ.Open();
		String id = dbServ.CheckLoginId().getId();
		dbServ.InsertPreOrder(id, orderItm.getName(), orderItm.getQty(), orderItm.getPrice());
	}	
   
	//주문하기
   @Override
   public void orderProc(String item) {
	  Stage orderForm = new Stage();
	  FXMLLoader loader = new FXMLLoader(getClass().getResource("../Order.fxml"));
	  Parent parent = null;
	  try {
	     parent = loader.load();
	  } catch (IOException e) {
	     e.printStackTrace();
	  }
	  orderForm.setScene(new Scene(parent));
	  orderForm.setTitle("order");
      Controller cntler = loader.getController();
      cntler.setOrderForm(parent);  
      orderForm.show();
      
      orderDisplayInfo(parent, item);
   }

   private void orderDisplayInfo(Parent parent, String item) {
      dbServ.Open();
      
      Button loginbtn = (Button)parent.lookup("#loginbtn");
      String id = dbServ.CheckLoginId().getId();
		
      TextField userName = (TextField)parent.lookup("#userName");
      TextField userTel = (TextField)parent.lookup("#userTel");
      TextField userEmail = (TextField)parent.lookup("#userEmail");
      TextField userAddr = (TextField)parent.lookup("#userAddress");
      
      Member mem = new Member();
      if (id == null) {
    	  loginbtn.setText("비회원");
      } else {
    	  loginbtn.setText(id);
    	  mem = dbServ.SelectAll(id);
    	  userName.setText(mem.getName());
    	  userTel.setText(mem.getPhoneNum());
    	  userEmail.setText(mem.getEmail());
    	  userAddr.setText(mem.getAddress());
      }
		
      int indexNum = item.indexOf("주");
      String item1 = item.substring(0, (indexNum-1));

      OrderItem items = dbServ.DisplayOneItem(item1);
      Button orderButton = (Button)parent.lookup("#orderbtn");
      orderButton.setText(items.getName() + " 주문하기");
      OrderItem items1 = dbServ.GetPreOrder();

      ImageView imgView = (ImageView)parent.lookup("#item_image");
      Label itemName = (Label)parent.lookup("#item_name");
      Label itemPrice = (Label)parent.lookup("#item_price");
      Label itemQty = (Label)parent.lookup("#item_quantity");
		
      itemName.setText(items.getName());
      itemPrice.setText(items1.getPrice());
      itemQty.setText(items1.getQty());
      imgView.setImage(new Image("/miniproject/img/" + items.getImageUrl() + ".jpg"));		
	}

   //로그인 상태 확인하기
   @Override
   public boolean checkLogin(Parent detailForm2) {
	   String id = ((Button)detailForm2.lookup("#loginbtn")).getText();
	   if (id.equals("비회원")) {
		   return false;
	   }else {
		   return true;
	   }
   }
   
   //주문 상세페이지 띄우기
   @Override
   public void orderDetail(String item) {
      Stage orderDetailForm = new Stage();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../OrderDetails.fxml"));
      Parent parent = null;
      try {
         parent = loader.load();
      } catch (IOException e) {
         e.printStackTrace();
      }
      orderDetailForm.setScene(new Scene(parent));
      orderDetailForm.setTitle("OrderDetail");
      Controller cntler = loader.getController();
      cntler.setOrderDetailForm(parent);  
      orderDetailForm.show();
      
      orderDetailDisplayInfo(parent, item);
   }

	private void orderDetailDisplayInfo(Parent parent, String item) {
	    dbServ.Open();
	    
	    String id = dbServ.CheckLoginId().getId();
	    Member mem = dbServ.SelectAll(id);
	    Label userName = (Label)parent.lookup("#userName");
	    Label userTel = (Label)parent.lookup("#userTel");
	    Label userEmail = (Label)parent.lookup("#userEmail");
	    Label userOrderId = (Label)parent.lookup("#userOrderId");
	    Button loginbtn = (Button)parent.lookup("#loginbtn");
			
	    int indexNum = item.indexOf("주");
	    String item1 = item.substring(0, (indexNum-1));
	    
	    OrderItem items = dbServ.DisplayOneItem(item1);
	    OrderItem preOrder = dbServ.GetPreOrder();
	
	    dbServ.OrderItem(preOrder, mem);
	    dbServ.DeletePreOrder();
	    
	    Member member = dbServ.DisplayOrderId();
	    
	    loginbtn.setText(id);
		userName.setText(mem.getName());
		userTel.setText(mem.getPhoneNum());
		userEmail.setText(mem.getEmail());
		userOrderId.setText(member.getOrderId());
	  	  
	    OrderItem orderedItem = dbServ.DisplayOrderItem(item1);
	    
	    ImageView imgView = (ImageView)parent.lookup("#item_image");
	    Label itemName = (Label)parent.lookup("#item_name");
	    Label itemPrice = (Label)parent.lookup("#item_price");
	    Label itemQty = (Label)parent.lookup("#item_quantity");
			
	    itemName.setText(orderedItem.getName());
	    itemPrice.setText(orderedItem.getPrice());
	    itemQty.setText(orderedItem.getQty());
	    imgView.setImage(new Image("/miniproject/img/" + items.getImageUrl() + ".jpg"));
	    
	    dbServ.UpdateOrderStatus(member.getOrderId(), id);	
	}
}
