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

var topPlannings = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/top_plannings_prj3.json').toString());
var milestones = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/milestones.json').toString());
var backlogItems = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/backlog_items.json').toString());
var topPlanning30 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/top_plannings/top_planning_30.json').toString());

var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());

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
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

exports.get = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
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
