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

var projects = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/projects/projects.json').toString());

var project0 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/projects/project-0.json').toString());
var project1 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/projects/project-1.json').toString());
var project2 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/projects/project-2.json').toString());
var project3 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/projects/project-3.json').toString());
var project4 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/projects/project-4.json').toString());

var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());

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