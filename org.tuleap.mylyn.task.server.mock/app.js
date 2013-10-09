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


// Imports
var crypto = require('crypto')
var https = require("https");
var http = require("http");
var fs = require("fs")
var express = require('express');

// Creating the server and exporting it
var app = express();

module.exports = app;

// The version of the API to use
var currentMajor = -1;
var currentMinor = -1;

// The currently supported version of the API (eg. v5.17)
var currentlySupportedVersion = process.argv[2] || 'v3.14';

var indexOfVersion = currentlySupportedVersion.indexOf('v');
var indexOfDot = currentlySupportedVersion.indexOf('.');
if (indexOfVersion != -1 && indexOfDot != -1 && indexOfVersion < indexOfDot) {
  var currentMajorString = currentlySupportedVersion.substring(indexOfVersion + 1, indexOfDot);
  var currentMinorString = currentlySupportedVersion.substring(indexOfDot + 1);
  currentMajor = parseInt(currentMajorString);
  currentMinor = parseInt(currentMinorString);
}

var apiVersion = 'v' + currentMajor + '.' + currentMinor;

// Configuration
app.configure(function(){
  app.use(express.compress());
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
});

var apiPrefix = '/api/';

// Invalid version of the API
app.all('/*', function (req, res, next) {
  var url = req.url;
  if (url.indexOf(apiPrefix) != -1) {
    var paths = url.split('/');
    var apiVersionString = paths[2];
    
    indexOfVersion = apiVersionString.indexOf('v');
    indexOfDot = apiVersionString.indexOf('.');
    if (indexOfVersion != -1 && indexOfDot != -1 && indexOfVersion < indexOfDot) {
      var requestedMajorString = apiVersionString.substring(indexOfVersion + 1, indexOfDot);
      var requestedMinorString = apiVersionString.substring(indexOfDot + 1);
      var requestedMajor = parseInt(requestedMajorString);
      var requestedMinor = parseInt(requestedMinorString);
      
      console.log('Using API v' + requestedMajor + '.' + requestedMinor);
      console.log('APIs supported [v'+ currentMajor + '.' + currentMinor + ']');
      
      if (requestedMajor < currentMajor) {
        // Old deprecated version of the API -> Gone
        res.status(410);
        res.send({
          code: 410,
          message: 'Gone'
        });
      } else if (requestedMajor === currentMajor && requestedMinor < currentMinor) {
        // Redirect to current
        var newUrl = req.protocol + "://" + req.get('host') + paths[0] + '/' + paths[1] + '/';
        var urlSuffix = url.substring(paths[0].length + paths[1].length + paths[2].length + 3);
        if (urlSuffix.length > 0) {
          urlSuffix = '/' + urlSuffix;
        }
        newUrl = newUrl + apiVersion + urlSuffix;
        res.status(301);
        res.header('Location', newUrl);
        res.send({
          code: 301,
          message: 'Moved Permanently'
        });
      } else if (requestedMajor === currentMajor && requestedMinor === currentMinor) {
        // Use the current version
        next();
      } else {
        // 404 Unknown version of the API
        res.status(404);
        res.send({
          code: 404,
          message: 'Not Found'
        });
      }
    }
  } else {
    // 404 Not using the API
    res.status(404);
    var response = {
        code: 404,
        message: 'Not Found'
    };
    res.send(response);
  }
});

// Content type
app.all('/*', function (req, res, next) {
  res.header('Accept-Charset', 'utf-8');
  res.header('Accept', 'application/json');
  res.header('Content-Type', 'application/json, chartset=utf-8');

  // Log the headers of the request
  console.log(req.originalMethod + " " + req.protocol + "://" + req.get('host') + req.url);
  console.log("Headers:");
  console.log(JSON.stringify(req.headers, 0, 2));
  console.log("Body:");
  console.log(JSON.stringify(req.body, 0, 2));
  console.log("");
  
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
var auth = express.basicAuth(function(user, password) {
  console.log('user = ' + user + ', password = ' + password);
  return user != 'test' && password != 'test';
});

// Static files
app.use(express.static(__dirname + '/public'));

// Load the controllers dynamically
require('./routes/routes.js')(app, apiPrefix + apiVersion);

//BacklogItems
var backlogItems = require('./controllers/backlogItems.js');
//Getting the list of backlogItems
app.options('/api/' + apiVersion + '/backlog_items', backlogItems.optionsList);
app.get('/api/' + apiVersion + '/backlog_items', auth, backlogItems.list);
//Getting one backlogItem
app.options('/api/' + apiVersion + '/backlog_items/:backlogItemId', backlogItems.options);
app.get('/api/' + apiVersion + '/backlog_items/:backlogItemId', auth, backlogItems.get);

// 404 Since no middleware has responded
app.use(function(req, res, next){
  res.status(404);
  var response = {
    code: 404,
    message: 'Not Found - ' + req.originalMethod + " " + req.protocol + "://" + req.get('host') + req.url
  };
  res.send(response);
})

// HTTPS configuration
var options = {
  key: fs.readFileSync('openssl/privatekey.pem'),
  cert: fs.readFileSync('openssl/certificate.pem')
};

// Launch the server
// https.createServer(options, app).listen(3001);
app.listen(3001)
console.log("Express server listening on port 3001");
