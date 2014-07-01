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
package org.tuleap.mylyn.task.core.internal.data.converter;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownData;
import org.tuleap.mylyn.task.agile.core.data.burndown.BurndownMapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.ColumnWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.core.internal.TuleapCoreActivator;
import org.tuleap.mylyn.task.core.internal.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.core.internal.data.TuleapTaskId;
import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapComputedValue;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.core.internal.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactReference;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapBurndown;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapColumn;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.core.internal.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreKeys;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreMessages;

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
	 * Populates the burndown data in a task data from the given pojo.
	 *
	 * @param taskData
	 *            The task data to fill.
	 * @param burndown
	 *            The burndown POJO
	 * @param monitor
	 *            The progress monitor to use
	 */
	public void populateBurndown(TaskData taskData, TuleapBurndown burndown, IProgressMonitor monitor) {
		BurndownMapper mapper = new BurndownMapper(taskData);
		BurndownData data = new BurndownData(burndown.getDuration(), burndown.getCapacity(), burndown
				.getPoints());
		mapper.setBurndownData(data);
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
		boolean hasChildren = false;
		for (TuleapTracker theTracker : project.getAllTrackers()) {
			if (theTracker.getParentTracker() != null) {
				if (theTracker.getParentTracker().getId() == card.getArtifact().getTracker().getId()) {
					hasChildren = true;
				}
			}
		}
		cardWrapper.setHasChildren(hasChildren);

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
			cardWrapper.setDisplayId(TuleapCoreMessages.getString(TuleapCoreKeys.cardDisplayId, tracker
					.getLabel(), Integer.toString(artifact.getId())));
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
					// Create options, apply workflow, etc.
					field.initializeAttribute(fieldAtt);
					// TODO Find a way to put this responsibility in the Field classes
					// Set the field read-only like for tickets
					// @see AbstractTuleapField.initializeMetaData()
					if (!field.isUpdatable() && !field.isUpdatable() || field instanceof TuleapComputedValue) {
						fieldAtt.getMetaData().setReadOnly(true);
					}
					populateCardField(tracker, fieldValue, field, fieldAtt);
				} else {
					TuleapCoreActivator.log(TuleapCoreMessages.getString(
							TuleapCoreKeys.cardTrackerConfigNeedsUpdate, Integer.toString(trackerId)), true);
				}
			}
		} else {
			cardWrapper.setDisplayId(Integer.toString(artifact.getId()));
			TuleapCoreActivator.log(TuleapCoreMessages.getString(TuleapCoreKeys.cardTrackerNotAvailable,
					Integer.toString(trackerId)), true);
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
		try {
			// REQ-6749 Errors when File upload in cards.
			// File upload fields in cards need to be handled appropriately later.
			if (!(field instanceof TuleapFileUpload)) {
				field.setValue(fieldAtt, fieldValue);
			}
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			TuleapCoreActivator.log(e, true);
			TuleapCoreActivator.log(TuleapCoreMessages.getString(TuleapCoreKeys.cardTrackerConfigNeedsUpdate,
					Integer.toString(tracker.getIdentifier())), true);
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
		for (BacklogItemWrapper biWrapper : wrapper.getOrderedUnassignedBacklogItems()) {
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
		return backlogItems;
	}

	/**
	 * Indicates whether the milestone with the given ID needs to be sent to the server for update.
	 *
	 * @param taskData
	 *            The milestone's task data.
	 * @param submilestoneId
	 *            the sub-milestone identifier to extract content.
	 * @return <code>true</code> if and only if the milestone's content has been modified locally.
	 */
	public boolean mustUpdate(TaskData taskData, TuleapTaskId submilestoneId) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		SubMilestoneWrapper subMilestone = wrapper.getSubMilestone(submilestoneId.toString());
		return subMilestone == null || subMilestone.hasContentChanged();
	}

	/**
	 * Indicates whether the backlog needs to be sent to the server for update.
	 *
	 * @param taskData
	 *            The milestone's task data.
	 * @return <code>true</code> if and only if the backlog has been modified locally.
	 */
	public boolean mustUpdateBacklog(TaskData taskData) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		return wrapper.hasBacklogChanged();
	}

	/**
	 * Extracts the list of a sub-milestone content from a Milestone {@link TaskData} object.
	 *
	 * @param taskData
	 *            The milestone's task data.
	 * @param submilestoneId
	 *            the sub-milestone identifier to extract content.
	 * @return The list of sub-milestone content items to send to the server for update.
	 */
	public List<TuleapBacklogItem> extractContent(TaskData taskData, TuleapTaskId submilestoneId) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		SubMilestoneWrapper subMilestone = wrapper.getSubMilestone(submilestoneId.toString());
		for (BacklogItemWrapper biWrapper : subMilestone.getOrderedBacklogItems()) {
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
		if (wrapper.isAllowedToHaveSubmilestones()) {
			for (SubMilestoneWrapper subMilestoneWrapper : wrapper.getSubMilestones()) {
				TuleapTaskId subMilestoneTaskId = TuleapTaskId.forName(subMilestoneWrapper.getId());
				int id = subMilestoneTaskId.getArtifactId();
				int projectId = subMilestoneTaskId.getProjectId();
				TuleapReference projectRef = new TuleapReference();
				projectRef.setId(projectId);
				TuleapMilestone submilestone = new TuleapMilestone(id, projectRef);
				submilestone.setArtifact(new ArtifactReference(id, null, new TuleapReference(
						subMilestoneTaskId.getTrackerId(), null)));
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
	public List<TuleapCard> extractModifiedCards(TaskData taskData) {
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

		TuleapTracker tracker = connector.getServer(taskRepository).getTracker(trackerId);

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
			String columnId = cardWrapper.getColumnId();
			if (columnId != null) {
				card.setColumnId(Integer.valueOf(columnId));
			}
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
		// We need to add the field config also so that JSON serialization goes well
		card.addField(field);
		card.addFieldValue(field.createFieldValue(attribute, fieldId));
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
			TuleapTaskId biTaskId = extractBacklogItemTaskId(backlogItem, taskData);
			BacklogItemWrapper backlogItemWrapper = milestonePlanning.addBacklogItem(biTaskId.toString());
			populateItem(backlogItem, biTaskId, backlogItemWrapper);
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
		if (!milestonePlanning.isAllowedToHaveSubmilestones()) {
			throw new IllegalStateException(TuleapCoreMessages
					.getString(TuleapCoreKeys.notAllowedAddSubmilestones));
		}

		// #6636 - Tracker ID is needed otherwise navigation KO when disconnected
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

		if (milestonePlanning.isAllowedToHaveSubmilestones()) {
			SubMilestoneWrapper subMilestoneWrapper = milestonePlanning.addSubMilestone(internalMilestoneId
					.toString());
			subMilestoneWrapper.setDisplayId(Integer.toString(milestone.getId().intValue()));
			subMilestoneWrapper.setLabel(milestone.getLabel());
			subMilestoneWrapper.setStartDate(milestone.getStartDate());
			subMilestoneWrapper.setEndDate(milestone.getEndDate());
			subMilestoneWrapper.setCapacity(milestone.getCapacity());
			subMilestoneWrapper.setStatusValue(milestone.getStatusValue());
			if (milestone.getStatus() != null) {
				subMilestoneWrapper.setStatus(milestone.getStatus().toString());
			}
			for (TuleapBacklogItem backlogItem : milestoneContent) {
				TuleapTaskId biTaskId = extractBacklogItemTaskId(backlogItem, taskData);
				BacklogItemWrapper backlogItemWrapper = subMilestoneWrapper.addBacklogItem(biTaskId
						.toString());
				populateItem(backlogItem, biTaskId, backlogItemWrapper);
			}
		}
	}

	/**
	 * Populates the parent info in the given wrapper, from the given Backlog item.
	 *
	 * @param backlogItem
	 *            Input backlog item to copy into the wrapper
	 * @param biTaskId
	 *            Task ID of the backlog item to add in the wrapper.
	 * @param backlogItemWrapper
	 *            wrapper to populate
	 */
	private void populateItem(TuleapBacklogItem backlogItem, TuleapTaskId biTaskId,
			BacklogItemWrapper backlogItemWrapper) {
		backlogItemWrapper.setDisplayId(Integer.toString(backlogItem.getId().intValue()));
		backlogItemWrapper.setType(backlogItem.getType());
		backlogItemWrapper.setLabel(backlogItem.getLabel());
		if (backlogItem.getStatus() != null) {
			backlogItemWrapper.setStatus(backlogItem.getStatus().toString());
		}
		if (backlogItem.getInitialEffort() != null) {
			backlogItemWrapper.setInitialEffort(backlogItem.getInitialEffort());
		}
		if (backlogItem.getParent() != null) {
			// #6636 - Tracker ID is needed otherwise navigation KO when disconnected
			int parentTrackerId = TuleapTaskId.UNKNOWN_ID;
			TuleapReference parentTracker = backlogItem.getParent().getTracker();
			if (parentTracker != null) {
				parentTrackerId = parentTracker.getId();
			}
			backlogItemWrapper.setParent(TuleapTaskId.forArtifact(biTaskId.getProjectId(), parentTrackerId,
					backlogItem.getParent().getId()).toString(), Integer.toString(backlogItem.getParent()
							.getId()));
		}
	}

	/**
	 * Extract the backlog item's task ID from the given item and task data.
	 *
	 * @param backlogItem
	 *            , which should have a project reference and a tracker reference
	 * @param taskData
	 *            TaskData of the milestone containing the backlog item, its project will be used if the given
	 *            BI has no project reference.
	 * @return The Task ID for the given backlog item.
	 */
	private TuleapTaskId extractBacklogItemTaskId(TuleapBacklogItem backlogItem, TaskData taskData) {
		int projectId;
		TuleapReference project = backlogItem.getProject();
		if (project != null) {
			projectId = project.getId();
		} else {
			projectId = Integer.parseInt(taskData.getRoot().getAttribute(TuleapArtifactMapper.PROJECT_ID)
					.getValue());
		}
		ArtifactReference artifactRef = backlogItem.getArtifact();
		// #6636 - Tracker ID is needed otherwise navigation KO when disconnected
		int biTrackerId = TuleapTaskId.UNKNOWN_ID;
		if (artifactRef != null && artifactRef.getTracker() != null) {
			biTrackerId = artifactRef.getTracker().getId();
		}
		return TuleapTaskId.forArtifact(projectId, biTrackerId, backlogItem.getId().intValue());
	}
}
