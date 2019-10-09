package ru.javawebinar.topjava.repository;

import java.util.List;

public interface Repository<T> {
    T save(T entity);
    void delete(long id);
    List<T> findAll();
    T findById(long id);
}
