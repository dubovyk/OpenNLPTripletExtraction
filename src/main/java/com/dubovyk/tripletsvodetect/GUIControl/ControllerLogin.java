package com.dubovyk.tripletsvodetect.GUIControl;

import com.dubovyk.tripletsvodetect.database.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Arrays;


public class ControllerLogin {
    public GridPane login;
    @FXML private TextField address, port, user;
    @FXML private PasswordField password;


    @FXML protected void handleSubmitButtonAction(ActionEvent event){
        String title = "TripletSVO | Login";
        try {
            DBConnector connector = DBConnector.getInstance();
            boolean success = connector.connect("bolt://"+address.getText(), Integer.parseInt(port.getText()), user.getText(), password.getText());
            if(success){
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(this.getClass().getResource("/templates/main.fxml"));
                Scene scene = new Scene(root, 750, 450);
                stage.setTitle(title);
                stage.setScene(scene);
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Wrong login data or neo4j server isn`t launched.");
                alert.showAndWait();
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(e.toString() + "\n" + Arrays.toString(e.getStackTrace()));
            alert.showAndWait();
        }
    }
}
