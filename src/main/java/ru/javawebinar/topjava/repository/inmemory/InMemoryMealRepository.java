package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            log.info("save {}", meal);
            meal.setUserId(userId);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // treat case: update, but not present in storage
        if (getByUser(meal.getId(), userId) != null) {
            log.info("update {}", meal);
            meal.setUserId(userId);
            return repository.put(meal.getId(), meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (getByUser(id, userId) != null) {
            log.info("delete {}", id);
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return getByUser(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllByFilter(userId, meal -> meal.getUserId() == userId);
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll by date");
        return getAllByFilter(userId, (meal -> meal.getUserId() == userId
                    && DateTimeUtil.isBetween(meal.getDate(), startDate, endDate)));
    }

    private List<Meal> getAllByFilter(int userId, Predicate<? super Meal> filter) {
        return repository.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private Meal getByUser(int id, int userId) {
        Meal meal;
        if ((meal = repository.get(id)) != null
                && meal.getUserId() == userId) {
            return meal;
        }
        return null;
    }
}

