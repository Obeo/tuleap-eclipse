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

var artifact0 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/artifacts/artifact-0.json').toString());
var artifact1 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/artifacts/artifact-1.json').toString());
var artifact2 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/artifacts/artifact-2.json').toString());

var error404 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/404.json').toString());

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
  var artifactId = req.params.artifactId;
  
  if (artifactId === '0') {
    response = artifact0;
  } else if (artifactId === '1') {
    response = artifact1;
  } else if (artifactId === '2') {
    response = artifact2;
  } else {
    res.status(404);
    response = error404;
  }
  res.send(response);
};