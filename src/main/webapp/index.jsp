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
</div>
</body>
</html>