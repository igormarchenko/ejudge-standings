<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/static/script.js"></script>
    <script src="/app/StandingsController.js"></script>
    <link rel="stylesheet" href="/static/style.css"/>
    <style>
        .cell-ok {
            background-color: #2b9e2b;
        }

        .cell-wrong {
            background-color: #b34542;
        }

        .cell-unknown {
            background-color: #965f0c;
        }

        .cell-empty {
            background-color: #8c8c8c;
        }
    </style>
</head>
<body ng-app="standingsPage"
      ng-controller="standingsController"
      ng-init="init()">
<table class="table table-bordered" style="border:#929292 !important;">
    <tr ng-repeat="team in results track by $index">
        <td width="40px">
            <h4>{{$index + 1}}</h4>
        </td>
        <td width="450px">
            <b>{{team.name}}</b> <br/><i ng-if="team.university.name != '-'">{{team.university.name}} [{{team.university.region}}]</i>
        </td>
        <td ng-repeat="task in team.tasks track by $index"
            width="50px"
            valign="middle"
            ng-class="cellStyle(task.status)"
            ng-bind-html = "taskResult(task)"
            class = "text-center">

        </td>
        <td width="50px">
            <b>{{team.solved}}</b>
            <br/>
            <i>{{team.penalty}}</i>
        </td>
    </tr>
</table>
</body>
</html>
