package com.dubovyk.tripletsvodetect.GUIControl;

import com.dubovyk.tripletsvodetect.NLP.config.consts;
import com.dubovyk.tripletsvodetect.NLP.nlp.TripletSVODetector;
import com.dubovyk.tripletsvodetect.database.DBConnector;
import com.dubovyk.tripletsvodetect.model.GlobalModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.*;
import java.util.Optional;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class ControllerMain {
    @FXML private TextArea textArea;
    private InputStream modelPos;
    private POSModel model;
    private POSTaggerME tagger;

    private TripletSVODetector detector = new TripletSVODetector();
    private String filePath;
    {
        try {
            //modelPos = new FileInputStream();
            model = new POSModel(this.getClass().getResourceAsStream(consts.EN_POS_MAXENT));
            tagger = new POSTaggerME(model);
        } catch (IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Couldn`t load model data");
            Platform.exit();
        }
    }


    @FXML protected void handleNewFile(ActionEvent event){
        if(filePath != null || !textArea.getText().isEmpty()){
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("New file");
            confirmation.setHeaderText("Do you want to create a new file?");
            confirmation.setContentText("If so, current changes will be lost.");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() != ButtonType.OK || !result.isPresent()){
                return;
            }
            GlobalModel.getInstance().clear();
            filePath = null;
            textArea.setText(null);
        }

    }

    @FXML protected void handleOpenFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        if(file != null){
            filePath = file.getAbsolutePath();
            String data = readFile(filePath);
            textArea.setText(data);
        }
    }

    @FXML protected void handeSaveAsFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null) {
            filePath = file.getAbsolutePath();
            writeFile(filePath, textArea.getText());
        }
    }

    @FXML protected void handleSaveFile(ActionEvent event){
        if(filePath != null){
            writeFile(filePath, textArea.getText());
        } else {
            handeSaveAsFile(event);
        }
    }

    @FXML protected void handleDetectSVO(ActionEvent event){
        GlobalModel.getInstance().clear();
        GlobalModel.getInstance().parseText(textArea.getText());
        try {
            Stage stage = new Stage();
            Parent resultDesign = FXMLLoader.load(getClass().getResource("/templates/results.fxml"));
            Scene resultScene = new Scene(resultDesign, 400, 500);
            stage.setTitle("Results");
            stage.setScene(resultScene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e){
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("File Error");
            error.setContentText("Could not load resource file");
            error.showAndWait();
        }
    }

    @FXML protected void handleDetectAndWrite(ActionEvent event){
        GlobalModel.getInstance().clear();
        GlobalModel.getInstance().parseText(textArea.getText());
        try {
            DBConnector connector = DBConnector.getInstance();
            connector.addAll(GlobalModel.getInstance().getCurrentData());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML protected void handleExit(ActionEvent event){
        Platform.exit();
    }

    private void writeFile(String fileName, String data){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String readFile(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null){
                builder.append(line);
                line = reader.readLine();
                if(line != null){
                    builder.append("\n");
                }
            }
            return builder.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
