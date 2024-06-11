<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Text Quest</title>
    <meta charset="UTF-8">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        h4 {
            text-align: center;
            font-family: "Comic Sans MS", sans-serif;
            margin-bottom: 2px;
        }
        h5 {
            text-align: center;
            font-family: "Comic Sans MS", sans-serif;
            white-space: pre-wrap;
            margin-bottom: 2px;
        }
        .form-control {
            text-align: center;
            width: auto;
        }
        div.container{
            width: auto;
        }
    </style>
</head>
<body>
<div class="container">
    <h4>Добро пожаловать в текстовый квест! </h4>
    <h5>
        Ты отправляешься в удивительное путешествие по всем континентам нашей планеты. В каждой точке мира тебя будут встречать эксперты в своих областях, которые приготовили для тебя интересные вопросы. Ответив на каждый из них, ты узнаешь много нового и докажешь свои знания по географии.

        Начнем наше путешествие!
    </h5>

    <form action="start" method="post">
        <div class="form-group">
            <label for="playerName">Введите имя:</label>
            <input type="text" class="form-control" id="playerName" name="playerName" required>
        </div>
        <button type="submit" class="btn btn-primary">Старт</button>
    </form>
<%--    <div class="row mt-3">--%>
<%--        <div class="col">--%>
<%--            <a href="stats.jsp" class="btn btn-info">View Statistics</a>--%>
<%--        </div>--%>
<%--    </div>--%>
</div>

<footer class="footer mt-1 py-5 bg-light">
    <div class="container mt-1 py-2">
        <div class="card mt-1">
            <div class="card-header mt-0">
                Statistics:
            </div>
            <div class="card-body mt-1">
                <p>IP address: <c:out value="${sessionScope.ipAddress}"/></p>
                <p>Player: <c:out value="${sessionScope.playerName}"/></p>
                <p>Games played: <c:out value="${sessionScope.gamesPlayed}"/></p>
                <p>Wins: <c:out value="${sessionScope.wins}"/></p>
                <p>Loses: <c:out value="${sessionScope.losses}"/></p>
                <p>Session ID: <c:out value="${pageContext.session.id}"/></p>
            </div>
        </div>
    </div>
</footer>
</body>
</html>
