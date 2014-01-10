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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;
import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapComputedValue;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.ArtifactReference;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapColumn;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * Class to convert a milestone to task data and task data to milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class MilestoneTaskDataConverter {

	/**
	 * The task repository.
	 */
	private final TaskRepository taskRepository;

	/**
	 * The connector.
	 */
	private final ITuleapRepositoryConnector connector;

	/**
	 * Constructor.
	 * 
	 * @param taskRepository
	 *            The task repository to use.
	 * @param connector
	 *            The repository connector to use.
	 */
	public MilestoneTaskDataConverter(TaskRepository taskRepository, ITuleapRepositoryConnector connector) {
		this.taskRepository = taskRepository;
		this.connector = connector;
	}

	/**
	 * Populate the given task data with milestone data. Also, marks the task data kind with a value that
	 * means this task data represents a milestone.
	 * 
	 * @param taskData
	 *            Object to populate.
	 * @param milestone
	 *            Milestone POJO that contains the milestone-specific data.
	 * @param monitor
	 *            Monitor
	 */
	public void populateTaskData(TaskData taskData, TuleapMilestone milestone, IProgressMonitor monitor) {
		// TODO Add milestone-specific data?
	}

	/**
	 * Populates the planning data in a task data from the given pojo.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param cardwall
	 *            The cardwall pojo.
	 * @param project
	 *            The project.
	 * @param monitor
	 *            The progress monitor to use
	 */
	public void populateCardwall(TaskData taskData, TuleapCardwall cardwall, TuleapProject project,
			IProgressMonitor monitor) {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		int index = 0;
		for (TuleapColumn column : cardwall.getColumns()) {
			ColumnWrapper colWrapper = wrapper.addColumn(Integer.toString(column.getId()), column.getLabel());
			colWrapper.setColor(column.getColor());
		}
		for (TuleapSwimlane swimlane : cardwall.getSwimlanes()) {
			SwimlaneWrapper swimlaneWrapper = wrapper.addSwimlane(String.valueOf(index));
			swimlaneWrapper.setDisplayId(String.valueOf(String.valueOf(cardwall.getSwimlanes().indexOf(
					swimlane))));
			index++;
			for (TuleapCard card : swimlane.getCards()) {
				CardWrapper cardWrapper = swimlaneWrapper.addCard(card.getId());
				populateCard(cardWrapper, card, project);
			}
		}
	}

	/**
	 * Transfers data form the given card POJO to the given {@link CardWrapper}.
	 * 
	 * @param cardWrapper
	 *            The card wrapper.
	 * @param card
	 *            The card POJO.
	 * @param project
	 *            The card project.
	 */
	public void populateCard(CardWrapper cardWrapper, TuleapCard card, TuleapProject project) {
		cardWrapper.setLabel(card.getLabel());
		if (card.getColumnId() != null) {
			cardWrapper.setColumnId(String.valueOf(card.getColumnId()));
		}
		cardWrapper.setAccentColor(card.getAccentColor());

		int[] allowedColumnIds = card.getAllowedColumnIds();
		if (allowedColumnIds != null) {
			for (int columnId : allowedColumnIds) {
				cardWrapper.addAllowedColumn(String.valueOf(columnId));
			}
		}

		TuleapStatus status = card.getStatus();
		cardWrapper.setComplete(status == TuleapStatus.Closed);
		ArtifactReference artifact = card.getArtifact();
		int trackerId = artifact.getTracker().getId();
		int cardProjectId = card.getProject().getId();
		TuleapTaskId cardArtifactId = TuleapTaskId.forArtifact(cardProjectId, trackerId, artifact.getId());
		cardWrapper.setArtifactId(cardArtifactId.toString());
		// The card ID is different from the artifact ID, but we want to display the artifact ID
		TuleapTracker tracker = project.getTracker(artifact.getTracker().getId());
		if (tracker != null) {
			cardWrapper.setDisplayId(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.cardDisplayId, tracker.getLabel(), Integer.toString(artifact
							.getId())));
			// TODO Implement an init mechanism like artifact: create all card fields according to config
			// then populate the values of received fields.
			for (AbstractFieldValue fieldValue : card.getFieldValues()) {
				// TODO manage other types of fields
				int fId = fieldValue.getFieldId();
				String fieldId = Integer.toString(fId);
				AbstractTuleapField field = tracker.getFieldById(fId);
				if (field != null) {
					TaskAttribute fieldAtt = cardWrapper.addField(fieldId, field.getLabel(), field
							.getMetadataType());
					// TODO Find a way to put this responsibility in the Field classes
					// Set the field read-only like for tickets
					// @see AbstractTuleapField.initializeMetaData()
					if (!field.isUpdatable() && !field.isUpdatable() || field instanceof TuleapComputedValue) {
						fieldAtt.getMetaData().setReadOnly(true);
					}
					populateCardField(tracker, fieldValue, field, fieldAtt);
				} else {
					TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.cardTrackerConfigNeedsUpdate, Integer
									.toString(trackerId)), true);
				}
			}
		} else {
			cardWrapper.setDisplayId(Integer.toString(artifact.getId()));
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.cardTrackerNotAvailable, Integer.toString(trackerId)), true);
		}
	}

	/**
	 * Populates a card field.
	 * 
	 * @param tracker
	 *            The tracker
	 * @param fieldValue
	 *            The field value
	 * @param field
	 *            The field
	 * @param fieldAtt
	 *            The field TaskAttribute
	 */
	private void populateCardField(TuleapTracker tracker, AbstractFieldValue fieldValue,
			AbstractTuleapField field, TaskAttribute fieldAtt) {
		if (fieldValue instanceof LiteralFieldValue) {
			fieldAtt.setValue(((LiteralFieldValue)fieldValue).getFieldValue());
		} else if (fieldValue instanceof BoundFieldValue) {
			BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
			for (Integer boundId : boundFieldValue.getValueIds()) {
				fieldAtt.addValue(String.valueOf(boundId));
			}
			if (field instanceof AbstractTuleapSelectBox) {
				AbstractTuleapSelectBox sb = (AbstractTuleapSelectBox)field;
				// The options map is immutable, we must add options one at a time
				for (TuleapSelectBoxItem entry : sb.getItems()) {
					fieldAtt.putOption(Integer.toString(entry.getIdentifier()), entry.getLabel());
				}
			} else {
				TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
						TuleapMylynTasksMessagesKeys.cardTrackerConfigNeedsUpdate, Integer.toString(tracker
								.getIdentifier())), true);
			}
		}
	}

	/**
	 * Extracts the list of backlog from a Milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            The milestone's task data
	 * @return The list of backlog backlogitems to send to the server for update.
	 */
	public List<TuleapBacklogItem> extractBacklog(TaskData taskData) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		for (BacklogItemWrapper biWrapper : wrapper.getAllBacklogItems()) {
			String assignedId = biWrapper.getAssignedMilestoneId();
			if (assignedId == null) {
				TuleapTaskId taskId = TuleapTaskId.forName(biWrapper.getId());
				int id = taskId.getArtifactId();
				int projectId = taskId.getProjectId();
				TuleapReference projectRef = new TuleapReference();
				projectRef.setId(projectId);
				TuleapBacklogItem bi = new TuleapBacklogItem(id, projectRef);
				bi.setInitialEffort(biWrapper.getInitialEffort());
				bi.setLabel(biWrapper.getLabel());
				bi.setType(biWrapper.getType());
				String status = biWrapper.getStatus();
				if (status != null) {
					bi.setStatus(TuleapStatus.valueOf(status));
				}

				String internalParentId = biWrapper.getParentId();
				if (internalParentId != null) {
					TuleapTaskId parentTaskId = TuleapTaskId.forName(internalParentId);
					int parentId = parentTaskId.getArtifactId();
					int trackerId = parentTaskId.getTrackerId();
					TuleapReference trackerRef = new TuleapReference();
					trackerRef.setId(trackerId);
					ArtifactReference parentref = new ArtifactReference(parentId, null, trackerRef);
					bi.setParent(parentref);
				}
				backlogItems.add(bi);
			}
		}
		return backlogItems;
	}

	/**
	 * Extracts the list of a submilestone content from a Milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            The milestone's task data.
	 * @param submilestoneId
	 *            the submilestone identifier to extract content.
	 * @return The list of submilestone content backlogitems to send to the server for update.
	 */
	public List<TuleapBacklogItem> extractContent(TaskData taskData, int submilestoneId) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		int assignedId = -1;
		for (BacklogItemWrapper biWrapper : wrapper.getAllBacklogItems()) {
			String assignedMilestoneId = biWrapper.getAssignedMilestoneId();
			if (assignedMilestoneId != null) {
				TuleapTaskId assignedTaskId = TuleapTaskId.forName(assignedMilestoneId);
				assignedId = assignedTaskId.getArtifactId();
			}
			if (assignedId == submilestoneId) {
				TuleapTaskId taskId = TuleapTaskId.forName(biWrapper.getId());
				int id = taskId.getArtifactId();
				int projectId = taskId.getProjectId();
				TuleapReference projectRef = new TuleapReference();
				projectRef.setId(projectId);
				TuleapBacklogItem bi = new TuleapBacklogItem(id, projectRef);
				bi.setInitialEffort(biWrapper.getInitialEffort());
				bi.setLabel(biWrapper.getLabel());
				bi.setType(biWrapper.getType());
				String status = biWrapper.getStatus();
				if (status != null) {
					bi.setStatus(TuleapStatus.valueOf(status));
				}

				String internalParentId = biWrapper.getParentId();
				if (internalParentId != null) {
					TuleapTaskId parentTaskId = TuleapTaskId.forName(biWrapper.getParentId());
					int parentId = parentTaskId.getArtifactId();
					int trackerId = parentTaskId.getTrackerId();
					TuleapReference trackerRef = new TuleapReference();
					trackerRef.setId(trackerId);
					ArtifactReference parentref = new ArtifactReference(parentId, null, trackerRef);
					bi.setParent(parentref);
				}

				backlogItems.add(bi);
			}
		}
		return backlogItems;
	}

	/**
	 * Extracts the list of submilestones from a Milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            The milestone's task data
	 * @return The list of milestones to send to the server for update.
	 */
	public List<TuleapMilestone> extractMilestones(TaskData taskData) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		List<TuleapMilestone> subMilestones = Lists.newArrayList();
		for (SubMilestoneWrapper subMilestoneWrapper : wrapper.getSubMilestones()) {

			TuleapTaskId subMilestoneTaskId = TuleapTaskId.forName(subMilestoneWrapper.getId());
			int id = subMilestoneTaskId.getArtifactId();
			int projectId = subMilestoneTaskId.getProjectId();
			TuleapReference projectRef = new TuleapReference();
			projectRef.setId(projectId);
			TuleapMilestone submilestone = new TuleapMilestone(id, projectRef);
			submilestone.setLabel(subMilestoneWrapper.getLabel());
			if (subMilestoneWrapper.getCapacity() != null) {
				submilestone.setCapacity(subMilestoneWrapper.getCapacity());
			}

			if (subMilestoneWrapper.getStartDate() != null) {
				submilestone.setStartDate(subMilestoneWrapper.getStartDate());
			}
			if (subMilestoneWrapper.getEndDate() != null) {
				submilestone.setEndDate(subMilestoneWrapper.getEndDate());
			}

			subMilestones.add(submilestone);

		}
		return subMilestones;
	}

	/**
	 * Extract the list of Cardwall cards that have at least one modification.
	 * 
	 * @param taskData
	 *            The task data
	 * @return The list of cards to send to the server for update.
	 */
	public List<TuleapCard> extractCards(TaskData taskData) {
		CardwallWrapper cardwallWrapper = new CardwallWrapper(taskData.getRoot());
		List<TuleapCard> cards = new ArrayList<TuleapCard>();
		for (SwimlaneWrapper swimlaneWrapper : cardwallWrapper.getSwimlanes()) {
			for (CardWrapper cardWrapper : swimlaneWrapper.getCards()) {
				TuleapCard card = extractCard(cardWrapper);
				if (card != null) {
					cards.add(card);
				}
			}
		}
		return cards;
	}

	/**
	 * Extracts a TuleapCard from a CardWrapper.
	 * 
	 * @param cardWrapper
	 *            The card Wrapper
	 * @return A {@link TuleapCard} object that contain modifications to send to the server, and can be null
	 *         if the card has no modification to send to the server.
	 */
	private TuleapCard extractCard(CardWrapper cardWrapper) {
		TuleapTaskId taskId = TuleapTaskId.forName(cardWrapper.getArtifactId());

		int trackerId = taskId.getTrackerId();
		TuleapReference trackerRef = new TuleapReference();
		trackerRef.setId(trackerId);

		TuleapTracker tracker = connector.getServer(taskRepository.getUrl()).getTracker(trackerId);

		int artifactId = taskId.getArtifactId();
		ArtifactReference artifactRef = new ArtifactReference(artifactId, null, trackerRef);
		artifactRef.setId(artifactId);

		int projectId = taskId.getProjectId();
		TuleapReference projectRef = new TuleapReference();
		projectRef.setId(projectId);

		TuleapCard card = null;
		if (cardWrapper.hasChanged()) {
			card = new TuleapCard(cardWrapper.getId(), artifactRef, projectRef);
			// TODO Label should not be updated, it is not editable yet!
			// For the time being, Tuleap returns an error 400
			card.setLabel(cardWrapper.getLabel());
			// if (cardWrapper.hasColumnIdChanged()) {
			card.setColumnId(Integer.valueOf(cardWrapper.getColumnId()));
			// }
			for (TaskAttribute attribute : cardWrapper.getFieldAttributes()) {
				if (cardWrapper.hasChanged(attribute)) {
					extractField(cardWrapper, tracker, card, attribute);
				}
			}
		}
		return card;
	}

	/**
	 * Extract a field from a {@link TaskAttribute} for a {@link CardWrapper} into a {@link TuleapCard}
	 * instance.
	 * 
	 * @param cardWrapper
	 *            the card wrapper
	 * @param tracker
	 *            the tracker configuration
	 * @param card
	 *            the {@link TuleapCard} instance to populate
	 * @param attribute
	 *            The {@link TaskAttribute} to extract
	 */
	private void extractField(CardWrapper cardWrapper, TuleapTracker tracker, TuleapCard card,
			TaskAttribute attribute) {
		String attributeId = cardWrapper.getFieldId(attribute);
		int fieldId = Integer.parseInt(attributeId);
		AbstractTuleapField field = tracker.getFieldById(fieldId);
		if (field instanceof TuleapMultiSelectBox) {
			List<Integer> valueIds = new ArrayList<Integer>();
			for (String strValue : attribute.getValues()) {
				valueIds.add(Integer.valueOf(strValue));
			}
			BoundFieldValue boundFieldValue = new BoundFieldValue(fieldId, valueIds);
			card.addFieldValue(boundFieldValue);
		} else if (field instanceof AbstractTuleapSelectBox) {
			// TODO Check if this works with JSON serialization!
			// bind_value_ids or bind_value_id?
			List<Integer> ids = new ArrayList<Integer>();
			for (String value : attribute.getValues()) {
				ids.add(Integer.valueOf(value));
			}
			BoundFieldValue boundFieldValue = new BoundFieldValue(fieldId, ids);
			card.addFieldValue(boundFieldValue);
		} else {
			String value = null;
			if (!attribute.getValues().isEmpty()) {
				value = attribute.getValue();
			}
			LiteralFieldValue fieldValue = new LiteralFieldValue(fieldId, value);
			card.addFieldValue(fieldValue);
		}
	}

	/**
	 * Retrieves the milestone artifact id from a milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            the task data.
	 * @return The milestone id.
	 */
	public int getMilestoneId(TaskData taskData) {
		return TuleapTaskId.forName(taskData.getTaskId()).getArtifactId();
	}

	/**
	 * Retrieves the project id from a milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            the task data.
	 * @return The milestone id.
	 */
	public int getProjectId(TaskData taskData) {
		return TuleapTaskId.forName(taskData.getTaskId()).getProjectId();
	}

	/**
	 * Populates the milestone backlog with the given list of backlogItems.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param backlog
	 *            The backlogItems list to add to the milestone backlog.
	 * @param monitor
	 *            The progress monitor to use.
	 */
	public void populateBacklog(TaskData taskData, List<TuleapBacklogItem> backlog, IProgressMonitor monitor) {
		MilestonePlanningWrapper milestonePlanning = new MilestonePlanningWrapper(taskData.getRoot());
		for (TuleapBacklogItem backlogItem : backlog) {
			int projectId;

			TuleapReference project = backlogItem.getProject();
			if (project != null) {
				projectId = project.getId();
			} else {
				projectId = Integer.parseInt(taskData.getRoot().getAttribute(TuleapArtifactMapper.PROJECT_ID)
						.getValue());
			}
			BacklogItemWrapper backlogItemWrapper = milestonePlanning.addBacklogItem(TuleapTaskId
					.forArtifact(projectId, 0, backlogItem.getId().intValue()).toString());
			backlogItemWrapper.setDisplayId(Integer.toString(backlogItem.getId().intValue()));

			backlogItemWrapper.setLabel(backlogItem.getLabel());
			backlogItemWrapper.setType(backlogItem.getType());
			if (backlogItem.getStatus() != null) {
				backlogItemWrapper.setStatus(backlogItem.getStatus().toString());
			}

			if (backlogItem.getInitialEffort() != null) {
				backlogItemWrapper.setInitialEffort(backlogItem.getInitialEffort());
			}
			if (backlogItem.getParent() != null) {
				backlogItemWrapper.setParent(TuleapTaskId.forArtifact(projectId, 0,
						backlogItem.getParent().getId()).toString(), Integer.toString(backlogItem.getParent()
						.getId()));
			}

		}
	}

	/**
	 * Add a submilestone with its given list of backlogItems.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param milestone
	 *            The milestone to add.
	 * @param milestoneContent
	 *            The backlogItems list to add to the milestone content.
	 * @param monitor
	 *            The progress monitor to use.
	 */
	public void addSubmilestone(TaskData taskData, TuleapMilestone milestone,
			List<TuleapBacklogItem> milestoneContent, IProgressMonitor monitor) {
		MilestonePlanningWrapper milestonePlanning = new MilestonePlanningWrapper(taskData.getRoot());
		int trackerId = TuleapTaskId.UNKNOWN_ID;
		ArtifactReference artifact = milestone.getArtifact();
		if (artifact != null) {
			TuleapReference tracker = artifact.getTracker();
			if (tracker != null) {
				trackerId = tracker.getId();
			}
		}
		TuleapTaskId internalMilestoneId = TuleapTaskId.forArtifact(milestone.getProject().getId(),
				trackerId, milestone.getId().intValue());

		SubMilestoneWrapper subMilestoneWrapper = milestonePlanning.addSubMilestone(internalMilestoneId
				.toString());
		subMilestoneWrapper.setDisplayId(Integer.toString(milestone.getId().intValue()));
		subMilestoneWrapper.setLabel(milestone.getLabel());
		subMilestoneWrapper.setStartDate(milestone.getStartDate());
		subMilestoneWrapper.setEndDate(milestone.getEndDate());
		subMilestoneWrapper.setCapacity(milestone.getCapacity());
		subMilestoneWrapper.setStatusValue(milestone.getStatusValue());

		for (TuleapBacklogItem backlogItem : milestoneContent) {
			int projectId;

			TuleapReference project = backlogItem.getProject();
			if (project != null) {
				projectId = project.getId();
			} else {
				projectId = Integer.parseInt(taskData.getRoot().getAttribute(TuleapArtifactMapper.PROJECT_ID)
						.getValue());
			}
			BacklogItemWrapper backlogItemWrapper = milestonePlanning.addBacklogItem(TuleapTaskId
					.forArtifact(projectId, 0, backlogItem.getId().intValue()).toString());
			backlogItemWrapper.setDisplayId(Integer.toString(backlogItem.getId().intValue()));

			backlogItemWrapper.setAssignedMilestoneId(internalMilestoneId.toString());
			backlogItemWrapper.setType(backlogItem.getType());
			backlogItemWrapper.setLabel(backlogItem.getLabel());

			if (backlogItem.getStatus() != null) {
				backlogItemWrapper.setStatus(backlogItem.getStatus().toString());
			}
			if (backlogItem.getInitialEffort() != null) {
				backlogItemWrapper.setInitialEffort(backlogItem.getInitialEffort());
			}
			if (backlogItem.getParent() != null) {
				backlogItemWrapper.setParent(TuleapTaskId.forArtifact(projectId, 0,
						backlogItem.getParent().getId()).toString(), Integer.toString(backlogItem.getParent()
						.getId()));
			}
		}

	}
}
