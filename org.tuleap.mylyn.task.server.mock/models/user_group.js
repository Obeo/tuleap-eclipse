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

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userGroupSchema = new Schema({
  id: int,
  name: String
});

module.exports = mongoose.model('UserGroup', userGroupSchema);