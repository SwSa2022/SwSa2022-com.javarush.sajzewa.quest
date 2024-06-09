<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Question</title>
    <meta charset="UTF-8">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h2>${question}</h2>
    <c:if test="${retry}">
        <p class="text-danger">Неправильный ответ. Попробуйте снова!</p>
    </c:if>
    <form action="game" method="post">
        <div class="form-group">
            <label>Ваш ответ:</label>
            <c:forEach var="answer" items="${answers}">
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="answer" id="answer_${answer}" value="${answer}" required>
                    <label class="form-check-label" for="answer_${answer}">
                            ${answer}
                    </label>
                </div>
            </c:forEach>
        </div>
        <button type="submit" class="btn btn-primary">Ответить</button>
    </form>
    <c:if test="${not empty correctAnswer}">
        <p>
            <c:choose>
                <c:when test="${correctAnswer eq selectedAnswer}">
                    Правильный ответ!
                </c:when>
                <c:otherwise>
                    Неправильный ответ. Правильный ответ: ${correctAnswer}
                </c:otherwise>
            </c:choose>
        </p>
    </c:if>
</div>
</body>
</html>


