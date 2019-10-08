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
    <table  border="1">
        <thead>
            <tr>
                <th>Date/Time</th>
                <th>Description</th>
                <th>Calories</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${meals}" var="meal">
                <tr style="color:${meal.excess ? 'red' : 'green'}">
                    <td>${meal.dateTime.format(TimeUtil.DATE_TIME_FORMATTER)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals?action=update&id=<c:out value="${meal.id}"/>">Update</a></td>
                    <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="meals?action=insert">Add meal</a></p>
</body>
</html>
