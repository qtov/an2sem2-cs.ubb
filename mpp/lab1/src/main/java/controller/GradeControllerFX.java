package controller;

import domain.Grade;
import domain.Project;
import domain.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import repository.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class GradeControllerFX extends ControllerFX<Grade> {
    @FXML private TextField idStText;
    @FXML private TextField idPrText;
    @FXML private TextField valueText;
    @FXML private TextField weekText;
    @FXML private TextArea obsText;
    @FXML private TextField search;
    @FXML private TableColumn<Grade, String> studentName;
    @FXML private CheckBox filHighWeekCB;
    @FXML private CheckBox filLowWeekCB;
    @FXML private CheckBox filHighCB;
    @FXML private CheckBox filLowCB;

    public GradeControllerFX() {
    }

    @FXML
    public void initialize() {
        super.initialize();
        model = FXCollections.observableList(s.findAllListGrade());
        grRB.setSelected(true);
        grRB.setDisable(true);

        lst = s.findAllListGrade();

        table.getItems().setAll(lst);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent ev) {
                Grade gr = table.getSelectionModel().getSelectedItem();
                if (gr != null && idStText != null && !(gr.getId().equals(idStText.getText() + " " + idPrText.getText()))) {
                    idStText.setText(gr.getStId().toString());
                    idPrText.setText(gr.getPrId().toString());
                    valueText.setText(gr.getValue() + "");
                    weekText.setText(gr.getInWeek() + "");
                    obsText.setText(gr.getObs());
                }
            }
        });
        s.addObserverGrade(this);
        studentName.setCellValueFactory(c -> new SimpleStringProperty(s.findOneStudent(c.getValue().getStId()).getName()));
    }

    public void filUncheckValue() {
        filLowCB.setSelected(false);
        filHighCB.setSelected(false);
    }
    public void filUncheckWeek() {
        filHighWeekCB.setSelected(false);
        filLowWeekCB.setSelected(false);
    }

    public void addBtnAction() {
        Project prj = prRepo.findOne(intConverter(idPrText.getText()));
        if (prj == null) {
            handleError("Invalid project.");
            return;
        }

        int pr = prj.getWeek();

        Grade gr = new Grade(intConverter(idStText.getText()), intConverter(idPrText.getText()), floatConverter(valueText.getText()), intConverter(weekText.getText()), pr, obsText.getText());
        try {
            Grade resp = this.s.addGradeObj(gr);
            if (resp != null) {
                handleError("The id already exists.");
            }
        }
        catch (ValidationException e) {
            handleError(e.getMessage());
        }
    }

    public void editBtnAction() {
        int pr = prRepo.findOne(intConverter(idPrText.getText())).getWeek();
        Grade gr = new Grade(intConverter(idStText.getText()), intConverter(idPrText.getText()), floatConverter(valueText.getText()), intConverter(weekText.getText()), pr, obsText.getText());

        try {
            Grade resp = this.s.updateGradeObj(gr);
            if (resp != null) {
                handleError("The id was not found.");
            }
        }
        catch (ValidationException e) {
            handleError(e.getMessage());
        }
    }

    public void stRBAction() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../../resources/student.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    public void prRBAction() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../../resources/project.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    public void checkBoxAction() {
        List<Predicate<Grade>> pred = new ArrayList<>();

        if (filHighWeekCB.isSelected()) {
            pred.add(x -> x.getInWeek() >= intConverter(weekText.getText()));
        }

        if (filLowWeekCB.isSelected()) {
            pred.add(x -> x.getInWeek() <= intConverter(weekText.getText()));
        }

        if (filLowCB.isSelected()) {
            pred.add(x -> x.getValue() <= floatConverter(valueText.getText()));
        }

        if (filHighCB.isSelected()) {
            pred.add(x -> x.getValue() >= floatConverter(valueText.getText()));
        }

        table.getItems().setAll(s.filterGrades(pred));
        if (!search.getText().equals("")) {
            filterSearch();
        }
    }

    private void filterSearch() {
        List<Predicate<Student>> predSt = new ArrayList<>();

        predSt.add(x -> Pattern.matches("(?i).*" + search.getText() + ".*", x.getName()));
        List<Student> stLst = s.filterStudents(predSt);

        if (stLst != null) {
            List<Predicate<Grade>> pred = new ArrayList<>();

            StringBuilder match = new StringBuilder("");
            for (Student x : stLst) {
                match.append(x.getId()).append("|");
            }
            match.append("0");

            pred.add(x -> Pattern.matches("(?i).*" + match + ".*", x.getStId().toString()));
            try {
                List<Grade> grLst = s.filterGrades(pred);
                table.getItems().setAll(intersection(table.getItems(), grLst));
            }
            catch (IndexOutOfBoundsException e) {
                table.getItems().clear();
            }
        }
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
