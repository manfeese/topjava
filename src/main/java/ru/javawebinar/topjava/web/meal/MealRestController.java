package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService mealService;

    @Autowired
    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(mealService.getAll(authUserId()),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllByDates(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAll filtered");

        startDate = (startDate == null) ? LocalDate.MIN : startDate;
        startTime = (startTime == null) ? LocalTime.MIN : startTime;
        endDate = (endDate == null) ? LocalDate.MAX : endDate;
        endTime = (endTime == null) ? LocalTime.MAX : endTime;
        return MealsUtil.getFilteredTos(mealService.getAllByDates(authUserId(), startDate, endDate),
                MealsUtil.DEFAULT_CALORIES_PER_DAY,
                startTime,
                endTime);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return mealService.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return mealService.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        mealService.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        mealService.update(meal, authUserId());
    }
}