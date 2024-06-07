<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Text Quest</title>
    <meta charset="UTF-8">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet"></head>
<body>
<div class="container">
    <h1>Добро пожаловать в текстовый квест!</h1>
    <form action="start" method="post">
        <div class="form-group">
            <label for="playerName">Введите ваше имя:</label>
            <input type="text" class="form-control" id="playerName" name="playerName" required>
        </div>
        <button type="submit" class="btn btn-primary">Старт</button>
    </form>
    <!-- Link to the statistics page -->
        <div class="row mt-3">
            <div class="col">
                <a href="stats.jsp" class="btn btn-info">View Statistics</a>
            </div>
        </div>
</div>

<footer class="footer mt-1 py-1 bg-light">
    <div class="container">
        <div class="card mt-3">
            <div class="card-header">
                Statistics:
            </div>
            <div class="card-body">
                <p>IP address: <c:out value="${sessionScope.ipAddress}"/></p>
                <p>Player: <c:out value="${sessionScope.playerName}"/></p>
                <p>Game amount: <c:out value="${sessionScope.gamesPlayed}"/></p>
                <p>Win amount: <c:out value="${sessionScope.wins}"/></p>
                <p>Failure amount: <c:out value="${sessionScope.losses}"/></p>
                <p>Session ID: <c:out value="${session.id}"/></p>
            </div>
        </div>
    </div>
</footer>
</body>
</html>