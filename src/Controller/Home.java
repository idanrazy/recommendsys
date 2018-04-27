package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by levye on 27-Apr-18.
 */
public class Home implements Initializable {

    public Pane register;
    public Pane login;
    public AnchorPane detailContainer;
    public TextField login_username;
    public TextField login_password;
    public Button mymovies;
    public TextField shawshenk;
    public TextField requiem;
    public TextField fightclub;
    public TextField starwars;
    public TextField forrest;
    public TextField register_username;
    public TextField register_password;

    public final static Map<String,String> users = new HashMap<>();
    public static String currentUser = null;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        users.put("a", "a");

    }

    public void onPressMaster(ActionEvent event){
        String id = ((Button) event.getSource()).getText();
        detailContainer.getChildren().forEach(
                pane -> {
                    if (pane.getId().equalsIgnoreCase(id)) pane.setVisible(true);
                    else pane.setVisible(false);
                }
        );
    }

    public void onPressLogin(ActionEvent event){
        String username = login_username.getText();
        String password = login_password.getText();

        if (username.length()>0 && password.length()>0 && users.containsKey(username) && users.get(username).equals(password)){
            if (currentUser != null){
                logout();
            }
            currentUser = username;
            mymovies.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed");
            alert.setHeaderText("Login failed!");
            alert.setContentText("Username and/or password incorrect.");
            alert.show();
        }
    }

    public void logout(){
        currentUser = null;
        mymovies.setVisible(false);
    }

    public void onRegister(ActionEvent event){
        String username = register_username.getText();
    }

}
