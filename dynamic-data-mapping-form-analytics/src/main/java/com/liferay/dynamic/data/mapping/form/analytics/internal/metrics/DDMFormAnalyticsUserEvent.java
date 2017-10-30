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

import com.liferay.portal.kernel.util.HashUtil;

import java.util.Objects;

/**
 * @author Leonardo Barros
 */
public class DDMFormAnalyticsUserEvent {

	public DDMFormAnalyticsUserEvent(long userId, Event event) {
		_userId = userId;
		_event = event;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMFormAnalyticsUserEvent)) {
			return false;
		}

		DDMFormAnalyticsUserEvent userEvent = (DDMFormAnalyticsUserEvent)obj;

		if (Objects.equals(_userId, userEvent._userId) &&
			Objects.equals(_event, userEvent._event)) {

			return true;
		}

		return false;
	}

	public Event getEvent() {
		return _event;
	}

	public long getUserId() {
		return _userId;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _userId);

		return HashUtil.hash(hash, _event);
	}

	private final Event _event;
	private final long _userId;

}