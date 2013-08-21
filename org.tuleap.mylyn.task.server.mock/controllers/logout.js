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

var error401 = undefined;

fs.readFile('../org.tuleap.mylyn.task.server.data/json/errors/401.json', 'utf-8', function(err, data) {
  if (err) {
    console.log(err);
    return;
  }
  error401 = JSON.parse(data);
});

exports.options = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'POST, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'POST, OPTIONS');
  res.send();
};

exports.post = function (req, res) {
  res.header('Access-Control-Allow-Methods', 'POST, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
  res.header('Allow', 'POST, OPTIONS');

  // The authentification error should have been intercepted before
  if (req.headers['authorization'] === 'qljslqkdhqkhdqsjkhdqkhsdsq715d7hfg6h41f6') {
    res.send();
  } else {
    // Invalid session hash!
    response = error401;
    res.status(401);
    res.send(response);
  }
};