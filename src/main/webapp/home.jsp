<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Standings</title>
    <link rel="stylesheet" href="/static/style.css"/>
</head>

<body>

<ul class="list-group">
    <c:forEach items="${contests}" var="contest">
        <li class="list-group-item"><a href="/contest/${contest.key}">${contest.value}</a></li>
    </c:forEach>
</ul>
</body>
</html>
