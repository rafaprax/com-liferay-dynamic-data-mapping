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

import java.util.HashMap;
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
	immediate = true, property = {"ddm.form.analytics.metric=timeSpentPerField"}
)
public class DDMFormAnalyticsTimeSpentPerField implements DDMFormAnalytics {

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
			(event.getEvent().equals(Event.FIELD_BLUR) ||
			event.getEvent().equals(Event.FIELD_FOCUS)));

		stream = stream.sorted();

		Map<Long, Map<String, List<DDMFormAnalyticsEventEntry>>> map =
			stream.collect(
				Collectors.groupingBy(
					DDMFormAnalyticsEventEntry::getUserId,
					Collectors.groupingBy(
						DDMFormAnalyticsEventEntry::getFieldName)));

		Set<Map.Entry<Long, Map<String, List<DDMFormAnalyticsEventEntry>>>>
			entrySet = map.entrySet();

		Stream<Map.Entry<Long, Map<String, List<DDMFormAnalyticsEventEntry>>>>
			entrySetStream = entrySet.stream();

		List<Map<String, Integer>> timeSpentPerField = entrySetStream.map(
			this::map
		).collect(
			Collectors.toList()
		);

		Stream<Map<String, Integer>> timeSpentPerFieldStream =
			timeSpentPerField.stream();

		Map<Object, Double> timeSpentPerFieldMap =
			timeSpentPerFieldStream.flatMap(
				mapper -> mapper.entrySet().stream()
			).collect(
				Collectors.groupingBy(
					Map.Entry::getKey,
					Collectors.averagingInt(Map.Entry::getValue))
			);

		for (Map.Entry<Object, Double> entry :
				timeSpentPerFieldMap.entrySet()) {

			jsonObject.put(
				entry.getKey().toString(), entry.getValue().intValue());
		}

		return jsonObject;
	}

	protected Map<String, Integer> map(
		Map.Entry<Long, Map<String, List<DDMFormAnalyticsEventEntry>>> entry) {

		Map<String, Integer> fieldDurationMap = new HashMap<>();

		Map<String, List<DDMFormAnalyticsEventEntry>> value = entry.getValue();

		for (Map.Entry<String, List<DDMFormAnalyticsEventEntry>> valueEntry :
				value.entrySet()) {

			List<DDMFormAnalyticsEventEntry> events = valueEntry.getValue();

			int total = 0;

			for (int i = 0; i < events.size(); i++) {
				int focusIndex = i;
				int blurIndex = i + 1 < events.size() ? i + 1 : i;

				if (blurIndex != focusIndex) {
					DDMFormAnalyticsEventEntry focusEvent = events.get(
						focusIndex);
					DDMFormAnalyticsEventEntry blurEvent = events.get(
						blurIndex);

					Duration duration = Duration.between(
						focusEvent.getTimestamp(), blurEvent.getTimestamp());

					total += duration.getSeconds();

					i++;
				}
			}

			fieldDurationMap.put(valueEntry.getKey(), total);
		}

		return fieldDurationMap;
	}

	@Reference
	private JSONFactory _jsonFactory;

}