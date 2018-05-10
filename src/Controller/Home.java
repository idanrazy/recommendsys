package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;

/**
 * Created by levye on 27-Apr-18.
 */
public class Home implements Initializable {

    public Pane register;
    public Pane login;
    public AnchorPane detailContainer;
    public TextField login_username;
    public TextField login_password;
    public Button btn_mymovies;
    public TextField shawshenk;
    public TextField requiem;
    public TextField fightclub;
    public TextField starwars;
    public TextField forrest;
    public TextField register_username;
    public TextField register_password;
    public Button logout;
    public TableView table;
    public TableColumn col_name;
    public TableColumn col_rating;
    public TableColumn col_genres;

    public static HashMap<String,Double> myratings = new HashMap<>();

    public static HashMap<String,ArrayList<MovieRow>> user_tables = new HashMap<>();

    public final static Map<String,String> users = new HashMap<>();
    public static String currentUser = null;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        col_genres.setCellValueFactory(new PropertyValueFactory<>("genres"));

//        table.getItems().add(new MovieRow("star wars", 4.5, "Drama"));
    }

    public ArrayList<Pair<String,Double>> getRatings(java.util.Set<java.util.Map.Entry<String,Double>> user_rank){
        HashMap<Integer, String[]> dataset = CSVparser.parse(300000);
        DataFrame df = new DataFrame(dataset);
        item2item t = new item2item(df);
        java.util.Map.Entry<String,Double>[] ranks = new Map.Entry[user_rank.size()];
        user_rank.toArray(ranks);
        ArrayList<Pair<String,Double>> test = t.predictmovelist(df,ranks);

        return test;
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

        login (username,password);
    }

    public void login(String username, String password){
        if (username.length()>0 && password.length()>0 && users.containsKey(username) && users.get(username).equals(password)){
            if (currentUser != null){
                logout();
            }
            currentUser = username;
            btn_mymovies.setVisible(true);
            logout.setVisible(true);

            ArrayList<MovieRow> user_rows = user_tables.get(currentUser);
            if (user_rows == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error loading your preferences.");
                alert.show();
                return;
            } else {
                table.getItems().clear();
                table.getItems().addAll(user_rows);
            }

            btn_mymovies.getOnAction().handle(new ActionEvent(btn_mymovies,null));
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
        btn_mymovies.setVisible(false);
        logout.setVisible(false);

        detailContainer.getChildren().forEach(
                pane ->{
                    if (pane.getId().equalsIgnoreCase("welcome")) pane.setVisible(true);
                    else pane.setVisible(false);
                }
        );
    }

    public void onPressLogout(ActionEvent event){
        logout();
    }

    public void onRegister(ActionEvent event){
        String username = register_username.getText();
        String password = register_password.getText();

        if (users.containsKey(username)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username already exists. Pick a different user.");
            alert.show();
            return;
        } else{
            users.put(username,password);
            currentUser = username;

            java.util.Set<java.util.Map.Entry<String,Double>> user_rank = processRatings();
            prepareTable(user_rank);
            login(username,password);
        }

    }

    public void prepareTable(java.util.Set<java.util.Map.Entry<String,Double>> user_rank){

        ArrayList<MovieRow> rows = new ArrayList<>();

        ArrayList<Pair<String,Double>> ratings = getRatings(user_rank);
        ratings.stream().limit(20).forEachOrdered(
                item ->{
                    String[] details = item2item.getDetailsMovie(Integer.parseInt(item.getKey()));
                    MovieRow row = new MovieRow(details[1], item.getValue(), details[2]);
                    rows.add(row);
                }
        );

        table.getItems().clear();
        table.getItems().addAll(rows);

        user_tables.put(currentUser,rows);
    }

    public java.util.Set<java.util.Map.Entry<String, Double>> processRatings(){
        try {
            double shawshenk = Double.parseDouble(this.shawshenk.getText());
            double requiem = Double.parseDouble(this.requiem.getText());
            double forrest = Double.parseDouble(this.forrest.getText());
            double fightclub = Double.parseDouble(this.fightclub.getText());
            double starwars = Double.parseDouble(this.starwars.getText());

            myratings.put("318", shawshenk);
            myratings.put("356", forrest);
            myratings.put("2959", fightclub);
            myratings.put("3949", requiem);
            myratings.put("1196", starwars);

            return myratings.entrySet();


        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("One of your ratings is invalid! Please enter a valid number");
            alert.show();
        }

        return null;
    }


    public class MovieRow{
        private String name;
        private Double rating;
        private String genres;

        public MovieRow(String name, Double rating, String genres){
            this.name = name;
            this.rating = rating;
            this.genres = genres;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getGenres() {
            return genres;
        }

        public void setGenres(String genres) {
            this.genres = genres;
        }
    }

}
