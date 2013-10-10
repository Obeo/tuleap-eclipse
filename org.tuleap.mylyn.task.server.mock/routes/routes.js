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

var express = require('express')
var fs = require('fs');

module.exports = function(parent, apiPath) {
  console.log('Initializing routes...');
  
  var allRoutes = [];
  
  fs.readdirSync(__dirname + '/../controllers').forEach(function(controllersResourceName) {
    // We have a repository in "controllers", let's see all its javascript files
    
    fs.readdirSync(__dirname + '/../controllers/' + controllersResourceName).forEach(function (resourceName) {
      if (resourceName.indexOf('.js') != -1) {
      console.log('\n/controllers/' + controllersResourceName + '/' + resourceName);
        var app = express();

        // Load the module
        var loadedModule = require('./../controllers/' + controllersResourceName + '/' + resourceName);
        for (var key in loadedModule) {
          var routeEntry = loadedModule[key];
          
          // Register the route
          app[routeEntry.method](apiPath + routeEntry.path, routeEntry.behavior);
          
          var routeDescription = '   ' + routeEntry.method.toUpperCase() + ' ' + apiPath + routeEntry.path;
          
          var descriptionGap = 80;
          if (routeDescription.length < descriptionGap) {
            for(var i = routeDescription.length; i < descriptionGap; i++) {
              routeDescription = routeDescription + ' ';
            }
          }
          
          allRoutes.push({
            route: routeDescription.trim(),
            description: routeEntry.description.trim()
          });
          
          console.log(routeDescription + routeEntry.description);
        }
        
        // mount the app
        parent.use(app);
      }
    });
  });
  
  var app = express();
  
  app.options(apiPath, function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  });
  
  app.get(apiPath, function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send(allRoutes);
  });
  
  parent.use(app);
  
  console.log('/routes/routes.js');
  console.log('   ' + 'OPTIONS ' + apiPath);
  console.log('   ' + 'GET ' + apiPath);
  console.log();
};