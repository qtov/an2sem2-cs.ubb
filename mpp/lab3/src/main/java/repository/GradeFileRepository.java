package repository;

import domain.Grade;

import java.io.*;

public class GradeFileRepository extends AbstractFileRepository<Grade, String> {
    public GradeFileRepository(GradeValidator _val, String _filename) {
        super(_val, _filename);
    }

    @Override
    Grade buildEntity(String[] fields) {
        return new Grade(Integer.parseInt(fields[0].replaceFirst("\\.txt$", "")), Integer.parseInt(fields[1]), Float.parseFloat(fields[2]), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5]);
    }

    private void saveToFile(Grade e, String st) {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filename + e.getStId() + ".txt", true))) {
            br.write(st + e.toString() + "\n");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Grade save(Grade g) throws ValidationException {
        Grade gr = super.saveInMem(g);

        if (gr == null) {
            saveToFile(g, "Agaugare nota;");
        }

        return gr;
    }

    @Override
    public Grade update(Grade g) throws ValidationException {
        Grade gr = super.updateInMem(g);
        if (gr == null) {
            saveToFile(g, "Modificare nota;");
        }
        return gr;
    }
}
