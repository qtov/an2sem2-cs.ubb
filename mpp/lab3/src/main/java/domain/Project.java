package domain;

public class Project implements HasID<Integer> {
    private int id;
    private String desc;
    private int week;

    public Project(int _id, String _desc, int _week) {
        this.id = _id;
        this.desc = _desc;
        this.week = _week;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer _id) {
        this.id = _id;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String _desc) {
        this.desc = _desc;
    }

    public int getWeek() {
        return this.week;
    }

    public void setWeek(int _week) {
        this.week = _week;
    }

    public void incWeek() {
        ++this.week;
    }

    @Override
    public String toString() {
        return this.getId() + "; " + this.getDesc() + "; " + this.getWeek();
    }

    public String toStringNice() {
        return "Id: " + id + "\n" +
                "Description: " + desc + "\n" +
                "Deadline: " + week + "\n";
    }
}
