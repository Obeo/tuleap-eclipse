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
  path: '/milestone_types/:milestoneTypeId/milestones',
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
  path: '/milestone_types/:milestoneTypeId/milestones',
  description: 'Retrieve all the milestones with a specific type',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.show = {
  method: 'get',
  path: '/milestones/:milestoneId',
  description: 'Retrieve a specific milestone',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

exports.edit = {
    method: 'put',
    path: '/milestones/:milestoneId',
    description: 'Update a milestone',
    behavior: function (req, res) {    
      res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
      res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
      res.header('Allow', 'OPTIONS, GET');
      
      res.send();
    }
};

exports.create = {
  method: 'post',
  path: '/milestones',
  description: 'Create a new milestone',
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};

