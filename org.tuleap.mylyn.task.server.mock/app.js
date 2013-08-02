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

var express = require('express');
var app = module.exports = express();

app.configure(function(){
  app.use(express.compress());
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
});

// Content type
app.all('/*', function (req, res, next) {
	res.header('Accept-Charset', 'utf-8');
	res.header('Accept', 'application/json');
	res.header('Content-Type', 'application/json, chartset=utf-8');

  // Log the headers of the request
  console.log(req.headers);
  console.log(req.body);

  next();
});

// Cross Origin Resource Sharing Headers
app.all('/*', function (req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Credentials', 'true');
  next();
});

// Authentification error
app.all('/*', function (req, res, next) {
	next();
});

// API
var api = require('./controllers/api.js');
app.options('/api/v3.14', api.options);
app.get('/api/v3.14', api.get);

// Authentification
var login = require('./controllers/login.js');
app.options('/api/v3.14/login', login.options);
app.get('/api/v3.14/login', login.get);

var logout = require('./controllers/logout.js');
app.options('/api/v3.14/logout', logout.options);
app.post('/api/v3.14/logout', logout.post);

// Projects
var projects = require('./controllers/projects.js');
app.options('/api/v3.14/projects', projects.optionsList);
app.get('/api/v3.14/projects', projects.list);

app.options('/api/v3.14/projects/:projectId', projects.options);
app.get('/api/v3.14/projects/:projectId', projects.get);

// Trackers
var trackers = require('./controllers/trackers.js');
app.options('/api/v3.14/projects/:projectId/trackers', trackers.optionsList);
app.get('/api/v3.14/projects/:projectId/trackers', trackers.list);

app.options('/api/v3.14/trackers/:trackerId', trackers.options);
app.get('/api/v3.14/trackers/:trackerId', trackers.get);

// Launch the server
app.listen(3001);
console.log("Express server listening on port 3001");