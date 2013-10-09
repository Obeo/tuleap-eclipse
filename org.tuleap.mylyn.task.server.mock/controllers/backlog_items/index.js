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
  path: '/backlog_item_types/:backlogItemTypeId/backlog_items',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.list = {
  method: 'get',
  path: '/backlog_item_types/:backlogItemTypeId/backlog_items',
  description: 'Retrieve all the backlog items with a specific type',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.milestonesOptionsList = {
  method: 'options',
  path: '/milestones/:milestoneId/backlog_items',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.milestonesList = {
  method: 'get',
  path: '/milestones/:milestoneId/backlog_items',
  description: 'Retrieve all the backlog items of a specific milestone',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.milestonesEdit = {
  method: 'put',
  path: '/milestones/:milestoneId/backlog_items',
  description: 'Update the list of the backlog items of a specific milestone',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.topPlanningOptionsList = {
  method: 'options',
  path: '/top_plannings/:topPlanningId/backlog_items',
  description: 'Description of the route',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.topPlanningList = {
  method: 'get',
  path: '/top_plannings/:topPlanningId/backlog_items',
  description: 'Retrieve all the backlog items from a specific top planning',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.topPlanningEdit = {
  method: 'put',
  path: '/top_plannings/:topPlanningId/backlog_items',
  description: 'Update the list of the backlog items of a specific top planning',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.options = {
  method: 'options',
  path: '/backlog_items',
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
  path: '/backlog_items/:backlogItemId',
  description: 'Retrieve a backlog item',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.edit = {
  method: 'put',
  path: '/backlog_items/:backlogItemId',
  description: 'Update a backlog item',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
      
      res.send();
    }
  };

exports.create = {
  method: 'post',
  path: '/backlog_items',
  description: 'Create a backlog item',
  authentificationRequired: true,
  behavior: function (req, res) {
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};