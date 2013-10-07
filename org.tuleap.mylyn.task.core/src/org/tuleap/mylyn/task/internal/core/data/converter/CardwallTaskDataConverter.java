/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.data.converter;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwallColumn;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapSwimlane;

/**
 * Class to inject a cardwall to a task data and extract a cardwall from a task data.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class CardwallTaskDataConverter {

	/**
	 * Populates the planning data in a task data from the given pojo.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param cardwall
	 *            The cardwall pojo.
	 */
	public void populateCardwall(TaskData taskData, TuleapCardwall cardwall) {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (TuleapCardwallColumn column : cardwall.getColumns()) {
			// TODO Validate that there's no need to compute an ID with TuleapTaskIdentityUtil
			wrapper.addColumn(Integer.toString(column.getId()), column.getLabel());
		}
		for (TuleapSwimlane swimlane : cardwall.getSwimlanes()) {
			TuleapBacklogItem backlogItem = swimlane.getBacklogItem();
			String swimlaneId = TuleapTaskIdentityUtil.getTaskDataId(backlogItem.getProjectId(), backlogItem
					.getConfigurationId(), backlogItem.getId());
			SwimlaneWrapper swimlaneWrapper = wrapper.addSwimlane(swimlaneId);
			for (TuleapCard card : swimlane.getCards()) {
				String cardId = TuleapTaskIdentityUtil.getTaskDataId(card.getProjectId(), card
						.getConfigurationId(), card.getId());
				CardWrapper cardWrapper = swimlaneWrapper.addCard(cardId);
				cardWrapper.setLabel(card.getLabel());
				// ID of assigned status must be computed like in the columnWrapper above
				cardWrapper.setStatusId(Integer.toString(card.getStatusId()));
				for (AbstractFieldValue fieldValue : card.getFieldValues()) {
					// TODO manage other types of fields
					if (fieldValue instanceof LiteralFieldValue) {
						cardWrapper.setFieldValue(Integer.toString(fieldValue.getFieldId()),
								((LiteralFieldValue)fieldValue).getFieldValue());
					}
				}
			}
		}
	}

}
