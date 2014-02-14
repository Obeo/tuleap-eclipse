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
package org.tuleap.mylyn.task.internal.core.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapDetailedElement;

import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.HTML_URL;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.LAST_MODIFIED_DATE;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_BY;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_ON;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object, for objects that have a tracker
 * ID (top-plannings, for instance, have no tracker ID).
 * 
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractDetailedElementDeserializer<T extends AbstractTuleapDetailedElement> extends AbstractProjectElementDeserializer<T> {

	/**
	 * Date format ISO-8601 with milliseconds when using a timezone with a sign and 4 digits separated by a
	 * colon.
	 */
	private static final String DATE_FORMAT_MILLIS_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; //$NON-NLS-1$

	/**
	 * Date format ISO-8601 with milliseconds when using the default timezone.
	 */
	private static final String DATE_FORMAT_MILLIS_WITH_DEFAULT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //$NON-NLS-1$

	/**
	 * Date format ISO-8601 when using a timezone with a sign and 4 digits separated by a colon.
	 */
	private static final String DATE_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZ"; //$NON-NLS-1$

	/**
	 * Date format ISO-8601 when using the default timezone.
	 */
	private static final String DATE_FORMAT_WITH_DEFAULT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public T deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		T pojo = super.deserialize(rootJsonElement, type, jsonDeserializationContext);

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		JsonElement element = jsonObject.get(HTML_URL);
		if (element != null && !element.isJsonNull()) {
			String htmlUrl = element.getAsString();
			pojo.setHtmlUrl(htmlUrl);
		}
		element = jsonObject.get(SUBMITTED_ON);
		if (element != null && !element.isJsonNull()) {
			String submittedOn = jsonObject.get(SUBMITTED_ON).getAsString();
			try {
				pojo.setSubmittedOn(parseISO8601Date(submittedOn));
			} catch (ParseException e) {
				TuleapCoreActivator.log(e, false);
			}
		}
		element = jsonObject.get(SUBMITTED_BY);
		if (element != null && !element.isJsonNull()) {
			int submittedBy = jsonObject.get(SUBMITTED_BY).getAsInt();
			pojo.setSubmittedBy(submittedBy);
		}
		element = jsonObject.get(LAST_MODIFIED_DATE);
		if (element != null && !element.isJsonNull()) {
			String lastUpdatedOn = jsonObject.get(LAST_MODIFIED_DATE).getAsString();
			try {
				pojo.setLastModifiedDate(parseISO8601Date(lastUpdatedOn));
			} catch (ParseException e) {
				TuleapCoreActivator.log(e, false);
			}
		}
		return pojo;
	}

	/**
	 * Parses a date in ISO 8601 format, without Joda time...
	 * 
	 * @param dateIso8601
	 *            Date to parse.
	 * @throws ParseException
	 *             if the given date is not in the right format.
	 * @return The parsed date.
	 */
	public Date parseISO8601Date(String dateIso8601) throws ParseException {
		SimpleDateFormat format;
		if (dateIso8601.endsWith("Z")) { //$NON-NLS-1$
			if (dateIso8601.indexOf('.') > 0) {
				format = new SimpleDateFormat(DATE_FORMAT_MILLIS_WITH_DEFAULT_TIMEZONE);
			} else {
				format = new SimpleDateFormat(DATE_FORMAT_WITH_DEFAULT_TIMEZONE);
			}
			format.setTimeZone(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
			return format.parse(dateIso8601);
		}
		if (dateIso8601.indexOf('.') > 0) {
			format = new SimpleDateFormat(DATE_FORMAT_MILLIS_WITH_TIMEZONE);
		} else {
			format = new SimpleDateFormat(DATE_FORMAT_WITH_TIMEZONE);
		}
		return format.parse(dateIso8601.replaceFirst(":(\\d\\d)$", "$1")); //$NON-NLS-1$//$NON-NLS-2$
	}

}
