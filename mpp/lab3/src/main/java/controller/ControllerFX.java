package controller;

import domain.Grade;
import domain.Project;
import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import repository.*;
import service.Service;
import utils.ListEvent;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class ControllerFX<E> implements Observer<E> {
    @FXML protected AnchorPane rootpane;
    @FXML
    protected RadioButton prRB;
    @FXML
    protected RadioButton stRB;
    @FXML
    protected RadioButton grRB;
    @FXML
    protected TableView<E> table;
    protected Service s;
    protected Repository<Student, Integer> stRepo;
    protected Repository<Project, Integer> prRepo;
    protected Repository<Grade, String> grRepo;
    protected ObservableList<E> model;
    protected List<E> lst;

    @Override
    public void notifyEvent(ListEvent<E> e) {
        table.getItems().setAll(StreamSupport.stream(e.getList().spliterator(),false)
                .collect(Collectors.toList()));
    }

    @FXML
    public void initialize() {
        this.stRepo = new StudentFileRepository(new StudentValidator(), "students.txt");
        this.prRepo = new ProjectFileRepository(new ProjectValidator(), "projects.txt");
        this.grRepo = new GradeFileRepository(new GradeValidator(), "grades.txt");
        this.s = new Service(stRepo, prRepo, grRepo);
    }

    public Integer intConverter(String x) {
        int newX;
        try {
            newX = Integer.parseInt(x);
        } catch (NumberFormatException e) {
            newX = -1;
        }
        return newX;
    }

    public Float floatConverter(String x) {
        float newX;
        try {
            newX = Float.parseFloat(x);
        } catch (NumberFormatException e) {
            newX = -1;
        }
        return newX;
    }

    protected List<E> intersection(List<E> list1, List<E> list2) {
        List<E> list = new ArrayList<>();

        for (E t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    protected void handleError(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}
