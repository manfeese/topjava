package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.MealTestData.getExpectedListWith;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, 10, 20, 11, 00), "Ланч", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), getExpectedListWith(USER_MEALS, newMeal));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateCreate() throws Exception {
        service.create(new Meal(null, USER_MEAL.getDateTime(), "Ланч", 500), USER_ID);
    }

    @Test
    public void update() {
        Meal updated = new Meal(ADMIN_MEAL);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(800);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(ADMIN_MEAL_ID, ADMIN_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = new Meal(ADMIN_MEAL);
        updated.setId(1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(800);
        service.update(updated, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateWithAnotherUser() {
        Meal updated = new Meal(ADMIN_MEAL);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(800);
        service.update(updated, USER_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_MEAL_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), getExpectedListWithout(USER_MEALS, USER_MEAL));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWithAnotherUser() {
        service.delete(USER_MEAL_ID, ADMIN_ID);
    }

    @Test
    public void get() {
        Meal receivedMeal = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(receivedMeal, ADMIN_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWithAnotherUser() throws Exception {
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2015, 5, 30);
        LocalDate endDate = LocalDate.of(2015, 5, 30);
        assertMatch(service.getBetweenDates(startDate, endDate, USER_ID),
                getExpectedList(USER_MEALS, meal -> Util.isBetweenInclusive(meal.getDate(), startDate, endDate)));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), getExpectedList(USER_MEALS));
    }

}
