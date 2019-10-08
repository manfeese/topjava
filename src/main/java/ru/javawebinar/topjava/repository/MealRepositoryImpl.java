package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class MealRepositoryImpl implements MealRepository {
    private static AtomicLong idCounter = new AtomicLong(0L);
    private static final List<Meal> mealList = new CopyOnWriteArrayList(new ArrayList());

    public static MealRepository of(List<Meal> mealList) {
        MealRepository repository = new MealRepositoryImpl();
        mealList.forEach(repository::save);
        return repository;
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == 0) {
            meal.setId(idCounter.incrementAndGet());
            mealList.add(meal);
        } else {
            mealList.stream()
                    .filter(m -> m.getId() == meal.getId())
                    .findFirst()
                    .ifPresent(m -> {
                        mealList.set(mealList.indexOf(m), meal);
                    });
        }
    }

    @Override
    public void delete(long id) {
        mealList.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .ifPresent(mealList::remove);
    }

    @Override
    public List<Meal> findAll() {
        return mealList;
    }

    @Override
    public Meal findById(long id) {
        return mealList.stream()
                    .filter(m -> m.getId() == id)
                    .findFirst()
                    .orElse(null);
    }
}
