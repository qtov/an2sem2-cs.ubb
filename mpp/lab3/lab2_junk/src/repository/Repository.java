package repository;

import java.util.ArrayList;
import java.util.Map;

public interface Repository<E, ID> {
    long size();
    E save(E entity) throws ValidationException;
    E delete(ID id);
    E findOne(ID id);
    Iterable<E> findAll();
    Map<ID, E> getAll();
    E update(E entity) throws ValidationException;
}
