<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
        .block {
            display: inline-block;
        }

        input,
        label {
            display: block;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form id="filter" method="get" action="meals">
        <input type="hidden" name="action" value="filter">
        <div class="block">
            <label>From Date:
            <input type="date" name="startDate" value="${startDate}">
            </label>
        </div>
        <div class="block">
            <label>To Date:
            <input type="date" name="endDate" value="${endDate}">
            </label>
        </div>
        <div class="block">
            <label>From Time:
            <input type="time" name="startTime" value="${startTime}">
            </label>
        </div>
        <div class="block">
            <label>To Time:
            <input type="time" name="endTime" value="${endTime}">
            </label>
        </div>
    </form>
    <button type="submit" form="filter">Filter</button>
    <button onclick="window.location.href = 'meals'">Cancel</button>
    <br><br>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>${fn:formatDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>