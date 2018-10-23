<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app="ejudgeStandings">
<head>
    <title>Title</title>
    <script src="../static/output.js"></script>
    <script src="../static/script.js"></script>
    <link rel="stylesheet" href="../static/style.css"/>
    <script src="../admin-app/app.js"></script>
    <script src="../admin-app/services.js"></script>
    <script src="../admin-app/controllers.js"></script>
</head>
<body>
<%--<nav class="navbar navbar-default">--%>
    <%--<div class="container-fluid">--%>
        <%--<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">--%>
            <%--<ul class="nav navbar-nav">--%>
                <%--<li><a href="/admin/teams">Teams Info</a></li>--%>
                <%--<li><a href="/admin/universities">Universities</a></li>--%>
                <%--<li><a href="/admin/contests">Contests</a></li>--%>
            <%--</ul>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</nav>--%>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Home</a>

    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
            <a class="nav-item nav-link" href="/admin/teams">Teams Info</a>
            <a class="nav-item nav-link" href="/admin/universities">Universities</a>
            <a class="nav-item nav-link" href="/admin/contests">Contests</a>
        </div>
    </div>
</nav>
<%--trololo--%>
<div id="content" style="margin: 0 15px;">
    <ng-view></ng-view>
</div>
</body>
</html>

