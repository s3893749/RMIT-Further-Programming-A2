package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Comment;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Objects;

public class ShowRecordController {

    @FXML
    private Label recordTypeLabel;

    @FXML
    private TextArea recordNotesTextArea;

    @FXML
    private DatePicker recordDatePicker;

    @FXML
    private TextField recordValueTextField;

    @FXML
    private Button deleteRecordButton;

    @FXML
    private PasswordField deletePasswordInput;

    @FXML
    private AnchorPane parent;

    @FXML
    private Button saveButton;

    public static ShowRecordController instance;

    private Record record;

    private String startingNote;

    public void initialize(){
        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.recordDatePicker.setDayCellFactory(dayCellFactory);

        //disable the date picker text input
        this.recordDatePicker.getEditor().setDisable(true);

        //disable the delete record button
        this.deleteRecordButton.setDisable(true);
        ShowRecordController.instance = this;

        this.parent.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());
    }

    public void cancel(){
        MyHealth.getStageById("showRecord").close();
    }

    public void save(){
        this.record.setDate(this.recordDatePicker.getValue().toString());
        this.record.setValue(this.recordValueTextField.getText());
        this.record.updateDetails();

        if(!this.startingNote.equals(this.recordNotesTextArea.getText()) && this.recordNotesTextArea.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.record.getId()+"/"+this.recordNotesTextArea.getText(),this.recordDatePicker.getValue().toString()));
        }
        MyHealth.getStageById("showRecord").close();
        RecordWithLineChartController.getInstance().updateModels();
    }

    public void reset(){
        this.recordDatePicker.setValue(this.record.getDate());
        this.recordValueTextField.setText(this.record.getValue());
        this.recordNotesTextArea.setText("COMING SOON");
        this.recordValueTextField.getStyleClass().removeAll("text-field-error");
        this.recordValueTextField.getStyleClass().add("text-field-success");
        this.saveButton.setDisable(false);
    }

    public void delete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Record?");
        alert.setContentText("Are you sure you want to delete this record?");
        ImageView imageView = new ImageView(Resource.faviconWarning());
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        alert.setGraphic(imageView);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(MyHealth.class.getResource("css/app.css")).toExternalForm());
        dialogPane.getStyleClass().add("parent");
        dialogPane.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Resource.faviconWarning());
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {

            if (type == okButton) {
                Record.delete(this.record);
                MyHealth.getStageById("showRecord").close();
                RecordWithLineChartController.getInstance().updateModels();
            }
        });
    }

    public void noteChanged(){
        if(!this.recordNotesTextArea.getText().isBlank() || this.recordNotesTextArea.getText().equals(this.startingNote)){

            if(this.recordNotesTextArea.getText().length() > 50){
                this.recordNotesTextArea.getStyleClass().add("text-field-error");
                this.saveButton.setDisable(true);
            }else{
                this.recordNotesTextArea.getStyleClass().add("text-field-success");
                this.recordNotesTextArea.getStyleClass().removeAll("text-field-error");
                this.saveButton.setDisable(false);
            }

        }
    }

    public void valueChanged(){

        if(this.record.getType().equals("BloodPressure")){

            if (!Validation.isBloodPressure(this.recordValueTextField.getText())) {
                this.recordValueTextField.getStyleClass().add("text-field-error");
                this.recordValueTextField.getStyleClass().removeAll("text-field-success");
                this.saveButton.setDisable(true);
            } else {
                this.recordValueTextField.getStyleClass().removeAll("text-field-error");
                this.recordValueTextField.getStyleClass().add("text-field-success");
                this.saveButton.setDisable(false);
            }


        }else {

            if (!Validation.isFloat(this.recordValueTextField.getText())) {
                this.recordValueTextField.getStyleClass().add("text-field-error");
                this.recordValueTextField.getStyleClass().removeAll("text-field-success");
                this.saveButton.setDisable(true);
            } else {
                this.recordValueTextField.getStyleClass().removeAll("text-field-error");
                this.recordValueTextField.getStyleClass().add("text-field-success");
                this.saveButton.setDisable(false);
            }
        }
    }

    public void passwordKeyPress(){

        if(!MyHealth.getInstance().getUser().getPassword().equals(User.hash(this.deletePasswordInput.getText()))){
            this.deletePasswordInput.getStyleClass().add("text-field-error");
            this.deletePasswordInput.getStyleClass().removeAll("text-field-success");
            this.deleteRecordButton.setDisable(true);
        }else{
            this.deletePasswordInput.getStyleClass().removeAll("text-field-error");
            this.deletePasswordInput.getStyleClass().add("text-field-success");
            this.deleteRecordButton.setDisable(false);
        }

    }

    public void setRecord(Record record){
        this.record = record;
        this.recordTypeLabel.setText(record.getType()+" Record");
        this.recordDatePicker.setValue(record.getDate());
        this.recordValueTextField.setText(record.getValue());

        ObservableList<Comment> comment = Comment.get(MyHealth.getInstance().getUser().getId(),this.record.getId());

        if(comment.size() != 0){
            this.recordNotesTextArea.setText(comment.get(0).getNote());
            this.startingNote = comment.get(0).getNote();
        }else{
            this.startingNote = "";
        }


    }

    private Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                for (Record record: Record.where("type",MyHealth.getInstance().getSelectedRecordType()).fromCache().get()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: -fx-error;");

                    }
                }
            }
        };

        return dayCellFactory;
    }

    public Record getRecord(){
        return this.record;
    }
}
