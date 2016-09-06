<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="static/scripts.min.js"></script>
    <script src="static/StandingsController.js"></script>
    <link rel="stylesheet" href="static/styles.min.css"/>
    <style>
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
    </style>
</head>
<body ng-app="standingsPage"
      ng-controller="standingsController"
      ng-init="init()">
<h1>Contest: {{contest.name}}</h1>
<div class="row" style="z-index: 2000; position: relative;">
    <div class="col-md-6">
        Last Success:
        {{getLastSuccess().team}} on problem {{getLastSuccess().problem}} at time {{getLastSuccess().time | secondsToDateTime | date:'HH:mm:ss'}}
        <br/>
        Last submit:
        {{getLastSubmit().team}} on problem {{getLastSubmit().problem}} at time {{getLastSubmit().time | secondsToDateTime | date:'HH:mm:ss'}}
    </div>
    <div class="col-md-5">

        <ui-select multiple
                   id="regionSelector"
                   ng-model="regionSelector.data"
                   on-select="updatePage()"
                   on-remove = "updatePage()"
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
<table class="table table-bordered" id="results_table">
    <thead>
    <tr>
        <th>#</th>
        <th>Team</th>
        <th id="regionCol">Region</th>
        <th>Solved</th>
        <th>Penalty</th>
    </tr>
    </thead>
    <tbody id="results_body">
    </tbody>
</table>
</body>
</html>
