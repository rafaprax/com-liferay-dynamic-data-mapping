/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.form.analytics.internal.metrics;

import com.liferay.portal.kernel.json.JSONObject;

import java.time.LocalDateTime;

/**
 * @author Leonardo Barros
 */
public class DDMFormAnalyticsEventEntry
	implements Comparable<DDMFormAnalyticsEventEntry> {

	public DDMFormAnalyticsEventEntry(
		long userId, JSONObject attributes, Event event,
		LocalDateTime timestamp) {

		_userId = userId;
		_attributes = attributes;
		_event = event;
		_timestamp = timestamp;
	}

	@Override
	public int compareTo(DDMFormAnalyticsEventEntry other) {
		return getTimestamp().compareTo(other.getTimestamp());
	}

	public JSONObject getAttributes() {
		return _attributes;
	}

	public Event getEvent() {
		return _event;
	}

	public String getFieldName() {
		return _attributes.getString("fieldName");
	}

	public long getFormId() {
		return _attributes.getLong("formId");
	}

	public int getPage() {
		return _attributes.getInt("page");
	}

	public LocalDateTime getTimestamp() {
		return _timestamp;
	}

	public long getUserId() {
		return _userId;
	}

	private final JSONObject _attributes;
	private final Event _event;
	private final LocalDateTime _timestamp;
	private final long _userId;

}