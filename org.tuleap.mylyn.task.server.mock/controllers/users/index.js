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

exports.optionsList = {
  method: 'options',
  path: '/projects/:projectId/user_groups',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    res.header('X-PAGINATION-LIMIT', '5');
    res.header('X-PAGINATION-OFFSET', '0');
    res.header('X-PAGINATION-SIZE', '5');
    
    res.send();
  }
};

exports.list = {
  method: 'get',
  path: '/projects/:projectId/user_groups',
  description: 'Retrieve all the user groups for a specific project',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    res.header('X-PAGINATION-LIMIT', '5');
    res.header('X-PAGINATION-OFFSET', '0');
    res.header('X-PAGINATION-SIZE', '5');
    
    res.send();
  }
};

exports.options = {
  method: 'options',
  path: '/user_groups/:userGroupId',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.show = {
  method: 'get',
  path: '/user_groups/:userGroupId',
  description: 'Retrieve a user group',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.usersOptionsList = {
  method: 'options',
  path: '/user_groups/:userGroupId/users',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    res.header('X-PAGINATION-LIMIT', '5');
    res.header('X-PAGINATION-OFFSET', '0');
    res.header('X-PAGINATION-SIZE', '5');
    
    res.send();
  }
};

exports.usersList = {
  method: 'get',
  path: '/user_groups/:userGroupId/users',
  description: 'Retrieve all the users from a specific user group',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    res.header('X-PAGINATION-LIMIT', '5');
    res.header('X-PAGINATION-OFFSET', '0');
    res.header('X-PAGINATION-SIZE', '5');
    
    res.send();
  }
};

exports.userOptions = {
  method: 'options',
  path: '/users/:userId',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.userShow = {
  method: 'get',
  path: '/users/:userId',
  description: 'Retrieve an user',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};