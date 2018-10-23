<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <script src="static/output.js"></script>
    <script src="static/script.js"></script>
    <link rel="stylesheet" href="static/style.css"/>
</head>
<body>
<div class="panel panel-default col-md-8 col-md-offset-2">
    <div class="panel-body">

        <form class="form-horizontal" action="<c:url value='j_spring_security_check' />" method='POST'>
            <div class="form-group">
                <label for="login" class="col-sm-2 control-label">Login</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="login" placeholder="Login" name = "login">
                </div>
            </div>

            <div class="form-group">
                <label for="password" class="col-sm-2 control-label">Password</label>
                <div class="col-sm-10">
                    <input type="password" class="form-control" id="password" name = "password" placeholder="Password">
                </div>
            </div>

            <input type="hidden" name="${_csrf.parameterName}"
                   value="${_csrf.token}" />

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Sign in</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
