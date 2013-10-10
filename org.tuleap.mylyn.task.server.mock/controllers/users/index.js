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

var paginationLimit = 7;

var UserGroupModel = require('../../models/user_group.js');
var UserModel = require('../../models/user.js');

exports.optionsList = {
  method: 'options',
  path: '/projects/:projectId/user_groups',
  description: 'Description of the route',
  behavior: function (req, res) {    
    UserGroupModel.count({}, function (err, count) {      
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
  path: '/projects/:projectId/user_groups',
  description: 'Retrieve all the user groups for a specific project',
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
    
    UserGroupModel.find({}, null, options, function (err, userGroups) {      
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET, POST, DELETE');
      res.header('X-PAGINATION-LIMIT', options.limit);
      res.header('X-PAGINATION-OFFSET', options.skip);
      
      res.send(userGroups);
    });
  }
};

exports.create = {
  method: 'post',
  path: '/projects/:projectId/user_groups',
  description: 'Create a new user group',
  behavior: function (req, res) {
    var userGroupToCreate = req.body;
    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    if (userGroupToCreate.name != undefined) {
      var anUserGroup = new UserGroupModel(userGroupToCreate);
      anUserGroup.save();
      
      console.log('An user group has been create: id=' + anUserGroup.id);
      res.send(anUserGroup.id);
    }
    
    res.send({
      "code": 406,
      "message": "Not Acceptable - Cannot create the user group since its name is missing."
    });
  }
};

exports.removeAll = {
  method: 'delete',
  path: '/projects/:projectId/user_groups',
  description: 'Delete all the user groups',
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    UserGroupModel.find({}, null, null, function (err, userGroups) {
      var length = userGroups.length;
      for(var i = 0; i < userGroups.length; i++) {
        userGroups[i].remove();
      }
      
      res.send(length);
    });
  }
};

exports.options = {
  method: 'options',
  path: '/user_groups/:userGroupId',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    res.send();
  }
};

exports.show = {
  method: 'get',
  path: '/user_groups/:userGroupId',
  description: 'Retrieve a user group',
  behavior: function (req, res) {    
    UserGroupModel.find({"id": req.params.userGroupId}, function (err, userGroups) {      
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET, POST, DELETE');
      
      if (userGroups != undefined && userGroups.length === 1) {
        res.send(userGroups[0]);
      } else {        
        res.send({
          "code": 404,
          "message": "The user group does not exist"
        });
      }
    });
  }
};

exports.usersOptionsList = {
  method: 'options',
  path: '/user_groups/:userGroupId/users',
  description: 'Description of the route',
  behavior: function (req, res) {    
    UserModel.count({}, function (err, count) {      
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

exports.usersList = {
  method: 'get',
  path: '/user_groups/:userGroupId/users',
  description: 'Retrieve all the users from a specific user group',
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
    
    UserModel.find({}, null, options, function (err, users) {      
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET, POST, DELETE');
      res.header('X-PAGINATION-LIMIT', options.limit);
      res.header('X-PAGINATION-OFFSET', options.skip);
      
      res.send(users);
    });
  }
};

exports.usersCreate = {
  method: 'post',
  path: '/user_groups/:userGroupId/users',
  description: 'Create a new user',
  behavior: function (req, res) {    
    var userToCreate = req.body;
    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    if (userToCreate.identifier != undefined) {
      var anUser = new UserModel(userToCreate);
      anUser.save();
      
      console.log('An user has been create: id=' + anUser.id);
      res.send(anUser.id);
    }
    
    res.send({
      "code": 406,
      "message": "Not Acceptable - Cannot create the user since its identifier is missing."
    });
  }
};

exports.usersRemoveAll = {
  method: 'delete',
  path: '/user_groups/:userGroupId/users',
  description: 'Delete all users',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET, POST, DELETE');
    
    UserModel.find({}, null, null, function (err, users) {
      var length = users.length;
      for(var i = 0; i < users.length; i++) {
        users[i].remove();
      }
      
      res.send(length);
    });
  }
};

exports.userOptions = {
  method: 'options',
  path: '/users/:userId',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.userShow = {
  method: 'get',
  path: '/users/:userId',
  description: 'Retrieve an user',
  behavior: function (req, res) {
    UserGroupModel.find({"id": req.params.userGroupId}, function (err, users) {      
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET, POST, DELETE');
      
      if (users != undefined && users.length === 1) {
        res.send(users[0]);
      } else {        
        res.send({
          "code": 404,
          "message": "The user does not exist"
        });
      }
    });
  }
};