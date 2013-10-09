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
  path: '/projects/:projectId/backlog_item_types',
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
  path: '/projects/:projectId/backlog_item_types',
  description: 'Retrieve all the backlog item types from a specific project',
  authentificationRequired: true,
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
  path: '/backlog_item_types/:backlogItemTypeId',
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
  path: '/backlog_item_types/:backlogItemTypeId',
  description: 'Retrieve a backlog item type',
  authentificationRequired: true,
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};
