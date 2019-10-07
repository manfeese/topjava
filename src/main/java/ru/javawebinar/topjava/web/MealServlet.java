package ru.javawebinar.topjava.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.model.*;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final static Logger Log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log.debug("redirect to meals");

        List<MealTo> mealsTo = MealsUtil.getFiltered(
                MealsUtil.getMealList(),
                LocalTime.MIN,
                LocalTime.MAX,
                MealsUtil.DEFAULT_CALORIES_PER_DAY);

        request.setAttribute("meals", mealsTo);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/meals.jsp");
        dispatcher.forward(request, response);
    }
}
