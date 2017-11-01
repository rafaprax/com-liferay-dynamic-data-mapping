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
	immediate = true, property = {"ddm.form.analytics.metric=timeSpentPerPage"}
)
public class DDMFormAnalyticsTimeSpentPerPage implements DDMFormAnalytics {

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
			(event.getEvent().equals(Event.FORM_PAGE_SHOW) ||
			event.getEvent().equals(Event.FORM_PAGE_HIDE) ||
			event.getEvent().equals(Event.FORM_SUBMIT)));

		stream = stream.sorted();

		Map<Long, Map<Integer, List<DDMFormAnalyticsEventEntry>>> map =
			stream.collect(
				Collectors.groupingBy(
					DDMFormAnalyticsEventEntry::getUserId,
					Collectors.groupingBy(
						DDMFormAnalyticsEventEntry::getPage)));

		Set<Map.Entry<Long, Map<Integer, List<DDMFormAnalyticsEventEntry>>>>
			entrySet = map.entrySet();

		Stream<Map.Entry<Long, Map<Integer, List<DDMFormAnalyticsEventEntry>>>>
			entrySetStream = entrySet.stream();

		List<Map<Integer, Integer>> timeSpentPerPage = entrySetStream.map(
			this::map
		).collect(
			Collectors.toList()
		);

		Stream<Map<Integer, Integer>> timeSpentPerPageEntrySetStream =
			timeSpentPerPage.stream();

		Map<Object, Double> timeSpentPerPageMap =
			timeSpentPerPageEntrySetStream.flatMap(
				mapper -> mapper.entrySet().stream()
			).collect(
				Collectors.groupingBy(
					Map.Entry::getKey,
					Collectors.averagingInt(Map.Entry::getValue))
			);

		for (Map.Entry<Object, Double> entry : timeSpentPerPageMap.entrySet()) {
			jsonObject.put(
				entry.getKey().toString(), entry.getValue().intValue());
		}

		return jsonObject;
	}

	protected Map<Integer, Integer> map(
		Map.Entry<Long, Map<Integer, List<DDMFormAnalyticsEventEntry>>> entry) {

		Map<Integer, Integer> pageDurationMap = new HashMap<>();

		Map<Integer, List<DDMFormAnalyticsEventEntry>> value = entry.getValue();

		for (Map.Entry<Integer, List<DDMFormAnalyticsEventEntry>> valueEntry :
				value.entrySet()) {

			List<DDMFormAnalyticsEventEntry> events = valueEntry.getValue();

			int total = 0;

			for (int i = 0; i < events.size(); i++) {
				int showIndex = i;
				int hideIndex = i + 1 < events.size() ? i + 1 : i;

				if (hideIndex != showIndex) {
					DDMFormAnalyticsEventEntry showEvent = events.get(
						showIndex);
					DDMFormAnalyticsEventEntry hideEvent = events.get(
						hideIndex);

					Duration duration = Duration.between(
						showEvent.getTimestamp(), hideEvent.getTimestamp());

					total += duration.getSeconds();

					if (hideEvent.getEvent().equals(Event.FORM_SUBMIT)) {
						break;
					}

					i++;
				}
			}

			pageDurationMap.put(valueEntry.getKey(), total);
		}

		return pageDurationMap;
	}

	@Reference
	private JSONFactory _jsonFactory;

}