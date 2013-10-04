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

var releases = undefined;
var release200 = undefined;
var release201 = undefined;
var sprints_rel200 = undefined;
var sprint250 = undefined;
var sprint251 = undefined;
var sprint252 = undefined;
var backlogItems_rel200 = undefined;
var sprints_rel201 = undefined;
var backlogItems_rel201 = undefined;

var error404 = undefined;

var files = [
  '../org.tuleap.mylyn.task.server.data/json/milestones/releases.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/release200.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/release201.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/sprints_rel200.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/sprint250.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/sprint251.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/sprint252.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/backlog_items_rel200.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/sprints_rel201.json',
  '../org.tuleap.mylyn.task.server.data/json/milestones/backlog_items_rel201.json',
  '../org.tuleap.mylyn.task.server.data/json/errors/404.json'
];

for (var i = 0; i < files.length; i++) {
  var file = files[i];

  var functionCreator = function (i, file) {
    return function(err, data) {
      if (err) {
        console.log(err);
        return;
      }
      var jsonData = JSON.parse(data);
      
      if (i === 0) {
    	  releases = jsonData;
      } else if (i === 1) {
        release200 = jsonData;
      } else if (i === 2) {
    	  release201 = jsonData;
      } else if (i === 3) {
    	  sprints_rel200 = jsonData;
      } else if (i === 4) {
    	  sprint250 = jsonData;
      } else if (i === 5) {
    	  sprint251 = jsonData;
      } else if (i === 6){
    	  sprint252 = jsonData;
      } else if (i === 7){
    	  backlogItems_rel200 = jsonData;
      } else if (i === 8){
    	  sprints_rel201 = jsonData;
      } else if (i === 9){
    	  backlogItems_rel201 = jsonData;
      } else {
    	  error404 = jsonData;
      }
    }
  };

  fs.readFile(file, 'utf-8', functionCreator(i, file));
}

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