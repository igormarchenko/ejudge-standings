<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app="ejudgeStandings">
<head>
    <title>Title</title>
    <script src="../static/script.js"></script>
    <link rel="stylesheet" href="../static/style.css"/>
    <script src="../admin-app/app.js"></script>
    <script src="../admin-app/services.js"></script>
    <script src="../admin-app/controllers.js"></script>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="/admin/team">Teams Info</a></li>
                <li><a href="/admin/universities">Universities</a></li>
                <li><a href="/admin/contestDEPRECATEDS">Contests</a></li>
            </ul>
        </div>
    </div>
</nav>
<div id="content" style="margin: 0 15px;">
    <ng-view></ng-view>
</div>
</body>
</html>

