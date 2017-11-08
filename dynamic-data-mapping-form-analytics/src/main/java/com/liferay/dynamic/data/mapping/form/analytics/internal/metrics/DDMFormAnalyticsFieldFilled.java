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
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true, property = {"ddm.form.analytics.metric=fieldFilled"}
)
public class DDMFormAnalyticsFieldFilled implements DDMFormAnalytics {

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
			(event.getEvent().equals(Event.FIELD_EMPTY) ||
			event.getEvent().equals(Event.FIELD_LOADED) ||
			event.getEvent().equals(Event.FIELD_STARTED_FILLING) ||
			event.getEvent().equals(Event.FORM_SUBMIT)));

		stream = stream.sorted();

		Map<Long, List<DDMFormAnalyticsEventEntry>> map = stream.collect(
			Collectors.groupingBy(DDMFormAnalyticsEventEntry::getUserId));

		Set<Entry<Long, List<DDMFormAnalyticsEventEntry>>> entrySet =
			map.entrySet();

		Stream<Entry<Long, List<DDMFormAnalyticsEventEntry>>> entrySetStream =
			entrySet.stream();

		Map<String, List<EventSummary>> fieldEventSummaryMap =
			entrySetStream.map(
				this::map
			).flatMap(
				Collection::stream
			).collect(
				Collectors.groupingBy(EventSummary::getFieldName)
			);

		List<EventSummary> submittedForms = fieldEventSummaryMap.getOrDefault(
			_FORM_SUBMITTED, Collections.emptyList());

		long submittedFormsCount = submittedForms.size();

		JSONObject fieldsJSONObject = _jsonFactory.createJSONObject();

		for (Map.Entry<String, List<EventSummary>>
				entry :fieldEventSummaryMap.entrySet()) {

			if (Objects.equals(entry.getKey(), _FORM_SUBMITTED)) {
				continue;
			}

			Stream<EventSummary> eventSummaryStream = entry.getValue().stream();

			Optional<EventSummary> reduce = eventSummaryStream.reduce(
				(e1, e2) -> {
					e2._empty = e1._empty + e2._empty;
					e2._filled = e1._filled + e2._filled;

					return e2;
				});

			reduce.ifPresent(
				eventSummary -> {
					JSONObject fieldJSONObject =
						_jsonFactory.createJSONObject();

					double filledRate =
						eventSummary._filled / submittedFormsCount * 100;

					fieldJSONObject.put("empty", (long)eventSummary._empty);
					fieldJSONObject.put("emptyRate", 100 - filledRate);
					fieldJSONObject.put("filled", (long)eventSummary._filled);
					fieldJSONObject.put("filledRate", filledRate);

					fieldsJSONObject.put(entry.getKey(), fieldJSONObject);
				});
		}

		jsonObject.put("fields", fieldsJSONObject);
		jsonObject.put("totalSubmissions", (long)submittedFormsCount);

		return jsonObject;
	}

	protected EventSummary createEventSummary(
		Entry<String, List<DDMFormAnalyticsEventEntry>> fieldEntry) {

		List<DDMFormAnalyticsEventEntry> events = fieldEntry.getValue();

		Stream<DDMFormAnalyticsEventEntry> eventsStream = events.stream();

		Event[] eventsArray =
			{Event.FIELD_EMPTY, Event.FIELD_STARTED_FILLING, Event.FORM_SUBMIT};

		Map<Event, List<DDMFormAnalyticsEventEntry>> eventsMap =
			eventsStream.filter(
				event -> {
					return ArrayUtil.contains(eventsArray, event.getEvent());
				}
			).collect(
				Collectors.groupingBy(DDMFormAnalyticsEventEntry::getEvent)
			);

		int formSubmitCount = eventsMap.getOrDefault(
			Event.FORM_SUBMIT, Collections.emptyList()
		).size();

		if (formSubmitCount > 0) {
			return new EventSummary(fieldEntry.getKey(), 0, 0);
		}

		long emptyCount = eventsMap.getOrDefault(
			Event.FIELD_EMPTY, Collections.emptyList()
		).size();

		long startedFillingCount = eventsMap.getOrDefault(
			Event.FIELD_STARTED_FILLING, Collections.emptyList()
		).size();

		long empty = 0;
		long filled = 0;

		if (startedFillingCount > emptyCount) {
			filled = 1;
		}
		else {
			empty = 1;
		}

		return new EventSummary(fieldEntry.getKey(), empty, filled);
	}

	protected List<EventSummary> map(
		Entry<Long, List<DDMFormAnalyticsEventEntry>> entry) {

		List<DDMFormAnalyticsEventEntry> value = entry.getValue();

		boolean submitted = value.stream().anyMatch(
			e -> e.getEvent().equals(Event.FORM_SUBMIT));

		if (!submitted) {
			return Collections.emptyList();
		}

		Stream<DDMFormAnalyticsEventEntry> valueStream = value.stream();

		Map<String, List<DDMFormAnalyticsEventEntry>> map = valueStream.collect(
			Collectors.groupingBy(
				eventEntry -> {
					if (Objects.equals(
							eventEntry.getEvent(), Event.FORM_SUBMIT)) {

						return _FORM_SUBMITTED;
					}

					return eventEntry.getFieldName();
				}));

		Set<Entry<String, List<DDMFormAnalyticsEventEntry>>> entrySet =
			map.entrySet();

		Stream<Entry<String, List<DDMFormAnalyticsEventEntry>>> entrySetStream =
			entrySet.stream();

		Stream<EventSummary> eventSummaryStream = entrySetStream.map(
			fieldEntry -> createEventSummary(fieldEntry));

		return eventSummaryStream.collect(Collectors.toList());
	}

	private static final String _FORM_SUBMITTED = "FORM_SUBMITTED";

	@Reference
	private JSONFactory _jsonFactory;

	private class EventSummary {

		public EventSummary(String fieldName, long empty, long filled) {
			_fieldName = fieldName;
			_empty = empty;
			_filled = filled;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private long _empty;
		private final String _fieldName;
		private long _filled;

	}

}