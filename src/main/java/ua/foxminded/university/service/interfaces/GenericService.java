package ua.foxminded.university.service.interfaces;

import java.util.List;

public interface GenericService<T> {

    void add(T t);

    void update(int id, T t);

    void delete(int id);

    T find(int id);

    List<T> findAll();
}
