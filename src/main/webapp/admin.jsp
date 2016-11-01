<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Igor
  Date: 01.11.2016
  Time: 11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="static/scripts.min.js"></script>
    <link rel="stylesheet" href="static/styles.min.css"/>
</head>
<body>
admin page
<sec:authentication property="principal.username" />
<br/>
<sec:authentication property="principal.password" />
<br/>
</body>
</html>

