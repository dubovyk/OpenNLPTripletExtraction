package com.dubovyk.tripletsvodetect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        String title = "TripletSVO | Login";
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/templates/login.fxml"));
            Scene scene = new Scene(root, 350, 350);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*DBConnector connector = DBConnector.getInstance();
        connector.connect("bolt://127.0.0.1", 7687, "neo4j", "Password");
        Map<String, String > field1 = new HashMap<>();
        Map<String, String > field2 = new HashMap<>();
        field1.put("name", "1111");
        field1.put("surname", "1111");
        field2.put("name", "2222");
        field2.put("surname", "2222");

        GraphNode node1 = new GraphNode("Person", field1);
        GraphNode node2 = new GraphNode("Person", field2);
        TripletSVO svo = new TripletSVO();
        svo.setSubject("Java");
        svo.setObject("world");
        svo.setRelation("rules");
        connector.addRelation("killed", node1, node2);
        connector.addTriplet(svo);
        */
        launch(args);
    }
}
