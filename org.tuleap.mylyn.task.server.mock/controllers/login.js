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

var login = undefined;
var error401 = undefined;

fs.readFile('../org.tuleap.mylyn.task.server.data/json/login/login.json', 'utf-8', function(err, data) {
  if (err) {
    console.log(err);
    return;
  }
  login = JSON.parse(data);
});

fs.readFile('../org.tuleap.mylyn.task.server.data/json/errors/401.json', 'utf-8', function(err, data) {
  if (err) {
    console.log(err);
    return;
  }
  error401 = JSON.parse(data);
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
  
  var response = undefined;

  var data = req.body;
  if (data.user_name === 'admin' && data.password === 'password') {
    response = login;
    res.send(response);
  } else {
    response = error401;
    res.status(401);
    res.send(response);
  }

};