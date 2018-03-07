package repository;

import domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentValidator implements Validator<Student> {
    /**
     * Validates if a string is email type.
     *
     * @param email
     *   Param to validate.
     *
     * @return
     *   Returns true if variable email is of email format.
     */
    private boolean validateEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * Validates if a string is a name.
     *
     * @param name
     *   The name to check
     *
     * @return
     *   Returns true if variable name is a name.
     */
    private boolean validateName(String name) {
        String ePattern = "^[a-zA-Z]+ [a-zA-Z]+[- ]?[a-zA-Z]+$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(name);

        return m.matches();
    }

    /**
     * Validates variable st.
     * @param st
     *   st - student to validate.
     *
     * @throws ValidationException
     *   Throws ValidationException if the validation fails.
     */
    @Override
    public void validate(Student st) throws ValidationException {
        List<String> error_msg = new ArrayList<>();

        if (st.getId() < 0)
            error_msg.add("The id is invalid.");

        if (!validateName(st.getName()))
            error_msg.add("The name is invalid.");

        if (st.getGroup().length() != 3)
            error_msg.add("The group is invalid.");

        if (!validateEmail(st.getEmail()))
            error_msg.add("The email is invalid.");

        if (!validateName(st.getGuide()))
            error_msg.add("The guide's name is invalid.");

        if (!error_msg.isEmpty())
            throw new ValidationException(String.join("\n", error_msg));
    }
}
