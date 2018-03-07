package controller;

import domain.Student;
import utils.ListEvent;
import utils.Observable;
import utils.Observer;

public class PrinterStudentController implements Observer<Student> {
    @Override
    public void notifyEvent(ListEvent<Student> e) {
        switch (e.getType())
        {
            case ADD:
                System.out.println("A fost adaugat un Student");
                break;
            case REMOVE:
                System.out.println("A fost sters un Student");
                break;
            case UPDATE:
                System.out.println("A fost modificat un Student");
                break;
        }
        e.getList().forEach(System.out::println);
    }
}
