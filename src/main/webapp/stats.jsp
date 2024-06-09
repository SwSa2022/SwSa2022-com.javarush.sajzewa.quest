<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Player Statistics</title>
    <meta charset="UTF-8">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="card">
        <div class="card-header">
            Statistics:
        </div>
        <div class="card-body">
            <p>IP address: <c:out value="${sessionScope.ipAddress}"/></p>
            <p>Player: <c:out value="${sessionScope.playerName}"/></p>
            <p>Games played: <c:out value="${sessionScope.gamesPlayed}"/></p>
            <p>Wins: <c:out value="${sessionScope.wins}"/></p>
            <p>Loses: <c:out value="${sessionScope.losses}"/></p>
            <p>Session ID: <c:out value="${sessionScope.session.id}"/></p>
        </div>
    </div>
    <!-- Back to Home Button -->
        <div class="mt-3">
            <a href="index.jsp" class="btn btn-primary">Back to Home</a>
        </div>
</div>
</body>
</html>
