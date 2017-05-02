package com.dubovyk.tripletsvodetect.GUIControl;

import com.dubovyk.tripletsvodetect.NLP.model.TripletSVO;
import com.dubovyk.tripletsvodetect.database.DBConnector;
import com.dubovyk.tripletsvodetect.model.GlobalModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class ControllerResults implements Initializable{

    private ObservableList<TripletSVO> dataEntries;
    @FXML public VBox vbox;
    @FXML public TableView<TripletSVO> resultsTable;
    @FXML public TableColumn<TripletSVO, String> objectColumn;
    @FXML public TableColumn<TripletSVO, String> relationColumn;
    @FXML public TableColumn<TripletSVO, String> subjectColumn;
    @FXML public Button submitBtn;
    @FXML public Button cancelBtn;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        dataEntries = GlobalModel.getInstance().getCurrentData();
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        relationColumn.setCellValueFactory(new PropertyValueFactory<>("relation"));
        objectColumn.setCellValueFactory(new PropertyValueFactory<>("object"));
        resultsTable.setItems(dataEntries);
    }

    /**
     * Used to write data about the parsed relations to the database.
     */
    @FXML protected void handleSubmit(ActionEvent event){
        DBConnector connector = DBConnector.getInstance();
        boolean success = connector.addAll(GlobalModel.getInstance().getCurrentData());
        if (success){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Data was saved to database");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed");
            alert.setContentText("Data wasn`t saved to database");
            alert.showAndWait();
        }
    }

    @FXML protected void handleCancel(ActionEvent event){
        Stage curStage = (Stage) cancelBtn.getScene().getWindow();
        curStage.close();
    }
}
