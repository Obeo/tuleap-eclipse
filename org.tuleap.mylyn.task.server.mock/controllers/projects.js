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

var projects = undefined;

var project0 = undefined;
var project1 = undefined;
var project2 = undefined;
var project3 = undefined;
var project4 = undefined;

var error404 = undefined;

var files = [
  '../org.tuleap.mylyn.task.server.data/json/projects/projects.json',
  '../org.tuleap.mylyn.task.server.data/json/projects/project-0.json',
  '../org.tuleap.mylyn.task.server.data/json/projects/project-1.json',
  '../org.tuleap.mylyn.task.server.data/json/projects/project-2.json',
  '../org.tuleap.mylyn.task.server.data/json/projects/project-3.json',
  '../org.tuleap.mylyn.task.server.data/json/projects/project-4.json',
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
        projects = jsonData;
      } else if (i === 1) {
        project0 = jsonData;
      } else if (i === 2) {
        project1 = jsonData;
      } else if (i === 3) {
        project2 = jsonData;
      } else if (i === 4) {
        project3 = jsonData;
      } else if (i === 5) {
        project4 = jsonData;
      } else if (i === 6) {
        error404 = jsonData;
      }
    }
  };

  fs.readFile(file, 'utf-8', functionCreator(i, file));
};

exports.optionsList = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');
  res.header('X-PAGINATION-LIMIT', '5');
  res.header('X-PAGINATION-OFFSET', '0');
  res.header('X-PAGINATION-SIZE', '5');

  res.send();
};

exports.list = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');
  res.header('X-PAGINATION-LIMIT', '5');
  res.header('X-PAGINATION-OFFSET', '0');
  res.header('X-PAGINATION-SIZE', '5');

  var response = projects;
  console.log(response);
  res.send(response);
};

exports.options = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

exports.get = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  var projectId = req.params.projectId;
  
  
  if (projectId === '0') {
    response = project0;
  } else if (projectId === '1') {
	  response = project1;
  } else if (projectId === '2') {
	  response = project2;
  } else if (projectId === '3') {
	  response = project3;
  } else if (projectId === '4') {
	  response = project4;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};