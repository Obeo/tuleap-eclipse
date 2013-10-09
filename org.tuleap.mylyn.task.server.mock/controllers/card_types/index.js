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
  path: '/projects/:projectId/card_types',
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
  path: '/projects/:projectId/card_types',
  description: 'Retrieve all the card types from a specific project',
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
  path: '/card_types/:cardTypeId',
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
  path: '/card_types/:cardTypeId',
  description: 'Retrieve a card type',
  authentificationRequired: true,
  behavior: function (req, res) {    
    res.header('Access-Control-Allow-Methods', 'OPTIONS, GET');
    res.header('Access-Control-Allow-Headers', 'Accept-Charset, Accept, Content-Type, Authorization');
    res.header('Allow', 'OPTIONS, GET');
    
    res.send();
  }
};