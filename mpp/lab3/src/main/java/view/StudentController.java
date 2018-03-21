package view;

import domain.Student;
import repository.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import service.Service;
import utils.ListEvent;
import utils.Observer;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StudentController implements Observer<Student> {

    private Service service;
    private ObservableList<Student> model;

    private StudentView view;

    public StudentController(Service service) {
        this.service = service;
        model = FXCollections.observableList(service.findAllListStudent());
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

    @Override
    public void notifyEvent(ListEvent<Student> e) {
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false)
                .collect(Collectors.toList()));
    }

    public StudentView getView() {
        return view;
    }

    public void setView(StudentView view) {
        this.view = view;
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    public void handleAddMessage(ActionEvent actionEvent) {
        Student toAdd = extractMessage();
        try
        {
            Student returned = service.saveObj(toAdd);
            if (returned == null)
            {
                showMessage(Alert.AlertType.INFORMATION,"Student adaugat","Studentul a fost adaugat.");
            }
            else
            {
                showErrorMessage("Exista deja un student cu ID-ul " + returned.getId());
            }
        }
        catch (ValidationException e)
        {
            showErrorMessage("Valdiation exception: " + e.getMessage());
        }
    }

    public void handleUpdateMessage(ActionEvent actionEvent) {
        Student toUpdate = extractMessage();
        try {
            Student returned = service.updateStudentObj(toUpdate);
            if (returned == null) {
                showMessage(Alert.AlertType.INFORMATION, "Student updatat", "Studentul cu id-ul " + toUpdate.getId() + " a fost updatat.");
            } else {
                showErrorMessage("Studentul cu ID-ul " + toUpdate.getId() + " nu a fost gasit.");
            }
        }
        catch (ValidationException e) {
            showErrorMessage("Validation exception: " + e.getMessage());
        }
    }

    public void handleClearFields(ActionEvent actionEvent) {
        clearFields();
    }

    public void clearFields() {
        view.txtfieldName.setText("");
        view.txtfieldGroup.setText("");
        view.txtfieldGuide.setText("");
        view.txtfieldEmail.setText("");
        view.txtfieldID.setText("");
    }

    public void handleDeleteMessage(ActionEvent actionEvent) {
        Student toDel = extractMessage();
        Student returned = service.deleteStudentObj(toDel);
        if (returned != null)
        {
            showMessage(Alert.AlertType.INFORMATION,"Student sters","Studentul cu id-ul " + returned.getId() + " a fost sters.");
        }
        else
        {
            showErrorMessage("Studentul cu ID-ul " + toDel.getId() + " nu a fost gasit.");
        }
    }

    private Student extractMessage() {
        String id = view.txtfieldID.getText();
        String name = view.txtfieldName.getText();
        String group = view.txtfieldGroup.getText();
        String email = view.txtfieldEmail.getText();
        String guide = view.txtfieldGuide.getText();
        return new Student(intConverter(id), name, group , email , guide);
    }

    static void showMessage(Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Eroare");
        message.setContentText(text);
        message.showAndWait();
    }


    public void showStudentDetails(Student newValue) {
        if (newValue != null) {
            view.txtfieldGuide.setText(newValue.getGuide());
            view.txtfieldName.setText(newValue.getName());
            view.txtfieldGroup.setText(newValue.getGroup());
            view.txtfieldEmail.setText(newValue.getEmail());
            view.txtfieldID.setText("" + newValue.getId());
        }
        else {
            clearFields();
        }
    }
}
