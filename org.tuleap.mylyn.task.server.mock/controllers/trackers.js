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

var trackersPart1 = undefined;
var trackersPart2 = undefined;

var tracker0 = undefined;
var tracker1 = undefined;
var tracker2 = undefined;
var tracker3 = undefined;
var tracker4 = undefined;
var tracker5 = undefined;

var error404 = undefined;

var files = [
  '../org.tuleap.mylyn.task.server.data/json/trackers/trackers_part_1.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/trackers_part_2.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/tracker-0.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/tracker-1.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/tracker-2.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/tracker-3.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/tracker-4.json',
  '../org.tuleap.mylyn.task.server.data/json/trackers/tracker-5.json',
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
        trackersPart1 = jsonData;
      } else if (i === 1) {
        trackersPart2 = jsonData;
      } else if (i === 2) {
        tracker0 = jsonData;
      } else if (i === 3) {
        tracker1 = jsonData;
      } else if (i === 4) {
        tracker2 = jsonData;
      } else if (i === 5) {
        tracker3 = jsonData;
      } else if (i === 6) {
        tracker4 = jsonData;
      } else if (i === 7) {
        tracker5 = jsonData;
      } else if (i === 8) {
        error404 = jsonData;
      }
    }
  };

  fs.readFile(file, 'utf-8', functionCreator(i, file));
}

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