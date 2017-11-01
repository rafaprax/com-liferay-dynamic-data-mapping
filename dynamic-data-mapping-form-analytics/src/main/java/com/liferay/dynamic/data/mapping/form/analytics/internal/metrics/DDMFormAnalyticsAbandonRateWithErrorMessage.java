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
import java.util.Map.Entry;
import java.util.Optional;
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
	property = {"ddm.form.analytics.metric=abandonRateWithErrorMessage"}
)
public class DDMFormAnalyticsAbandonRateWithErrorMessage
	implements DDMFormAnalytics {

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
			event.getEvent().equals(Event.FIELD_VALIDATION_ERROR) ||
			event.getEvent().equals(Event.FORM_SUBMIT) ||
			event.getEvent().equals(Event.FORM_VALIDATION_ERROR)));

		Stream<DDMFormAnalyticsUserEvent> userEventstream = stream.map(
			e -> new DDMFormAnalyticsUserEvent(e.getUserId(), e.getEvent()));

		userEventstream = userEventstream.distinct();

		Map<Long, List<DDMFormAnalyticsUserEvent>> map =
			userEventstream.collect(
				Collectors.groupingBy(DDMFormAnalyticsUserEvent::getUserId));

		Set<Entry<Long, List<DDMFormAnalyticsUserEvent>>> entrySet =
			map.entrySet();

		Stream<Entry<Long, List<DDMFormAnalyticsUserEvent>>> entrySetStream =
			entrySet.stream();

		List<EventSummary> eventSummaries = entrySetStream.map(
			this::map
		).collect(
			Collectors.toList()
		);

		Stream<EventSummary> eventSummaryStream = eventSummaries.stream();

		Optional<EventSummary> reduce = eventSummaryStream.reduce(
			(e1, e2) -> new EventSummary(
				e1._abandons + e2._abandons, e1._errors + e2._errors,
				e1._views + e2._views));

		EventSummary eventSummary = reduce.get();

		jsonObject.put(
			"abandonRateWithError",
			(double)eventSummary._errors / eventSummary._views * 100);
		jsonObject.put(
			"abandonRateWithoutError",
			(double)(eventSummary._abandons - eventSummary._errors) /
				eventSummary._views * 100);
		jsonObject.put("abandons", (Long)eventSummary._abandons);
		jsonObject.put("visits", (Long)eventSummary._views);

		return jsonObject;
	}

	protected EventSummary map(
		Map.Entry<Long, List<DDMFormAnalyticsUserEvent>> entry) {

		List<DDMFormAnalyticsUserEvent> value = entry.getValue();

		boolean submitted = value.stream(
		).anyMatch(
			e -> e.getEvent().equals(Event.FORM_SUBMIT)
		);

		boolean hasErrors = value.stream(
		).anyMatch(
			e -> e.getEvent().equals(Event.FIELD_VALIDATION_ERROR) ||
			e.getEvent().equals(Event.FORM_VALIDATION_ERROR)
		);

		long abandon = 1;

		if (submitted) {
			abandon = 0;
		}

		long errors = 0;

		if (hasErrors && !submitted) {
			errors = 1;
		}

		return new EventSummary(abandon, errors, 1);
	}

	@Reference
	private JSONFactory _jsonFactory;

	private class EventSummary {

		public EventSummary(long abandons, long errors, long views) {
			_abandons = abandons;
			_errors = errors;
			_views = views;
		}

		private final long _abandons;
		private final long _errors;
		private final long _views;

	}

}