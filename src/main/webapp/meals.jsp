<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Date/Time</th>
                <th>Description</th>
                <th>Calories</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${meals}" var="meal">
                <tr style="color:${meal.excess ? 'red' : 'green'}">
                    <td>${meal.dateTime.format(TimeUtil.getDateTimeFormatter())}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
