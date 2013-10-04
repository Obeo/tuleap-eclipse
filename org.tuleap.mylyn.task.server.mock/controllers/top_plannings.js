/*******************************************************************************
 * Copyright (c) 2013 Obeo. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Obeo - initial API and implementation
 ******************************************************************************/
'use strict';

var fs = require('fs');

var topPlannings = undefined;
var topPlanning30 = undefined;
var milestones = undefined;
var backlogItems = undefined;

var error404 = undefined;

var files = [
    '../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/top_plannings_prj3.json',
    '../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/milestones.json',
    '../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/backlog_items.json',
    '../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/top_planning_30.json',
    '../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json'
];

for ( var i = 0; i < files.length; i++) {
  var file = files[i];

  var functionCreator = function(i, file) {
    return function(err, data) {
      if (err) {
        console.log(err);
        return;
      }
      var jsonData = JSON.parse(data);

      if (i === 0) {
        topPlannings = jsonData;
      } else if (i === 1) {
        milestones = jsonData;
      } else if (i === 2) {
        backlogItems = jsonData;
      } else if (i === 3) {
        topPlanning30 = jsonData;
      } else if (i === 4) {
        error404 = jsonData;
      }
    }
  };

  fs.readFile(file, 'utf-8', functionCreator(i, file));
}

// /projects/:projectId/top_plannings
exports.optionsList = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

// /projects/:projectId/top_plannings : list of ids of top plannings
exports.list = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  if (req.params.projectId === '3') {
    response = topPlannings;
  } else {
    response = error404;
  }
  res.send(response);
};

///top_plannings/:topPlanningId
exports.options = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

res.send();
};

exports.get = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

var response = undefined;
var topPlanningId = req.params.topPlanningId;

if (topPlanningId === '30') {
 response = topPlanning30;
} else {
 res.status(404);
 response = error404;
}
res.send(response);
};

// /top_plannings/:topPlanningId/milestones
exports.optionsMilestones = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

exports.milestones = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  var topPlanningId = req.params.topPlanningId;

  if (topPlanningId === '30') {
    response = milestones;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};

// /top_plannings/:topPlanningId/backlog_items
exports.optionsBacklogItems = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

exports.backlogItems = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  var topPlanningId = req.params.topPlanningId;

  if (topPlanningId === '30') {
    response = backlogItems;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};
