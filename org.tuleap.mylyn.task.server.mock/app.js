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
  console.log(req.path);
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

// Authentication error
app.all('/*', function (req, res, next) {
	next();
});

// Authenticator
var auth = express.basicAuth(function(user, pass) {
  console.log("user = " + user);
  console.log("password = " + pass);
  return user === 'admin' && pass === 'password';
});

// API
var api = require('./controllers/api.js');
app.options('/api/v3.14', api.options);
app.get('/api/v3.14', api.get);

//User
var user = require('./controllers/user.js');
app.options('/api/v3.14/user', auth, user.options);
app.get('/api/v3.14/user', auth, user.get);

// Projects
var projects = require('./controllers/projects.js');
app.options('/api/v3.14/projects', auth, projects.optionsList);
app.get('/api/v3.14/projects', auth, projects.list);

app.options('/api/v3.14/projects/:projectId', auth, projects.options);
app.get('/api/v3.14/projects/:projectId', auth, projects.get);

// Trackers
var trackers = require('./controllers/trackers.js');
app.options('/api/v3.14/projects/:projectId/trackers', auth, trackers.optionsList);
app.get('/api/v3.14/projects/:projectId/trackers', auth, trackers.list);

app.options('/api/v3.14/trackers/:trackerId', auth, trackers.options);
app.get('/api/v3.14/trackers/:trackerId', auth, trackers.get);

// Artifacts
var artifacts = require('./controllers/artifacts.js');
app.options('/api/v3.14/artifacts/:artifactId', auth, artifacts.options);
app.get('/api/v3.14/artifacts/:artifactId', auth, artifacts.get);

// Milestone types
var milestone_types = require('./controllers/milestone_types.js');
// Getting all the milestone types of a project
app.options('/api/v3.14/projects/:projectId/milestone_types', auth, milestone_types.optionsList);
app.get('/api/v3.14/projects/:projectId/milestone_types', auth, milestone_types.list);
// Getting a milestone type by id
app.options('/api/v3.14/milestone_types/:typeId', auth, milestone_types.options);
app.get('/api/v3.14/milestone_types/:typeId', auth, milestone_types.get);

// Backlog item types
var milestone_types = require('./controllers/backlog_item_types.js');
// Getting all the milestone types of a project
app.options('/api/v3.14/projects/:projectId/backlog_item_types', auth, milestone_types.optionsList);
app.get('/api/v3.14/projects/:projectId/backlog_item_types', auth, milestone_types.list);
// Getting a milestone type by id
app.options('/api/v3.14/backlog_item_types/:typeId', auth, milestone_types.options);
app.get('/api/v3.14/backlog_item_types/:typeId', auth, milestone_types.get);

// --------------- API-DOCS ---------------
var apiDocs = require('./controllers/api-docs.js');
app.options('/api/v3.14/api-docs', apiDocs.options);
app.get('/api/v3.14/api-docs', apiDocs.get);
app.get('/api/v3.14/api-docs/milestones', apiDocs.milestones);
app.get('/api/v3.14/api-docs/plannings', apiDocs.plannings);


// Launch the server
app.listen(3001);
console.log("Express server listening on port 3001");
