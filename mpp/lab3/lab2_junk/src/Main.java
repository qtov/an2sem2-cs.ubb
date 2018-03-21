import controller.StudentControllerFX;
import domain.Grade;
import domain.Project;
import domain.Student;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import service.Service;
import ui.Console;
import view.StudentController;
import view.StudentView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/student.fxml"));
        primaryStage.setTitle("MAP Lab");
        primaryStage.setScene(new Scene(root, 850, 550));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
