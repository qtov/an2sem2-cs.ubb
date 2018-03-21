package repository;

import domain.Student;

public class StudentRepositoryInMemory extends AbstractRepository<Student, Integer> {
    public StudentRepositoryInMemory(StudentValidator _val) {
        super(_val);
    }
}
