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

package com.liferay.dynamic.data.mapping.form.analytics.internal.servlet;

import com.liferay.dynamic.data.mapping.form.analytics.DDMFormAnalytics;
import com.liferay.dynamic.data.mapping.form.analytics.DDMFormAnalyticsTracker;
import com.liferay.dynamic.data.mapping.form.analytics.internal.metrics.DDMFormAnalyticsEventEntry;
import com.liferay.dynamic.data.mapping.form.analytics.internal.metrics.Event;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.io.IOException;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/dynamic-data-mapping-form-analytics-event",
		"osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.analytics.internal.servlet.DDMFormAnalyticsServlet",
		"osgi.http.whiteboard.servlet.pattern=/dynamic-data-mapping-form-analytics-event/*"
	},
	service = Servlet.class
)
public class DDMFormAnalyticsServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		try {
			DDMFormAnalytics ddmFormAnalytics =
				_ddmFormAnalyticsTracker.getMetric(
					ParamUtil.getString(request, "metric"));

			if (ddmFormAnalytics == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);

				return;
			}

			JSONObject jsonObject = ddmFormAnalytics.getData(
				_ddmFormAnalyticsEventEntries, request);

			ServletResponseUtil.write(response, jsonObject.toJSONString());
		}
		catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}
	}

	@Override
	protected void doPost(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		try {
			Event event = Event.valueOf(ParamUtil.getString(request, "event"));

			long userId = ParamUtil.getLong(request, "userId");

			JSONObject attributes = _jsonFactory.createJSONObject(
				ParamUtil.getString(request, "attributes"));

			_ddmFormAnalyticsEventEntries.add(
				new DDMFormAnalyticsEventEntry(
					userId, attributes, event, LocalDateTime.now()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final List<DDMFormAnalyticsEventEntry>
		_ddmFormAnalyticsEventEntries = new ArrayList<>();

	@Reference
	private DDMFormAnalyticsTracker _ddmFormAnalyticsTracker;

	@Reference
	private JSONFactory _jsonFactory;

}