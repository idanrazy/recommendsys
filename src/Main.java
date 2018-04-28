/**
 * Created by levye on 27-Apr-18.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/View/Home.fxml").openStream());
        primaryStage.setTitle("Recommendation System");
        primaryStage.setScene(new Scene(root, 1000, 750));
        primaryStage.show();


/*        if (debugMode) {
            Model.login("Eden", "Sisma123");
            mainController.loginSuccessful("Eden");
        }*/
    }


    public static void main(String[] args) {
        launch(args);
    }

}
