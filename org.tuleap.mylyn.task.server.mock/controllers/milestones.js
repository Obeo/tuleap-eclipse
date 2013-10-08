/*********************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 ********************************************************************************/
'use strict';

var fs = require('fs');

var releases = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/releases.json').toString());
var release200 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/release200.json').toString());
var release201 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/release201.json').toString());
var sprints_rel200 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/sprints_rel200.json').toString());
var sprint250 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/sprint250.json').toString());
var sprint251 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/sprint251.json').toString());
var sprint252 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/sprint252.json').toString());
var backlogItems_rel200 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/backlog_items_rel200.json').toString());
var sprints_rel201 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/sprints_rel201.json').toString());
var backlogItems_rel201 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/milestones/backlog_items_rel201.json').toString());
var cardwall250 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/cardwalls/cw_sprint250.json').toString());

var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());

// /milestones
exports.optionsList = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

// /milestones : list of ids of milestones
exports.list = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  response = releases;
  res.send(response);
};

///milestones/:milestoneId
exports.options = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

res.send();
};

///milestones/:milestoneId : contents of the sub-milestones
exports.get = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

var response = undefined;
var milestoneId = req.params.milestoneId;

if (milestoneId === '200') {
 response = release200;
} else if (milestoneId === '201') {
 response = release201;
} else if (milestoneId === '250') {
 response = sprint250;
} else if (milestoneId === '251') {
 response = sprint251;
} else if (milestoneId === '252') {
 response = sprint252;
} else {
 res.status(404);
 response = error404;
}
res.send(response);
};

// /milestones/:milestoneId/submilestones
exports.optionsSubmilestones = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

///milestones/:milestoneId/submilestones : list of the sub-milestones
exports.submilestones = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  var milestoneId = req.params.milestoneId;

  if (milestoneId === '200') {
    response = sprints_rel200;
  } else if (milestoneId === '201') {
    response = sprints_rel201;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};

// /milestones/:milestoneId/backlog_items
exports.optionsBacklogItems = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

// /milestones/:milestoneId/backlog_items
exports.backlogItems = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  var milestoneId = req.params.milestoneId;

  if (milestoneId === '200') {
    response = backlogItems_rel200;
  } else if (milestoneId === '201') {
    response = backlogItems_rel201;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};


///milestones/:milestoneId/cardwall
exports.optionsCardwall = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

res.send();
};

///milestones/:milestoneId/cardwall
exports.cardwall = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

var response = undefined;
var milestoneId = req.params.milestoneId;

if (milestoneId === '250') {
 response = cardwall250;
} else {
 res.status(404);
 response = error404;
}
res.send(response);
};
