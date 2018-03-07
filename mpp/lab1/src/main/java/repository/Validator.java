package repository;

import repository.ValidationException;

public interface Validator<E> {
    void validate(E e) throws ValidationException;
}
