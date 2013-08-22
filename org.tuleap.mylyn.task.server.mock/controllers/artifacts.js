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

var artifact0 = undefined;
var artifact1 = undefined;
var artifact2 = undefined;

var error404 = undefined;

var files = [
  '../org.tuleap.mylyn.task.server.data/json/artifacts/artifact-0.json',
  '../org.tuleap.mylyn.task.server.data/json/artifacts/artifact-1.json',
  '../org.tuleap.mylyn.task.server.data/json/artifacts/artifact-2.json',
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
    	  artifact0 = jsonData;
      } else if (i === 1) {
    	  artifact1 = jsonData;
      } else if (i === 2) {
    	  artifact2 = jsonData;
      } else if (i === 3) {
        error404 = jsonData;
      }
    }
  };

  fs.readFile(file, 'utf-8', functionCreator(i, file));
}

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