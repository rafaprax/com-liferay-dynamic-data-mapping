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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
	property = {"ddm.form.analytics.metric=successfulSubmission"}
)
public class DDMFormAnalyticsSuccessfulSubmission implements DDMFormAnalytics {

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
			event.getEvent().equals(Event.FIELD_STARTED_FILLING) ||
			event.getEvent().equals(Event.FORM_SUBMIT)));

		Map<Long, Map<Event, List<DDMFormAnalyticsEventEntry>>> map =
			stream.collect(
				Collectors.groupingBy(
					DDMFormAnalyticsEventEntry::getUserId,
					Collectors.groupingBy(
						DDMFormAnalyticsEventEntry::getEvent)));

		Set<Map.Entry<Long, Map<Event, List<DDMFormAnalyticsEventEntry>>>>
			entrySet = map.entrySet();

		Stream<Map.Entry<Long, Map<Event, List<DDMFormAnalyticsEventEntry>>>>
			entrySetStream = entrySet.stream();

		Map<Event, Long> eventsMap = entrySetStream.flatMap(
			this::flatMap
		).collect(
			Collectors.groupingBy(Function.identity(), Collectors.counting())
		);

		Long totalFormView = eventsMap.get(Event.FORM_VIEW);
		Long totalStarted = eventsMap.get(Event.FIELD_STARTED_FILLING);
		Long totalCompleted = eventsMap.get(Event.FORM_SUBMIT);

		double successRate = (double)totalCompleted / totalFormView * 100;

		jsonObject.put("abandonment", 100 - successRate);

		jsonObject.put("completed", totalCompleted);
		jsonObject.put("started", totalStarted);
		jsonObject.put("success", successRate);
		jsonObject.put("visitors", totalFormView);

		return jsonObject;
	}

	protected Stream<Event>
		flatMap(Map.Entry<Long, Map<Event, List<DDMFormAnalyticsEventEntry>>>
			entry) {

		Map<Event, List<DDMFormAnalyticsEventEntry>> value = entry.getValue();

		Set<Event> events = value.keySet();

		return events.stream();
	}

	@Reference
	private JSONFactory _jsonFactory;

}