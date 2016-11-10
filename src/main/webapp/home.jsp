<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="static/script.js"></script>
    <script src="static/StandingsControllerDeprecated.js"></script>
    <link rel="stylesheet" href="static/style.css"/>
    <style>
        body {
            padding: 15px;
        }

        .OK-cell {
            background-color: #4cae4c;
        }

        .WA-cell {
            background-color: #d9534f;
        }

        .unknown-cell {
            background-color: #eea236;
        }

        .empty-cell {
            background-color: #8c8c8c;
        }

        table.floatThead-table {
            border-top: none;
            border-bottom: none;
            background-color: #FFF;
        }

        .ui-select-search {
            width: auto !important;
        }

        th {
            text-align: center;
        }
    </style>
    <script>
        $(document).ready(function () {
            $("body").keydown(function (event) {
                if (event.which == 13) {
                    event.preventDefault();
                }

                if (event.which == 78) {
                    angular.element('body').scope().unFreezeNextSubmit();
                }
            });
        });
    </script>
    <title>Standings</title>
</head>

<body ng-app="standingsPage"
      ng-controller="standingsController"
      ng-init="init()">

<h1>Contest: {{contest.name}}</h1>
<div style="z-index: 1500; position: relative;">
    <div class="row">
        <div class="col-md-5">
            Last success:
            {{lastSuccess.team}} on problem {{lastSuccess.problem}} at time {{lastSuccess.time |
            secondsToDateTime | date:'HH:mm:ss'}}
            <br/>
            Last submit:
            {{lastSubmit.team}} on problem {{lastSubmit.problem}} at time {{lastSubmit.time |
            secondsToDateTime | date:'HH:mm:ss'}}
        </div>

        <div class="col-md-offset-11">
            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#settings-modal">
                Filters
            </button>
        </div>
    </div>

</div>

<table class="table table-bordered" id="results_table">
    <thead>
    <tr>
        <th>#</th>
        <th style='width:500px;'>Team</th>
        <%--<th>Universtiy</th>--%>
        <%--<th>University Type</th>--%>
        <th>Region</th>
        <th id="solvedTasks">Solved</th>
        <th>Penalty</th>
    </tr>
    </thead>
    <tbody id="results_body">
    </tbody>
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
                                       on-select="updatePage()"
                                       on-remove="updatePage()"
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
                                       on-select="updatePage()"
                                       on-remove="updatePage()"
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
            <div ng-show="isUserAuthorized()" class="modal-footer">
                <button type="button"
                        class="btn btn-warning"
                        ng-click="unFreezeResults()"
                        onclick="$('#settings-modal').modal('hide')">Unfreeze
                </button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
