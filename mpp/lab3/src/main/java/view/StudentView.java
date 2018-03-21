package view;

import domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StudentView {

    private StudentController ctrl;
    TextField txtfieldID = new TextField();
    TextField txtfieldName = new TextField();
    TextField txtfieldGroup = new TextField();
    TextField txtfieldEmail = new TextField();
    TextField txtfieldGuide = new TextField();

    public StudentView(StudentController ctrl) {
        this.ctrl = ctrl;
        initView();
    }

    private BorderPane borderPane;

    TableView<Student> tableView = new TableView<>();

    private void initView() {

        borderPane = new BorderPane();
        borderPane.setTop(initTop());
        borderPane.setLeft(initLeft());
        borderPane.setCenter(initCenter());

    }


    private AnchorPane initTop() {
        AnchorPane topAnchorPane = new AnchorPane();

        Label titleLabel = new Label("Student CRUD");
        topAnchorPane.getChildren().add(titleLabel);
        AnchorPane.setTopAnchor(titleLabel,20d);
        AnchorPane.setLeftAnchor(titleLabel,100d);
        titleLabel.setFont(new Font(30));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return topAnchorPane;
    }

    public AnchorPane initLeft(){
        AnchorPane leftAnchorPane = new AnchorPane();
        tableView = createStudentTableView();
        leftAnchorPane.getChildren().add(tableView);
        AnchorPane.setLeftAnchor(tableView,20d);
        AnchorPane.setRightAnchor(tableView,20d);
        AnchorPane.setTopAnchor(tableView,20d);
        AnchorPane.setBottomAnchor(tableView,20d);

        return  leftAnchorPane;
    }

    public AnchorPane initCenter() {
        AnchorPane centerAnchorPane = new AnchorPane();
        GridPane gridPane = createGridPane();
        AnchorPane.setRightAnchor(gridPane,20d);
        centerAnchorPane.getChildren().add(gridPane);

        HBox buttonZone = new HBox();
        Button addButton = new Button("Add");
        addButton.setOnAction(ctrl::handleAddMessage);
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(ctrl::handleDeleteMessage);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(ctrl::handleUpdateMessage);
        Button clearButton = new Button("Clear Fields");
        clearButton.setOnAction(ctrl::handleClearFields);
        buttonZone.getChildren().addAll(addButton,deleteButton,updateButton,clearButton);
        AnchorPane.setBottomAnchor(buttonZone,5d);

        centerAnchorPane.getChildren().add(buttonZone);
        return centerAnchorPane;
    }

    private GridPane createGridPane() {
        GridPane gp = new GridPane();
        Label labelID = createLabel("ID");
        Label labelName = createLabel("Nume");
        Label labelGroup = createLabel("Grupa");
        Label labelEmail = createLabel("Email");
        Label labelGuide = createLabel("Indrumator");
        gp.add(labelID,0,0);
        gp.add(this.txtfieldID,1,0);
        gp.add(labelName,0,1);
        gp.add(this.txtfieldName,1,1);
        gp.add(labelGroup,0,2);
        gp.add(this.txtfieldGroup,1,2);
        gp.add(labelEmail,0,3);
        gp.add(this.txtfieldEmail,1,3);
        gp.add(labelGuide,0,4);
        gp.add(this.txtfieldGuide,1,4);
        gp.setHgap(5);
        gp.setVgap(5);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPrefWidth(250);

        gp.getColumnConstraints().addAll(c1,c2);
        return gp;
    }


    private TableView<Student> createStudentTableView() {
        TableColumn<Student,String> colName = new TableColumn<>("Nume");
        TableColumn<Student,String> colGroup = new TableColumn<>("Grupa");
        TableColumn<Student,String> colEmail = new TableColumn<>("Email");
        TableColumn<Student,String> colGuide = new TableColumn<>("Indrumator");
        tableView.getColumns().addAll(colName, colGroup, colEmail, colGuide);
        colName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        colGroup.setCellValueFactory(new PropertyValueFactory<Student, String>("group"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        colGuide.setCellValueFactory(new PropertyValueFactory<Student, String>("guide"));
        tableView.setItems(ctrl.getModel());
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue)
            {
                ctrl.showStudentDetails(newValue);
            }
        });
        return tableView;
    }

    public BorderPane getView() {
        return borderPane;
    }

    private Label createLabel(String s) {
        Label l = new Label(s);
        l.setFont(new Font(15));
        l.setTextFill(Color.BLACK);
//        l.setStyle("-fx-font-weight: bold");
        return l;
    }

}
