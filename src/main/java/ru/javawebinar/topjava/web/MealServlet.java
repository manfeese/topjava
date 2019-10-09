package ru.javawebinar.topjava.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.Repository;
import ru.javawebinar.topjava.repository.RamMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.model.*;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String MEAL_LIST = "/meals.jsp";
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private Repository mealRepository;

    @Override
    public void init() throws ServletException {
        mealRepository = new RamMealRepository();
        MealsUtil.getMealList().forEach(mealRepository::save);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward;
        String action = request.getParameter("action");
        action = action == null ? "" : action.toLowerCase();

        switch (action) {
            case "delete": {
                long id = Long.parseLong(request.getParameter("id"));
                mealRepository.delete(id);
                response.sendRedirect("meals");
                return;
            }
            case "update": {
                forward = INSERT_OR_EDIT;
                long id = Long.parseLong(request.getParameter("id"));
                Meal meal = (Meal)mealRepository.findById(id);
                request.setAttribute("meal", meal);
                break;
            }
            case "insert": {
                forward = INSERT_OR_EDIT;
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                request.setAttribute("meal", new Meal(dateTime, "", 0));
                break;
            }
            default: {
                forward = MEAL_LIST;
                List<MealTo> mealsTo = MealsUtil.getFiltered(mealRepository.findAll(),
                        LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY);
                request.setAttribute("meals", mealsTo);
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        String id = request.getParameter("id");
        if (id != null) {
            meal.setId(Long.parseLong(id));
        }

        mealRepository.save(meal);
        response.sendRedirect("meals");
    }
}
