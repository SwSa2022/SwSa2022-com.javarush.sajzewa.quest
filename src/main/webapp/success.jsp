<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Success</title>
    <meta charset="UTF-8">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h1>Поздравляем!</h1>
    <p>Ты успешно прошел все этапы путешествия и узнал много нового о нашей планете.</p>
    <a href="restart" class="btn btn-secondary">На главную</a>
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
