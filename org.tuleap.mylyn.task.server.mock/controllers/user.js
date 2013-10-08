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

var user = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/user.json').toString());
var error401 = JSON.parse(fs.readFileSync('../org.tuleap.mylyn.task.server.data/f_tests/errors/401.json').toString());

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
  res.send(user);
};