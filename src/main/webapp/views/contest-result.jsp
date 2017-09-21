<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page pageEncoding="utf-8" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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

    .cell-frozen {
        background-color: #2e66b9;
    }

    .animate-repeat {
        line-height: 40px;
        border-bottom: 1px solid white;
        -webkit-transition: 1s linear all;
        transition: 1s linear all;
    }

    .animate-repeat.ng-move,
    .animate-repeat.ng-enter,
    .animate-repeat.ng-leave {
        -webkit-transition: all linear 1s;
        transition: all linear 1s;
    }

    .animate-repeat.ng-leave.ng-leave-active,
    .animate-repeat.ng-move,
    .animate-repeat.ng-enter {
        opacity: 0;
        max-height: 0;
    }

    .animate-repeat.ng-leave,
    .animate-repeat.ng-move.ng-move-active,
    .animate-repeat.ng-enter.ng-enter-active {
        opacity: 1;
        max-height: 40px;
    }

    .animate-repeat.ng-leave-stagger {
        -webkit-transition-delay: 0s;
        transition-delay: 0s;

        -webkit-transition-duration: 0s;
        transition-duration: 0s;
    }

    .animate-repeat.ng-enter-stagger {
        -webkit-transition-delay: 0s;
        transition-delay: 0s;
        -webkit-transition-duration: 0s;
        transition-duration: 0s;
    }

</style>

<script type="text/ng-template" id="dialog1.tmpl.html">
    <md-dialog
            style="width: 800px;"
            aria-label="Filter teams">
        <form ng-cloak>
            <md-toolbar>
                <div class="md-toolbar-tools">
                    <h2>Filter teams</h2>
                    <span flex></span>
                    <md-button class="md-icon-button" ng-click="cancel()">
                        <md-icon md-svg-src="/static/images/ic_close_24px.svg" aria-label="Close dialog"></md-icon>
                    </md-button>
                </div>
            </md-toolbar>

            <md-dialog-content style="max-width:800px;max-height:810px;">
                <div class="md-dialog-content">
                    <md-input-container style="width:500px;">
                        <label>Region</label>

                        <md-select multiple="true" ng-model = "selectedRegions">
                            <md-option ng-value="region" ng-repeat="region in regions">{{region}}</md-option>
                        </md-select>
                    </md-input-container>

                    <md-input-container style="width:500px;">
                        <label>University type</label>
                        <md-select ng-model="selectedUniversityTypes" multiple="true">
                            <md-option ng-value="type" ng-repeat="type in universityTypes">{{type}}</md-option>

                        </md-select>
                    </md-input-container>

                </div>
            </md-dialog-content>

            <md-dialog-actions layout="row">
                <md-button ng-click="applyFilter()">
                    Apply filter
                </md-button>
            </md-dialog-actions>
        </form>
    </md-dialog>
</script>

<div class="row">
    <div class="col-md-10">
        <h2>{{contest.name}}</h2>
    </div>


    <sec:authorize access="hasAnyAuthority('ADMIN', 'OBSERVER')">
        <div class="pull-right" style="margin-right:20px;">
            <button type="button" class="btn btn-light" style="margin-top:30px; height: 40px;" ng-click="unfreeze()">
                <i class="fa fa-snowflake-o" style="font-size:24px; cursor: pointer;"></i>
            </button>
            <button type="button" class="btn btn-light" style="margin-top:30px; height: 40px;"
                    ng-click="redirectToExportPage()">
                <span class="glyphicon glyphicon-export"></span>
            </button>
        </div>
    </sec:authorize>

</div>

<div class="row">
    <div class="col-md-10">
        <h4>Contest start time: {{contest.startTime | amDateFormat:'HH:mm:ss DD.MM.YYYY'}}</h4>
        <h4>Contest stop time: {{contest.stopTime | amDateFormat:'HH:mm:ss DD.MM.YYYY'}}</h4>
    </div>
</div>

<div>
    <table class="table table-bordered results-table" id="resultsTable">
        <thead>
        <tr>
            <th>#</th>
            <th>Team <span class="glyphicon glyphicon-filter pull-right" style="cursor:pointer;"
                           ng-click="filterTeams()"></span></th>
            <th ng-repeat="task in contest.tasks track by $index" style="text-align: center" title="{{task.longName}}">
                {{task.shortName}}
            </th>
            <th width="50px">Solved</th>
        </tr>
        </thead>
        <tbody infinite-scroll='loadMore()' infinite-scroll-disabled="scrollDisabled"
               infinite-scroll-use-document-bottom="true">

        <tr ng-repeat="team in display | filter:teamDisplay track by team.participant.id" id="teamrow-{{team.participant.id}}"
            class="animate-repeat">
            <td width="40px">
                <h4>{{$index + 1}}</h4>
            </td>
            <td width="450px">
                <b>{{team.participant.name}}</b> <br/>
                <i>{{team.participant.university.name}}</i>
            </td>
            <td ng-repeat="task in ::contest.tasks track by $index"
                width="50px"
                valign="middle"
                ng-class="{undefined : 'cell-empty', 'EMPTY' : 'cell-empty', 'OK' : 'cell-ok', 'UNKNOWN' : 'cell-unknown', 'WA' : 'cell-wrong', 'TL' : 'cell-wrong', 'RT' : 'cell-wrong', 'PE' : 'cell-wrong', 'ML' : 'cell-wrong', 'SE' : 'cell-wrong', 'FROZEN' : 'cell-frozen'}[team.results[task.id].status]"
                class="text-center">
                <div ng-switch="team.results[task.id].status">
                    <div ng-switch-when="EMPTY|null|undefined" ng-switch-when-separator="|"></div>
                    <div ng-switch-when="OK"><b>+ {{(team.results[task.id].tries - 1 > 0) ? team.results[task.id].tries
                        :
                        ''}} </b>
                        <br/>
                        <i>
                            <small> {{formatTime(team.results[task.id].acceptedTime)}}</small>
                        </i>
                    </div>
                    <div ng-switch-when="FROZEN"><b>?</b>
                    </div>
                    <div ng-switch-default><b>-{{team.results[task.id].tries}}</b></div>
                </div>


            </td>
            <td width="50px">
                <b>{{team.solved}}</b>
                <br/>
                <i>{{team.penalty}}</i>
            </td>
        </tr>
        </tbody>

    </table>
</div>