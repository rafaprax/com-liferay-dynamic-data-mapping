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
	property = {"ddm.form.analytics.metric=funnelStatsForMultiPageForm"}
)
public class DDMFormAnalyticsFunnelStatsForMultiPageForm
	implements DDMFormAnalytics {

	@Override
	public JSONObject getData(
			List<DDMFormAnalyticsEventEntry> eventEntries,
			HttpServletRequest request)
		throws Exception {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		Stream<DDMFormAnalyticsEventEntry> visitStream = eventEntries.stream();

		long formId = ParamUtil.getLong(request, "formId");

		visitStream = visitStream.filter(
			event -> event.getFormId() == formId &&
			(event.getEvent().equals(Event.FORM_VIEW) ||
			event.getEvent().equals(Event.FORM_SUBMIT)));

		Map<Event, Long> visitMap = visitStream.map(
			event -> new DDMFormAnalyticsUserEvent(
				event.getUserId(), event.getEvent())
		).distinct(
		).collect(
			Collectors.groupingBy(
				DDMFormAnalyticsUserEvent::getEvent, Collectors.counting())
		);

		Stream<DDMFormAnalyticsEventEntry> pageStream = eventEntries.stream();

		pageStream = pageStream.filter(
			event -> event.getFormId() == formId &&
			(event.getEvent().equals(Event.FORM_PAGE_SHOW)));

		Map<Integer, Long> pageMap = pageStream.map(
			event -> new DDMFormAnalyticsPageViewEvent(
				event.getUserId(), event.getPage())
		).distinct(
		).collect(
			Collectors.groupingBy(
				DDMFormAnalyticsPageViewEvent::getPage, Collectors.counting())
		);

		Long totalVisits = visitMap.get(Event.FORM_VIEW);
		Long totalSubmissions = visitMap.get(Event.FORM_SUBMIT);

		jsonObject.put("submissions", totalSubmissions);
		jsonObject.put(
			"successfulSubmissions",
			(double)totalSubmissions / totalVisits * 100);

		jsonObject.put("visits", totalVisits);

		for (int i = 0; i < pageMap.size(); i++) {
			int page = i + 1;

			jsonObject.put(
				String.format("visitsPage_%d", page), pageMap.get(page));

			if (page > 1) {
				jsonObject.put(
					String.format(
						"conversionFromPage_%d_to_%d", page - 1, page),
					(double)pageMap.get(page) / pageMap.get(page - 1) * 100);
			}

			if (page == pageMap.size()) {
				jsonObject.put(
					String.format("conversionFromPage_%d_to_Submission", page),
					(double)totalSubmissions / pageMap.get(page) * 100);
			}
		}

		return jsonObject;
	}

	@Reference
	private JSONFactory _jsonFactory;

}