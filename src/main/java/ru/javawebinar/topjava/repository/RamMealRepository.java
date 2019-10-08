package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RamMealRepository implements MealRepository {

    private final AtomicLong idCounter = new AtomicLong(0L);
    private final Map<Long, Meal> meals = new ConcurrentHashMap<>();

    private RamMealRepository() {
        MealsUtil.getMealList().forEach(this::save);
    }

    private static class RamMealRepositorySingleton {
        private static final RamMealRepository INSTANCE = new RamMealRepository();
    }

    public static RamMealRepository getInstance() {
        return RamMealRepositorySingleton.INSTANCE;
    }

    @Override
    public Meal save(Meal meal) {
        long id = meal.getId();

        // create operation
        if (id == 0) {
            id = idCounter.incrementAndGet();
            meal.setId(id);
            meals.put(id, meal);
            return MealsUtil.getCopy(meal);
        }

        // update operation
        Meal oldValue;
        if ((oldValue = meals.get(id)) != null) {
            if (oldValue.nonEquals(meal)) {
                meals.put(id, meal);
                return MealsUtil.getCopy(meal);
            } else {
                return MealsUtil.getCopy(oldValue);
            }
        } else {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        meals.remove(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal findById(long id) {
        return MealsUtil.getCopy(meals.get(id));
    }
}
