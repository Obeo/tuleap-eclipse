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

var paginationLimit = 5;

var ProjectModel = require('../../models/project.js');

exports.optionsList = {
  method: 'options',
  path: '/projects',
  description: 'Description of the route',
  behavior: function (req, res) {
    // Compute the number of projects available
    ProjectModel.count({}, function (err, count) {
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
      res.header('Allow', 'OPTIONS, GET, POST, DELETE');
      res.header('X-PAGINATION-LIMIT', paginationLimit);
      res.header('X-PAGINATION-OFFSET', '0');
      res.header('X-PAGINATION-SIZE', count);
      
      res.send();
    });
  }
};

exports.list = {
  method: 'get',
  path: '/projects',
  description: 'Retrieve all the projects',
  authentificationRequired: true,
  behavior: function (req, res) {
    var offset = 0;
    var reqOffset = req.headers['X-PAGINATION-OFFSET'];
    if (reqOffset != undefined) {
      offset = reqOffset;
    }
    
    var limit = paginationLimit;
    var reqLimit = req.headers['X-PAGINATION-LIMIT'];
    if (reqLimit != undefined && reqLimit < limit) {
      limit = reqLimit;
    }
    
    var options = {
      skip: offset,
      limit: limit
    };
    
    ProjectModel.find({}, null, options, function (err, projects) {
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET, POST, DELETE');
      res.header('X-PAGINATION-LIMIT', options.limit);
      res.header('X-PAGINATION-OFFSET', options.skip);
            
      res.send(projects);
    });
  }
}

exports.create = {
  method: 'post',
  path: '/projects',
  description: 'Create a project [internal]',
  behavior: function (req, res) {    
    var projectToCreate = req.body;
    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    if (projectToCreate.name != undefined) {
      var aProject = new ProjectModel(projectToCreate);
      aProject.save();
      
      console.log('A project has been created: id=' + aProject.id);
      
      res.send(aProject.id);
    }
    
    res.send({
      "code": 406,
      "message": "Not Acceptable - Cannot create the project since its name is missing."
    });
  }
};

exports.removeAll = {
  method: 'delete',
  path: '/projects',
  description: 'Remove all the projects',
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    ProjectModel.find({}, null, null, function (err, projects) {
      var length = projects.length;
      for(var i = 0; i < projects.length; i++) {
        projects[i].remove();
      }
      
      res.send(length);
    });
  }
};

exports.options = {
  method: 'options',
  path: '/projects/:projectId',
  description: 'Description of the route',
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
}

exports.show = {
  method: 'get',
  path: '/projects/:projectId',
  description: 'Retrieve a specific project',
  authentificationRequired: true,
  behavior: function (req, res) {
    ProjectModel.find({"id": req.params.projectId}, function (err, projects) {      
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET');
      
      if (projects != undefined && projects.length === 1) {        
        res.send(projects[0]);
      } else {
        res.send({
          "code": 404,
          "message": "The project does not exist"
        });
      }
    });
  }
}
