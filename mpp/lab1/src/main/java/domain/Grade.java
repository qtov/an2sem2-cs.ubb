package domain;

public class Grade implements HasID<String> {
    private String id;
    private Integer stId;
    private Integer prId;
    private float value;
    private Integer inWeek;
    private Integer deadline;
    private String obs;

    public Grade(Integer _stId, Integer _prId, float _value, Integer _inWeek, Integer _deadline, String _obs) {
        this.id = "" + _stId + " " + _prId;
        this.stId = _stId;
        this.prId = _prId;
        this.value = _value;
        this.inWeek = _inWeek;
        this.deadline = _deadline;
        this.obs = _obs;
    }

    @Override
    public void setId(String _id) {
        this.id = _id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setStId(Integer _id) {
        this.stId = _id;
    }

    public Integer getStId() {
        return this.stId;
    }

    public void setPrId(Integer _id) {
        this.prId = _id;
    }

    public Integer getPrId() {
        return this.prId;
    }

    public float getValue() {
        return this.value;
    }

    public Integer getInWeek() {
        return inWeek;
    }

    public void setInWeek(Integer inWeek) {
        this.inWeek = inWeek;
    }

    public void setValue(float _value) {
        this.value = _value;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getObs() {
        return obs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Grade grade = (Grade) o;

        return grade.getPrId().equals(this.getPrId()) && grade.getStId().equals(this.getStId());
    }

    @Override
    public String toString() {
        return stId + "; " + prId + "; " + value + "; " + inWeek + "; " + deadline + "; " + obs;
    }

    public String toStringNice() {
        return "Id project: " + prId + "\n" +
                "Given in week: " + inWeek + "\n" +
                "Deadline: " + deadline + "\n" +
                "Observations: " + obs + "\n";
    }
}
