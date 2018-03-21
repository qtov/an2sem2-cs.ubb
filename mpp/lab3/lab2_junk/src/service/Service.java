package service;

import domain.Grade;
import domain.Project;
import filter.FilterAndSorter;
import repository.Repository;
import domain.Student;
import repository.ValidationException;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Service implements Observable<Student> {
    private Repository<Student, Integer> stRepo;
    private Repository<Project, Integer> prRepo;
    private Repository<Grade, String> grRepo;
    private ArrayList<Observer<Student>> studentObservers = new ArrayList<>();
    private ArrayList<Observer<Project>> projectObservers = new ArrayList<>();
    private ArrayList<Observer<Grade>> gradeObservers = new ArrayList<>();

    public Service(Repository<Student, Integer> _stRepo, Repository<Project, Integer> _prRepo, Repository<Grade, String> _grRepo) {
        this.stRepo = _stRepo;
        this.prRepo = _prRepo;
        this.grRepo = _grRepo;
    }

    /**
     * Converts a string in int, -1 otherwise.
     *
     * @param x
     *   The string to convert.
     *
     * @return
     *   Returns the newly converted String as Integer.
     */
    public Integer intConverter(String x) {
        int newX;
        try {
            newX = Integer.parseInt(x);
        } catch (NumberFormatException e) {
            newX = -1;
        }
        return newX;
    }

    /**
     * Converts a string in float, -1 otherwise.
     *
     * @param x
     *   The string to convert.
     *
     * @return
     *   Returns the newly converted String as Float.
     */
    private Float floatConverter (String x) {
        float newX;
        try {
            newX = Float.parseFloat(x);
        } catch (NumberFormatException e) {
            newX = -1;
        }
        return newX;
    }

    /**
     * Adds project to repository.
     *
     * @param id
     *   id
     * @param desc
     *   description
     * @param deadline
     *   deadline
     *
     * @throws ValidationException
     *   If data is invalid.
     */
    public void addProject(String id, String desc, String deadline) throws ValidationException {
        int newId = intConverter(id);
        int newDeadline = intConverter(deadline);

        Project pr = new Project(newId, desc, newDeadline);
        this.prRepo.save(pr);
    }

    /**
     * Increments deadline by one if possible.
     *
     * @param _id
     *   Id of project.
     *
     * @return
     *   True if it was increased, false otherwise.
     */
    public boolean extendDeadline(String _id) throws ValidationException {
        Integer id = intConverter(_id);
        Project pr = this.prRepo.findOne(id);
        Integer currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        currentWeek = currentWeek < 39 ? currentWeek + 13 : currentWeek - 39;
        if (pr != null)
            if (currentWeek < pr.getWeek() && pr.getWeek() < 14)
            {
                pr.incWeek();
                if (this.prRepo.update(pr) == null)
                    return true;
            }
        return false;
    }

    public Iterable<Project> findAllProject() {
        return this.prRepo.findAll();
    }

    public Iterable<Grade> findAllGrade() {
        return this.grRepo.findAll();
    }

    /**
     * Adds Student to repo.
     *
     * @param id
     *   id
     * @param name
     *   name
     * @param group
     *   group
     * @param email
     *   email
     * @param guide
     *   guide
     *
     * @throws ValidationException
     *   If data is invalid.
     */
    public void save(String id, String name, String group, String email, String guide) throws ValidationException {
        Student st = new Student(intConverter(id), name, group, email, guide);
        this.stRepo.save(st);
    }

    public Student saveObj(Student st) throws ValidationException {
        Student s = this.stRepo.save(st);
        if (s == null) {
            ListEvent<Student> ev = createEvent(ListEventType.ADD, s, stRepo.findAll());
            notifyObservers(ev);
        }
        return s;
    }

    public Project addProjectObj(Project pr) throws ValidationException {
        Project p = this.prRepo.save(pr);
        if (p == null) {
            ListEvent<Project> ev = createEvent(ListEventType.ADD, p, prRepo.findAll());
            notifyObserversProject(ev);
        }
        return p;
    }

    public Grade addGradeObj(Grade gr) throws ValidationException {
        if (prRepo.findOne(gr.getPrId()) == null) {
            gr.setPrId(-1);
        }

        if (stRepo.findOne(gr.getStId()) == null) {
            gr.setStId(-1);
        }

        if (gr.getInWeek() > gr.getDeadline() + 2)
            gr.setValue(1);
        else if (gr.getInWeek() > gr.getDeadline())
            gr.setValue(gr.getValue() - 2 * (gr.getInWeek() - gr.getDeadline()));

        Grade g = this.grRepo.save(gr);

        if (g == null) {
            ListEvent<Grade> ev = createEvent(ListEventType.ADD, g, grRepo.findAll());
            notifyObserversGrade(ev);
        }
        return g;
    }

    public Grade updateGradeObj(Grade t) throws ValidationException {
        Grade r = grRepo.update(t);
        if (r == null) {
            ListEvent<Grade> ev = createEvent(ListEventType.UPDATE, r, grRepo.findAll());
            notifyObserversGrade(ev);
        }
        return r;
    }

    public Student deleteStudentObj(Student _st) {
        Student r = stRepo.delete(_st.getId());
        if (r != null) {
            ListEvent<Student> ev = createEvent(ListEventType.REMOVE, r, stRepo.findAll());
            notifyObservers(ev);
        }
        return r;
    }

    public Student updateStudentObj(Student t) throws ValidationException {
        Student r = stRepo.update(t);
        if (r == null) {
            ListEvent<Student> ev = createEvent(ListEventType.UPDATE, r, stRepo.findAll());
            notifyObservers(ev);
        }
        return r;
    }

    public int extendDeadlineProject(Project pr) throws ValidationException {
        Project t = this.prRepo.findOne(pr.getId());
        Integer currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        currentWeek = currentWeek < 39 ? currentWeek + 13 : currentWeek - 39;
        if (t != null) {
            if (currentWeek < t.getWeek() && t.getWeek() < 14) {
                t.incWeek();
                if (this.prRepo.update(t) == null) {
                    ListEvent<Project> ev = createEvent(ListEventType.UPDATE, t, prRepo.findAll());
                    notifyObserversProject(ev);
                    return 1;
                }
                return 0;
            }
            return 0;
        }
        return 2;
    }

    /**
     * Deletes a Student from repo.
     *
     * @param id
     *   id
     *
     * @return
     *   Returns the item deleted, null otherwise.
     */
    public Student delete(String id) {
        return this.stRepo.delete(intConverter(id));
    }

    /**
     * Updates the Student in repo.
     *
     * @param id
     *   id
     * @param name
     *   name
     * @param group
     *   group
     * @param email
     *   email
     * @param guide
     *   guide
     *
     * @return
     *   Returns null if the item was edited, the edited object otherwise.
     */
    public Student update(String id, String name, String group, String email, String guide) throws ValidationException {
        Student st = new Student(intConverter(id), name, group, email, guide);
        return this.stRepo.update(st);
    }

    public Iterable<Student> findAll() {
        return this.stRepo.findAll();
    }

    public Student findOneStudent(Integer id) {
        return stRepo.findOne(id);
    }

    public Project findOneProject(Integer id) {
        return prRepo.findOne(id);
    }

    public List<Student> findAllListStudent() {
        return new ArrayList<>(stRepo.getAll().values());
    }

    public List<Project> findAllListProject() {
        return new ArrayList<>(prRepo.getAll().values());
    }

    public List<Grade> findAllListGrade() {
        return new ArrayList<>(grRepo.getAll().values());
    }

    public void addGrade(String _stId, String _prId, String _value, String _inWeek, String _obs) throws ValidationException {
        int stId = intConverter(_stId);
        int prId = intConverter(_prId);
        float value = floatConverter(_value);
        int inWeek = intConverter(_inWeek);

        if (prRepo.findOne(prId) == null) {
            prId = -1;
        }

        if (stRepo.findOne(stId) == null) {
            stId = -1;
        }

        Project pr = prRepo.findOne(prId);

        if (inWeek > pr.getWeek() + 2)
            value = 1;
        else if (inWeek > pr.getWeek())
            value = value - 2 * (inWeek - pr.getWeek());

        Grade grade = new Grade(stId, prId, value, inWeek, pr.getWeek(), _obs);

        this.grRepo.save(grade);
    }

    public Grade updateGrade(String _stId, String _prId, String _value, String _inWeek, String _obs) throws ValidationException {
        int stId = intConverter(_stId);
        int prId = intConverter(_prId);
        float value = floatConverter(_value);
        int inWeek = intConverter(_inWeek);

        Project pr = prRepo.findOne(prId);

        if (inWeek > pr.getWeek() + 2)
            value = 1;
        else if (inWeek > pr.getWeek())
            value = value - 2 * (inWeek - pr.getWeek());

        Grade grade = new Grade(stId, prId, value, inWeek, pr.getWeek(), _obs);
        return this.grRepo.update(grade);
    }

    public Project deleteProject(String _id) {
        Integer id = intConverter(_id);
        return this.prRepo.delete(id);
    }

    public Project deleteProjectObj(Project pr) {
        Project r = prRepo.delete(pr.getId());
        if (r != null) {
            ListEvent<Project> ev = createEvent(ListEventType.REMOVE, r, prRepo.findAll());
            notifyObserversProject(ev);
        }
        return r;
    }

    public List<Student> filterStudentsGroup(String group) {
        List<Student> lst = new ArrayList<>();
        for (Student st : stRepo.findAll()) {
            lst.add(st);
        }

        Comparator<Student> stComp = Comparator.comparing(Student::getId);
        Predicate<Student> stPred = x -> x.getGroup().matches(".*" + group + ".*");
//        FilterAndSorter<Student> stFilter = new FilterAndSorter<>(lst, stPred, stComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Student> filterStudentsName(String name) {
        List<Student> lst = new ArrayList<>();
        for (Student st : stRepo.findAll()) {
            lst.add(st);
        }

        Comparator<Student> stComp = Comparator.comparing(Student::getName);
        Predicate<Student> stPred = x -> Pattern.matches("(?i).*" + name + ".*", x.getName());
//        FilterAndSorter<Student> stFilter = new FilterAndSorter<>(lst, stPred, stComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Student> filterStudentsGuide(String name) {
        List<Student> lst = new ArrayList<>();
        for (Student st : stRepo.findAll()) {
            lst.add(st);
        }

        Comparator<Student> stComp = Comparator.comparing(Student::getName);
        Predicate<Student> stPred = x -> Pattern.matches("(?i).*" + name + ".*", x.getGuide());
//        FilterAndSorter<Student> stFilter = new FilterAndSorter<>(lst, stPred, stComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Student> filterStudents(List<Predicate<Student>> pred) {
        List<Student> lst = new ArrayList<>();
        for (Student st : stRepo.findAll()) {
            lst.add(st);
        }

        Comparator<Student> stComp = Comparator.comparing(Student::getId);
        FilterAndSorter<Student> stFilter = new FilterAndSorter<>(lst, pred, stComp);
        return stFilter.doFilter();
    }

    public List<Project> filterProjectsDescA() {
        List<Project> lst = new ArrayList<>();
        for (Project pr : prRepo.findAll()) {
            lst.add(pr);
        }

        Comparator<Project> prComp = (x, y) -> x.getId() - y.getId();
        Predicate<Project> prPred = x -> Pattern.matches("(?i)a.*", x.getDesc());
//        FilterAndSorter<Project> stFilter = new FilterAndSorter<>(lst, prPred, prComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Project> filterProjectsLowerThanWeek(int week) {
        List<Project> lst = new ArrayList<>();
        for (Project pr : prRepo.findAll()) {
            lst.add(pr);
        }

        Comparator<Project> prComp = (x, y) -> x.getId() - y.getId();
        Predicate<Project> prPred = x -> x.getWeek() < week;
//        FilterAndSorter<Project> stFilter = new FilterAndSorter<>(lst, prPred, prComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Project> filterProjectsNew() {
        List<Project> lst = new ArrayList<>();
        for (Project pr : prRepo.findAll()) {
            lst.add(pr);
        }

        Integer currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        currentWeek = currentWeek < 39 ? currentWeek + 13 : currentWeek - 39;
        final int curWeek = currentWeek;

        Comparator<Project> prComp = (x, y) -> x.getId() - y.getId();
        Predicate<Project> prPred = x -> x.getWeek() > curWeek;
//        FilterAndSorter<Project> stFilter = new FilterAndSorter<>(lst, prPred, prComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Project> filterProjects(List<Predicate<Project>> pred) {
        List<Project> lst = new ArrayList<>();
        for (Project pr : prRepo.findAll()) {
            lst.add(pr);
        }

        Comparator<Project> prComp = Comparator.comparing(Project::getId);
        FilterAndSorter<Project> prFilter = new FilterAndSorter<>(lst, pred, prComp);
        return prFilter.doFilter();
    }

    public List<Grade> filterGrades1() {
        List<Grade> lst = new ArrayList<>();
        for (Grade gr : grRepo.findAll()) {
            lst.add(gr);
        }

        Comparator<Grade> grComp = (x, y) -> x.getId().compareTo(y.getId());
        Predicate<Grade> grPred = x -> x.getValue() == 1;
//        FilterAndSorter<Grade> stFilter = new FilterAndSorter<>(lst, grPred, grComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Grade> filterGradesStudent(int id) {
        List<Grade> lst = new ArrayList<>();
        for (Grade gr : grRepo.findAll()) {
            lst.add(gr);
        }

        Comparator<Grade> grComp = (x, y) -> x.getStId() - y.getStId();
        Predicate<Grade> grPred = x -> x.getStId() == id;
//        FilterAndSorter<Grade> stFilter = new FilterAndSorter<>(lst, grPred, grComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Grade> filterGradesOver5() {
        List<Grade> lst = new ArrayList<>();
        for (Grade gr : grRepo.findAll()) {
            lst.add(gr);
        }

        Comparator<Grade> grComp = (x, y) -> x.getStId() - y.getStId();
        Predicate<Grade> grPred = x -> x.getValue() >= 5;
//        FilterAndSorter<Grade> stFilter = new FilterAndSorter<>(lst, grPred, grComp);
//
//        return stFilter.doFilter();
        return null;
    }

    public List<Grade> filterGrades(List<Predicate<Grade>> pred) {
        List<Grade> lst = new ArrayList<>();
        for (Grade gr : grRepo.findAll()) {
            lst.add(gr);
        }

        Comparator<Grade> grComp = Comparator.comparing(Grade::getId);
        FilterAndSorter<Grade> grFilter = new FilterAndSorter<>(lst, pred, grComp);
        return grFilter.doFilter();
    }

    @Override
    public void addObserver(utils.Observer<Student> o) {
        studentObservers.add(o);
    }

    public void addObserverProject(utils.Observer<Project> o) {
        projectObservers.add(o);
    }

    public void addObserverGrade(utils.Observer<Grade> o) {
        gradeObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Student> o) {
        studentObservers.remove(o);
    }

    public void removeObserverProject(Observer<Project> o) {
        projectObservers.remove(o);
    }

    public void removeObserverGrade(Observer<Grade> o) {
        gradeObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        studentObservers.forEach(x -> x.notifyEvent(event));
    }

    public void notifyObserversProject(ListEvent<Project> event) {
        projectObservers.forEach(x -> x.notifyEvent(event));
    }

    public void notifyObserversGrade (ListEvent<Grade> event) {
        gradeObservers.forEach(x -> x.notifyEvent(event));
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }
}
