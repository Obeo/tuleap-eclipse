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

var trackersPart1 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/trackers_part_1.json').toString());
var trackersPart2 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/trackers_part_2.json').toString());

var tracker0 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/tracker-0.json').toString());
var tracker1 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/tracker-1.json').toString());
var tracker2 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/tracker-2.json').toString());
var tracker3 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/tracker-3.json').toString());
var tracker4 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/tracker-4.json').toString());
var tracker5 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/trackers/tracker-5.json').toString());

var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());

exports.optionsList = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');
  res.header('X-PAGINATION-LIMIT', '5');
  res.header('X-PAGINATION-OFFSET', '0');
  res.header('X-PAGINATION-SIZE', '6');

  res.send();
};

exports.list = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');
  res.header('X-PAGINATION-LIMIT', '5');
  res.header('X-PAGINATION-OFFSET', '0');
  res.header('X-PAGINATION-SIZE', '6');

  var response = undefined;
  if (req.params.projectId === '3' && (req.headers['x-pagination-offset'] === '0' || req.headers['x-pagination-offset'] === undefined)) {
  	response = trackersPart1;
  } else if (req.params.projectId === '3' && req.headers['x-pagination-offset'] === '5') {
    res.header('X-PAGINATION-OFFSET', '5');
  	response = trackersPart2;
  } else {
  	response = error404;
  }
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
  var trackerId = req.params.trackerId;
  
  
  if (trackerId === '0') {
    response = tracker0;
  } else if (trackerId === '1') {
    response = tracker1;
  } else if (trackerId === '2') {
    response = tracker2;
  } else if (trackerId === '3') {
    response = tracker3;
  } else if (trackerId === '4') {
    response = tracker4;
  } else if (trackerId === '5') {
    response = tracker5;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};