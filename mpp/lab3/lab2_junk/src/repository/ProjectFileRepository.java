package repository;

import domain.Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProjectFileRepository extends AbstractFileRepository<Project, Integer> {
    public ProjectFileRepository(ProjectValidator _val, String _filename) {
        super(_val, _filename);
    }

    @Override
    Project buildEntity(String[] fields) {
        return new Project(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]));
    }
}
