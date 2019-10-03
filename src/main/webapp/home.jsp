<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="utf-8" %>
<html ng-app="ejudgeStandings">
<head>
    <meta charset="UTF-8">
    <title>Standings</title>

    <script src="../static/scripts.js"></script>
    <link rel="stylesheet" href="../static/styles.css"/>
    <script src="../app/app.js"></script>
    <script src="../app/services.js"></script>
    <script src="../app/controllers.js"></script>
    <script src="../app/WSservices.js"></script>
</head>

<body>
    <div ng-view id = "ng-view"></div ng-view>


</body>
</html>
