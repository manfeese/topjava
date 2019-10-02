package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = mealList.stream()
                .collect(Collectors.groupingBy(
                        UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> toUserMealWithExceed(
                                meal,
                                caloriesByDays.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            caloriesByDays.merge(
                    userMeal.getDate(),
                    userMeal.getCalories(),
                    Integer::sum);
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getTime(), startTime, endTime)) {
                userMealWithExceedList.add(
                        toUserMealWithExceed(
                                userMeal,
                                caloriesByDays.get(userMeal.getDate()) > caloriesPerDay));
            }
        }
        return userMealWithExceedList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOptional2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceedList = Collections.synchronizedList(new ArrayList<>());
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        for (UserMeal userMeal: mealList) {

            caloriesByDays.merge(
                    userMeal.getDate(),
                    userMeal.getCalories(),
                    Integer::sum);

            if (TimeUtil.isBetween(userMeal.getTime(), startTime, endTime)) {
                queue.add(() -> userMealWithExceedList.add(
                                    toUserMealWithExceed(
                                        userMeal,
                                        caloriesByDays.get(userMeal.getDate()) > caloriesPerDay)));
            }

        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, queue);
        executor.prestartAllCoreThreads();
        executor.shutdown();

        while (!executor.isTerminated()) {
        }

        return userMealWithExceedList;
    }

    public static UserMealWithExceed toUserMealWithExceed(UserMeal userMeal, boolean exeed) {
        return new UserMealWithExceed(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                exeed);
    }

}
