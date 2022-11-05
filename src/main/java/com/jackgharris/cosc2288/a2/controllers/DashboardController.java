package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.controllers.components.popups.DeleteRecordPopupController;
import com.jackgharris.cosc2288.a2.controllers.components.popups.EditRecordPopupController;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Weight;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController{

    @FXML
    private AnchorPane parent;

    @FXML
    private AnchorPane menuContainerOuter;


    @FXML
    private TableView<Weight> weightTable;


    @FXML
    public void initialize() throws IOException {
        //Load the menu
        FXMLLoader menuLoader = new FXMLLoader(FXMLUtility.menu);
        AnchorPane menuNodes = menuLoader.load();
        this.menuContainerOuter.getChildren().add(menuNodes.getChildren().get(0));

        //set the background colors
        this.menuContainerOuter.setStyle("-fx-background-color: "+MyHealth.getInstance().getTheme().getMenuColor());
        this.parent.setStyle("-fx-background-color: "+MyHealth.getInstance().getTheme().getBackgroundColor());
    }


    public void deleteRecord() throws IOException {
       Weight weight = this.weightTable.getSelectionModel().getSelectedItem();

        if(!MyHealth.isStageShown("deleteRecordPopup")){
            Stage stage = new Stage();
            stage.getProperties().put("id", "deleteRecordPopup");
            stage.setResizable(false);
            stage.getIcons().add(Resource.warningFavicon());
            stage.setTitle("Delete Record Confirmation");
            stage.getProperties().put("record", weight);

            FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtility.deleteRecordPopup);
            stage.setScene(new Scene(fxmlLoader.load()));

            DeleteRecordPopupController controller = fxmlLoader.getController();
            controller.setRecord(weight);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MyHealth.getStageById("dashboard"));
            stage.showAndWait();

        }else{
            Stage stage = MyHealth.getStageById("deleteRecordPopup");
            stage.requestFocus();
        }
    }

    public void editRecord() throws IOException {

        Weight weight = this.weightTable.getSelectionModel().getSelectedItem();

        if(!MyHealth.isStageShown("editRecordPopup")){
            Stage stage = new Stage();
            stage.getProperties().put("id", "editRecordPopup");
            stage.setResizable(false);
            stage.getIcons().add(Resource.warningFavicon());
            stage.setTitle("Edit Record");
            stage.getProperties().put("record", weight);

            FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtility.editRecordPopup);
            stage.setScene(new Scene(fxmlLoader.load()));

            EditRecordPopupController controller = fxmlLoader.getController();
            controller.setRecord(weight);


            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MyHealth.getStageById("dashboard"));
            stage.showAndWait();

        }else{
            Stage stage = MyHealth.getStageById("editRecordPopup");
            stage.requestFocus();
        }

    }


}
