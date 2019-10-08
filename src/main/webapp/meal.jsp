<%--
  Created by IntelliJ IDEA.
  User: manfeese
  Date: 08.10.2019
  Time: 5:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>${meal.id > 0 ? "Update meal" : "Add meal"}</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    Date Time   : <input required type="datetime-local" name="dateTime" value="${meal.dateTime}"/> <br />
    Description : <input required type="text" name="description" value="${meal.description}" /> <br />
    Calories    : <input required type="number" name="calories" min="0" value="${meal.calories}" /> <br />
    <input type="hidden" name="id" value="${meal.id}" />
    <input type="submit" value="OK"/>
    <input type="button" value="Cancel" onClick='location.href="meals"'>
</form>
</body>
</html>
