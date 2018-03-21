package controller;

import domain.Student;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import repository.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public class StudentControllerFX extends ControllerFX<Student> {
    @FXML
    private TextField idText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField groupText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField guideText;
    @FXML
    private CheckBox filNameCB;
    @FXML
    private CheckBox filGroupCB;
    @FXML
    private CheckBox filGuideCB;
    @FXML
    private TextField search;

    public StudentControllerFX() {}

    @Override
    @FXML
    public void initialize() {
        super.initialize();
        model = FXCollections.observableList(s.findAllListStudent());
        stRB.setSelected(true);
        stRB.setDisable(true);
        lst = s.findAllListStudent();

        table.getItems().setAll(lst);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                Student st = table.getSelectionModel().getSelectedItem();
                if (st != null && idText != null && !idText.getText().equals(st.getId().toString())) {
                    idText.setText(st.getId().toString());
                    nameText.setText(st.getName());
                    groupText.setText(st.getGroup());
                    emailText.setText(st.getEmail());
                    guideText.setText(st.getGuide());
                }
            }
        });
        s.addObserver(this);
    }

    public void prRBAction() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/project.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    public void grRBAction() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/grade.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    public void checkBoxAction() {
        List<Predicate<Student>> pred = new ArrayList<>();

        if (filNameCB.isSelected()) {
            pred.add(x -> Pattern.matches("(?i).*" + nameText.getText() + ".*", x.getName()));
        }

        if (filGroupCB.isSelected()) {
            pred.add(x -> x.getGroup().matches(".*" + groupText.getText() + ".*"));
        }

        if (filGuideCB.isSelected()) {
            pred.add(x -> Pattern.matches("(?i).*" + guideText.getText() + ".*", x.getGuide()));
        }

        table.getItems().setAll(s.filterStudents(pred));
        if (!search.getText().equals("")) {
            filterSearch();
        }
    }

    public void addBtnAction() {
        Student st = new Student(intConverter(idText.getText()), nameText.getText(), groupText.getText(), emailText.getText(), guideText.getText());
        try {
            Student resp = this.s.saveObj(st);
            if (resp != null) {
                handleError("The id already exists.");
            }
        }
        catch (ValidationException e) {
            handleError(e.getMessage());
        }
    }

    public void editBtnAction() {
        Student st = new Student(intConverter(idText.getText()), nameText.getText(), groupText.getText(), emailText.getText(), guideText.getText());

        try {
            Student resp = this.s.updateStudentObj(st);
            if (resp != null) {
                handleError("The id was not found.");
            }
        }
        catch (ValidationException e) {
            handleError(e.getMessage());
        }
    }

    public void deleteBtnAction() {
        Student st = new Student(intConverter(idText.getText()), nameText.getText(), groupText.getText(), emailText.getText(), guideText.getText());

        if (s.deleteStudentObj(st) == null) {
            handleError("ID not found/invalid.");
        }
    }

    public void filUncheckName() {
        filNameCB.setSelected(false);
    }

    public void filUncheckGroup() {
        filGroupCB.setSelected(false);
    }

    public void filUncheckGuide() {
        filGuideCB.setSelected(false);
    }

    private void filterSearch() {
        List<Predicate<Student>> pred = new ArrayList<>();

        pred.add(x -> Pattern.matches("(?i).*" + search.getText() + ".*", x.getId().toString()));

        table.getItems().setAll(intersection(s.filterStudents(pred), table.getItems()));
    }

    public void searchAction() {
        if (!search.getText().equals("")) {
            filterSearch();
        }
        else {
            checkBoxAction();
        }
    }
}
