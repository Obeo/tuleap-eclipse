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

var backlog_item_types = undefined;

var epics = undefined; // id = 901
var userStories = undefined;  // id = 902

var error404 = undefined;

var files = [
  '../org.tuleap.mylyn.task.server.data/f_tests/backlog_item_types/types.json',
  '../org.tuleap.mylyn.task.server.data/f_tests/backlog_item_types/epics.json',
  '../org.tuleap.mylyn.task.server.data/f_tests/backlog_item_types/user_stories.json',
  '../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json'
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
        backlog_item_types = jsonData;
      } else if (i === 1) {
        epics = jsonData;
      } else if (i === 2) {
        userStories = jsonData;
      } else if (i === 3) {
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

  res.send();
};

exports.list = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  if (req.params.projectId === '3') {
  	response = backlog_item_types;
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
  var typeId = req.params.typeId;
  
  if (typeId === '801') {
    response = epics;
  } else if (typeId === '802') {
    response = userStories;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};