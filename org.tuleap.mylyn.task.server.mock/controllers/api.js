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

var api = undefined;

fs.readFile('../org.tuleap.mylyn.task.server.data/f_tests/api.json', 'utf-8', function(err, data) {
  if (err) {
    console.log(err);
    return;
  }
  api = JSON.parse(data);
});

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
  
  res.send(api);
};