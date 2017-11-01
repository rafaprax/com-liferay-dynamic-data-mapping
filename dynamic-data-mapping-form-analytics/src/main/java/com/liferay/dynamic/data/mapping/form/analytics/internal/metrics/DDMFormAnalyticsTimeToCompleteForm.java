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

import com.liferay.dynamic.data.mapping.form.analytics.DDMFormAnalytics;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;

import java.time.Duration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {"ddm.form.analytics.metric=timeToCompleteForm"}
)
public class DDMFormAnalyticsTimeToCompleteForm implements DDMFormAnalytics {

	@Override
	public JSONObject getData(
			List<DDMFormAnalyticsEventEntry> eventEntries,
			HttpServletRequest request)
		throws Exception {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		Stream<DDMFormAnalyticsEventEntry> stream = eventEntries.stream();

		long formId = ParamUtil.getLong(request, "formId");

		stream = stream.filter(
			event -> event.getFormId() == formId &&
			(event.getEvent().equals(Event.FORM_VIEW) ||
			event.getEvent().equals(Event.FORM_SUBMIT)));

		stream = stream.sorted();

		Map<Long, List<DDMFormAnalyticsEventEntry>> map = stream.collect(
			Collectors.groupingBy(DDMFormAnalyticsEventEntry::getUserId));

		Set<Map.Entry<Long, List<DDMFormAnalyticsEventEntry>>> entrySet =
			map.entrySet();

		Stream<Map.Entry<Long, List<DDMFormAnalyticsEventEntry>>>
			entrySetStream = entrySet.stream();

		Double timeToCompleteForm = entrySetStream.map(
			this::map
		).filter(
			value -> value.longValue() != Long.MAX_VALUE
		).collect(
			Collectors.averagingLong(Long::longValue)
		);

		jsonObject.put("averageTime", (Long)timeToCompleteForm.longValue());

		return jsonObject;
	}

	protected Long map(
		Map.Entry<Long, List<DDMFormAnalyticsEventEntry>> entry) {

		DDMFormAnalyticsEventEntry showEvent = null;
		DDMFormAnalyticsEventEntry submitEvent = null;

		for (DDMFormAnalyticsEventEntry event : entry.getValue()) {
			if ((showEvent == null) &&
				event.getEvent().equals(Event.FORM_VIEW)) {

				showEvent = event;
			}
			else if (event.getEvent().equals(Event.FORM_SUBMIT)) {
				submitEvent = event;
			}
		}

		if ((showEvent != null) && (submitEvent != null)) {
			Duration duration = Duration.between(
				showEvent.getTimestamp(), submitEvent.getTimestamp());

			return duration.getSeconds();
		}

		return Long.MAX_VALUE;
	}

	@Reference
	private JSONFactory _jsonFactory;

}