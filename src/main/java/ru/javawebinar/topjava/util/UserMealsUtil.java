package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        /*
        * key - Date of meal
        * value Pair<key, value>
        *           key - totalCaloriesPerDay
        *           value - List of objects Pair<key, value>
        *                                       key - index of meal int mealList
        *                                       value - index of mealWithExceed in userMealWithExceedList
        * */
        Map<LocalDate, Pair<Integer, List<Pair<Integer, Integer>>>> mealsByDayWithTotalCalories = new HashMap<>();

        for (int userMealIndex = 0; userMealIndex < mealList.size(); userMealIndex++) {

            UserMeal userMeal = mealList.get(userMealIndex);

            LocalDate mealDate = userMeal.getDate();
            Pair<Integer, List<Pair<Integer, Integer>>> keyValue =
                    mealsByDayWithTotalCalories.getOrDefault(
                        mealDate,
                        new Pair<>(0, new ArrayList<>()));

            /*
            * summing calories
            */
            keyValue.key += userMeal.getCalories();
            boolean exeed = keyValue.key > caloriesPerDay;

            if (TimeUtil.isBetween(userMeal.getTime(), startTime, endTime)) {
                userMealWithExceedList.add(toUserMealWithExceed(userMeal, exeed));
                if (!exeed) {
                    int userMealWithExceedIndex = userMealWithExceedList.size() - 1;
                    keyValue.value.add(new Pair<>(userMealIndex, userMealWithExceedIndex));
                }
            }

            /*
             * edit userMealWithExceedList
             */
            if (exeed && keyValue.value.size() > 0) {
                for (Pair<Integer, Integer> indexesPair : keyValue.value) {
                    userMealWithExceedList.set(indexesPair.value, toUserMealWithExceed(
                            mealList.get(indexesPair.key),
                            exeed
                    ));
                }
                keyValue.value.clear();
            }

            mealsByDayWithTotalCalories.put(mealDate, keyValue);
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

    private static class Pair<K, V> {
        K key;
        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
