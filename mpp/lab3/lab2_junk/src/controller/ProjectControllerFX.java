package controller;

import domain.Project;
import domain.Student;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import repository.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ProjectControllerFX extends ControllerFX<Project> {
    @FXML private CheckBox filDescCB;
    @FXML private CheckBox filLowWeekCB;
    @FXML private CheckBox filNewCB;
    @FXML private TextArea descText;
    @FXML private TextField idText;
    @FXML private TextField weekText;
    @FXML private TextField search;

    public ProjectControllerFX() {}

    @Override
    @FXML
    public void initialize() {
        super.initialize();
        model = FXCollections.observableList(s.findAllListProject());
        prRB.setSelected(true);
        prRB.setDisable(true);

        lst = s.findAllListProject();

        table.getItems().setAll(lst);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                Project pr = table.getSelectionModel().getSelectedItem();
                if (pr != null && idText != null && !idText.getText().equals(pr.getId().toString())) {
                    idText.setText(pr.getId().toString());
                    descText.setText(pr.getDesc());
                    weekText.setText(pr.getWeek() + "");
                }
            }
        });
        s.addObserverProject(this);
    }

    public void stRBAction() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/student.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    public void grRBAction() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/grade.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    public void checkBoxAction() {
        List<Predicate<Project>> pred = new ArrayList<>();

        if (filDescCB.isSelected()) {
            pred.add(x -> Pattern.matches("(?i).*" + descText.getText() + ".*", x.getDesc()));
        }

        if (filLowWeekCB.isSelected()) {
            pred.add(x -> x.getWeek() <= intConverter(weekText.getText()));
        }

        if (filNewCB.isSelected()) {
            Integer currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            currentWeek = currentWeek < 39 ? currentWeek + 13 : currentWeek - 39;
            final int curWeek = currentWeek;

            pred.add(x -> x.getWeek() > curWeek);
        }

        table.getItems().setAll(s.filterProjects(pred));
        if (!search.getText().equals("")) {
            filterSearch();
        }
    }

    public void filUncheckWeek() {
        filLowWeekCB.setSelected(false);
        filNewCB.setSelected(false);
    }

    public void filUncheckDesc() {
        filDescCB.setSelected(false);
    }

    public void addBtnAction() {
        Project pr = new Project(intConverter(idText.getText()), descText.getText(), intConverter(weekText.getText()));
        try {
            Project resp = this.s.addProjectObj(pr);
            if (resp != null) {
                handleError("The id already exists.");
            }
        }
        catch (ValidationException e) {
            handleError(e.getMessage());
        }
    }

    public void extBtnAction() {
        Project pr = new Project(intConverter(idText.getText()), descText.getText(), intConverter(weekText.getText()));

        try {
            int resp = this.s.extendDeadlineProject(pr);
            if (resp == 2) {
                handleError("The id was not found.");
            }
            else if (resp == 0) {
                handleError("Could not change week.");
            }
        }
        catch (ValidationException e) {
            handleError(e.getMessage());
        }
    }

    public void deleteBtnAction() {
        Project pr = new Project(intConverter(idText.getText()), descText.getText(), intConverter(weekText.getText()));

        if (s.deleteProjectObj(pr) == null) {
            handleError("ID not found/invalid.");
        }
    }

    private void filterSearch() {
        List<Predicate<Project>> pred = new ArrayList<>();

        pred.add(x -> Pattern.matches("(?i).*" + search.getText() + ".*", x.getId().toString()));

        table.getItems().setAll(intersection(s.filterProjects(pred), table.getItems()));
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
