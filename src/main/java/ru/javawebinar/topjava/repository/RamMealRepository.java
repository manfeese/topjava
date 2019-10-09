package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RamMealRepository implements Repository<Meal> {

    private final AtomicLong idCounter = new AtomicLong(0L);
    private final Map<Long, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == 0) {
            meal.setId(idCounter.incrementAndGet());
            meals.put(meal.getId(), MealsUtil.getCopy(meal));
            return meal;
        } else {
            return (meals.replace(meal.getId(), MealsUtil.getCopy(meal)) == null) ? null : meal;
        }
    }

    @Override
    public void delete(long id) {
        meals.remove(id);
    }

    @Override
    public List<Meal> findAll() {
        return meals.values().stream()
                .map(MealsUtil::getCopy)
                .collect(Collectors.toList());
    }

    @Override
    public Meal findById(long id) {
        return MealsUtil.getCopy(meals.get(id));
    }
}
