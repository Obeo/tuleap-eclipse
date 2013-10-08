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

var prj3_card_types = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/card_types/prj3_card_types.json').toString()); // id = 7000
var card_type_7000 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/card_types/task-7000.json').toString()); // id = 7000

var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());


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
    response = prj3_card_types;
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
  
  if (typeId === '7000') {
    response = card_type_7000;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};