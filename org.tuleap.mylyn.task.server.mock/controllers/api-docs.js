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

var apiDocs = undefined;
var milestonesDoc = undefined;
var planningsDoc = undefined;

var files = [
  '../org.tuleap.mylyn.task.server.data/f_tests/api-docs/api-docs.json',
  '../org.tuleap.mylyn.task.server.data/f_tests/api-docs/milestones.json',
  '../org.tuleap.mylyn.task.server.data/f_tests/api-docs/plannings.json'
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
    	  apiDocs = jsonData;
      } else if (i === 1) {
    	  milestonesDoc = jsonData;
      } else if (i === 2) {
    	  planningsDoc = jsonData;
      }
    }
  };

  fs.readFile(file, 'utf-8', functionCreator(i, file));
}

exports.options = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
  res.header('Allow', 'GET, OPTIONS');
  res.send();
};

exports.get = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
  res.header('Allow', 'GET, OPTIONS');
  
  res.send(apiDocs);
};

exports.milestones = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
  res.header('Allow', 'GET, OPTIONS');
  
  res.send(milestonesDoc);
};

exports.plannings = function(req, res) {
  res.header('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
  res.header('Allow', 'GET, OPTIONS');
  
  res.send(planningsDoc);
};
