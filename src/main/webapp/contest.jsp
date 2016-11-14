<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Standings</title>
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
            $("body").keydown(function (event) {
                if (event.which == 13) {
                    event.preventDefault();
                }

                if (event.which == 78) {
                    angular.element('body').scope().unfrozeNext();
                }
            });
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
            <button type="button" class="btn btn-info" style="margin-top:30px;" data-toggle="modal"
                    data-target="#settings-modal">Settings
            </button>
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
    <tr ng-repeat="team in results | filter: {isDisplayed: true} track by $index" id="team{{team.contest_team_id}}">
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


<div class="modal fade" id="settings-modal" tabindex="-1" role="dialog" aria-labelledby="settings-modal"
     style="z-index: 2010 !important;">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Settings</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="regionSelector" class="col-sm-2 control-label">Region</label>
                        <div class="col-sm-10">
                            <ui-select multiple
                                       id="regionSelector"
                                       ng-model="selectors.regions"
                                       theme="bootstrap">
                                <ui-select-match allow-clear="true" placeholder="">
                                    {{$item}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="region in regionList | filter: $select.search">
                                    {{region}}
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="universityTypeSelector" class="col-sm-2 control-label">University Type</label>
                        <div class="col-sm-10">
                            <ui-select multiple
                                       id="universityTypeSelector"
                                       ng-model="selectors.universityTypes"
                                       theme="bootstrap">
                                <ui-select-match allow-clear="true" placeholder="">
                                    {{$item}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="university in universityTypes | filter: $select.search">
                                    {{university}}
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" ng-click="filter()"
                        onclick="$('#settings-modal').modal('hide')">Filter
                </button>
                <button type="button"
                        class="btn btn-warning"
                        ng-click="unfrozeResults()"
                        onclick="$('#settings-modal').modal('hide')">Unfreeze
                </button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
