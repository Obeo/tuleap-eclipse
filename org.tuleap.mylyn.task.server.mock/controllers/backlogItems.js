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

var epics = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/backlog_items/epics.json').toString());
var epic300 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/backlog_items/epic300.json').toString());
var epic301 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/backlog_items/epic301.json').toString());
var epic302 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/backlog_items/epic302.json').toString());
var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());

// backlogItems
exports.optionsList = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  res.send();
};

// backlog_items : list of ids of backlog_items
exports.list = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
  res.header('Access-Control-Allow-Headers',
      'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'OPTIONS, GET');

  var response = undefined;
  response = epics;
  res.send(response);
};

//backlog_items/:backlogItemId
exports.options = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

res.send();
};

//backlog_items/:backlogItemId : 
exports.get = function(req, res) {
res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
res.header('Access-Control-Allow-Headers',
   'Accept-Charset, Accept, Content-Type, Authorization');
res.header('Allow', 'OPTIONS, GET');

var response = undefined;
var backlogItemId = req.params.backlogItemId;

if (backlogItemId === '300') {
 response = epic300;
} else if (backlogItemId === '301') {
 response = epic301;
} else if (backlogItemId === '302') {
 response = epic302;
} else {
 res.status(404);
 response = error404;
}
res.send(response);
};
