package repository;

import domain.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectValidator implements Validator<Project> {
    @Override
    public void validate(Project pr) throws ValidationException {
        List<String> error_msg = new ArrayList<>();

        if (pr.getId() < 0)
            error_msg.add("The id is invalid.");

        if (pr.getDesc().length() < 2)
            error_msg.add("Description should contain at least 2 characters.");

        if (pr.getWeek() > 14 || pr.getWeek() < 1)
            error_msg.add("Week should be between 1 and 14 (inclusive).");

        if (!error_msg.isEmpty())
            throw new ValidationException(String.join("\n", error_msg));
    }
}
