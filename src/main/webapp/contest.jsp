<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

        table.floatThead-table {
            border-top: none;
            border-bottom: none;
            background-color: #FFF;
        }
    </style>
    <script>
        $(document).ready(function () {

        });
    </script>
</head>
<body ng-app="standingsPage"
      ng-controller="standingsController"
      ng-init="init()">
<div class="row">
    <div class="col-md-11">
        <h2>{{contest.name}} <br/>
            <small style="line-height: 32px;">Last success: {{contest.last_success.team.name}} at
                {{contest.last_success.time}}
                on problem
                {{contest.last_success.task}}
            </small>
            <br/>
            <small style="line-height: 32px;">Last submit: {{contest.last_submit.team.name}} at
                {{contest.last_submit.time}} on
                problem
                {{contest.last_submit.task}}
            </small>
        </h2>
    </div>
    <div class="col-md-1">
        <sec:authorize access="hasAuthority('ADMIN')">
        <button type="button" class="btn btn-info" style="margin-top:30px;">Settings</button>
        </sec:authorize>
    </div>
</div>

<table class="table table-bordered" id="resultsTable">
    <thead>
    <tr>
        <th>#</th>
        <th>Team</th>
        <th ng-repeat="task in tasks track by $index" style="text-align: center"> {{task.shortName}}</th>
        <th>Solved</th>
    </tr>
    </thead>
    <tr ng-repeat="team in results track by $index" id="team{{team.contest_team_id}}">
        <td width="40px">
            <h4>{{$index + 1}}</h4>
        </td>
        <td width="450px">
            <b>{{team.name}}</b> <br/><i ng-if="team.university.name != '-'">{{team.university.name}}
            [{{team.university.region}}]</i>
        </td>
        <td ng-repeat="task in team.tasks track by $index"
            width="50px"
            valign="middle"
            ng-class="cellStyle(task.status)"
            ng-bind-html="taskResult(task)"
            class="text-center">

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
