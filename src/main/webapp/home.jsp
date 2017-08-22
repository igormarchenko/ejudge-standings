<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="utf-8" %>
<html ng-app="ejudgeStandings">
<head>
    <meta charset="UTF-8">
    <title>Standings</title>
    <script src="../static/script.js"></script>
    <link rel="stylesheet" href="../static/style.css"/>
    <script src="../app/app.js"></script>
    <script src="../app/services.js"></script>
    <script src="../app/controllers.js"></script>
    <script src="../app/WSservices.js"></script>
</head>

<body>
<div id="content" style="margin: 0 15px;">
    <ng-view></ng-view>
</div>
</body>
</html>
