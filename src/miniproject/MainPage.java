package miniproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import miniproject.DAO.DatabaseService;
import miniproject.DAO.DatabaseServiceImpl;

public class MainPage extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		Controller ctrler = loader.getController();
		ctrler.setMainForm(root);

		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//데이터베이스에 있는 로그인 기록들 모두 초기화시킴
		DatabaseService dbServ = new DatabaseServiceImpl();
		dbServ.Open();
		dbServ.ClearStatus();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
