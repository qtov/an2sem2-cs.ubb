package repository;

import domain.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentFileRepository extends AbstractFileRepository<Student, Integer> {
    public StudentFileRepository(StudentValidator _val, String _filename) {
        super(_val, _filename);
    }

    @Override
    Student buildEntity(String[] fields) {
        return new Student(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3], fields[4]);
    }
}
