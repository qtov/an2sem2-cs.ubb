package repository;

import domain.HasID;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<E extends HasID<ID>, ID> extends AbstractRepository<E, ID> implements Repository<E, ID> {
    protected String filename;

    AbstractFileRepository(Validator<E> _val, String _filename) {
        super(_val);
        this.filename = _filename;
        loadDataFileReader();
    }

    abstract E buildEntity(String[] fields);

    private void loadFile(String fullPath, boolean addFilename) {
        Path path = Paths.get(fullPath);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(line -> {
                if (line.compareTo("") != 0) {
                    String[] fields = line.split("; ");
                    String type = fields[0];
                    if (addFilename)
                    {
                        fields[0] = fullPath.replaceAll(".*\\\\", "");
                    }
                    E t = buildEntity(fields);
                    try {
                        if (type.matches("(?i)^adaugare.*"))
                            super.save(t);
                        else if (type.matches("(?i)^modificare.*"))
                            super.update(t);
                        else
                            super.save(t);
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("WTF");
            e.printStackTrace();
        }
    }

    private void loadDataFileReader() {
        File param = new File(filename);
        if (param.isFile()) {
            loadFile(filename, false);
        }
        else if (param.isDirectory()) {
            File[] listOfFiles = param.listFiles();
            for (File file : listOfFiles) {
                loadFile(file.toString(), true);
            }
        }
    }

    private void saveToFile(E e) {

        try (BufferedWriter br = new BufferedWriter(new FileWriter(filename, true))) {
            br.write(e.toString() + "\n");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public E saveInMem(E e) throws ValidationException {
        return super.save(e);
    }

    public E updateInMem(E e) throws ValidationException {
        return super.update(e);
    }

    @Override
    public E save(E entity) throws ValidationException {
        E e = super.save(entity);

        if (e == null) {
            saveToFile(entity);
        }

        return e;
    }

    private void rewriteFile() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filename, false))) {
            for (E e : findAll()) {
                br.write(e.toString() + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        rewriteFile();
        return e;
    }

    @Override
    public E update(E e) throws ValidationException {
        E entity = super.update(e);
        if (entity == null) {
            rewriteFile();
        }
        return entity;
    }
}
