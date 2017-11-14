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
public class DDMFormAnalyticsPageViewEvent {

	public DDMFormAnalyticsPageViewEvent(long userId, int page) {
		_userId = userId;
		_page = page;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMFormAnalyticsPageViewEvent)) {
			return false;
		}

		DDMFormAnalyticsPageViewEvent pageViewEvent =
			(DDMFormAnalyticsPageViewEvent)obj;

		if (Objects.equals(_userId, pageViewEvent._userId) &&
			Objects.equals(_page, pageViewEvent._page)) {

			return true;
		}

		return false;
	}

	public int getPage() {
		return _page;
	}

	public long getUserId() {
		return _userId;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _userId);

		return HashUtil.hash(hash, _page);
	}

	private final int _page;
	private final long _userId;

}