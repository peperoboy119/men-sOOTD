package miniproject;

public class OrderItem {
	
   private String id; 			//상품 고유 아이디
   private String name; 		//상품 이름
   private String qty; 			//상품 수량
   private String price;		//상품 가격
   private String imageUrl; 	//상품 사진 이름 
   private String category;		//상품 카테고리 (셔츠, 맨투맨 등등)
   private String userId;		//회원 아이디
   private String itemCount;	//주문 수
   
   public String getCategory() {
	   return category;
   }
   public void setCategory(String category) {
	   this.category = category;
   }
   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getQty() {
      return qty;
   }
   public void setQty(String qty) {
      this.qty = qty;
   }
   public String getPrice() {
      return price;
   }
   public void setPrice(String price) {
      this.price = price;
   }
   public String getImageUrl() {
      return imageUrl;
   }
   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

}
