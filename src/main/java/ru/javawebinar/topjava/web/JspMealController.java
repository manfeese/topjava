package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping
    public String getMeals(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        List<MealTo> mealsTo = MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
        request.setAttribute("meals", mealsTo);
        return "meals";
    }

    @GetMapping(params = "action=filter")
    public String getMealsBetween(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
        List<MealTo> mealsTo = MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);

        request.setAttribute("meals", mealsTo);
        return "meals";
    }

    @GetMapping(params = "action=create")
    public String toCreate(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping(params = "action=update")
    public String toUpdate(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        final Meal meal = service.get(getId(request), userId);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping(params = "id=")
    public String create(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        checkNew(meal);
        service.create(meal, userId);
        return "redirect:meals";
    }

    @PostMapping(params = "id")
    public String update(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        assureIdConsistent(meal, getId(request));
        service.update(meal, userId);
        return "redirect:meals";
    }

    @GetMapping(params = "action=delete")
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        service.delete(getId(request), userId);
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
