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

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String metric = ParamUtil.getString(request, "metric");
		long formId = ParamUtil.getLong(request, "formId");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			EVENT event = EVENT.valueOf(ParamUtil.getString(request, "event"));

			long userId = ParamUtil.getLong(request, "userId");

			JSONObject attributes = _jsonFactory.createJSONObject(
				ParamUtil.getString(request, "attributes"));

			_ddmFormAnalyticsEvent.add(
				new DDMFormAnalyticsEvent(
					event, LocalDateTime.now(), attributes, userId));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Reference
	private JSONFactory _jsonFactory;

	private static final long serialVersionUID = 1L;

	private static List<DDMFormAnalyticsEvent> _ddmFormAnalyticsEvent = new ArrayList<>();

	private enum EVENT {
		FIELD_BLUR,
		FIELD_FOCUS,
		FIELD_VALUE_CHANGE,
		FORM_VIEW,
		FORM_SUBMIT
	};

	protected class DDMFormAnalyticsEvent implements Comparable<DDMFormAnalyticsEvent>{
		private EVENT event;
		private long userId;
		private LocalDateTime timestamp;
		private JSONObject attributes;
		
		public DDMFormAnalyticsEvent(
			EVENT event, LocalDateTime timestamp, JSONObject attributes, long userId) {
			this.event = event;
			this.timestamp = timestamp;
			this.userId = userId;
			this.attributes = attributes;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public EVENT getEvent() {
			return event;
		}
		public void setEvent(EVENT event) {
			this.event = event;
		}
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		public JSONObject getAttributes() {
			return attributes;
		}
		public void setAttributes(JSONObject attributes) {
			this.attributes = attributes;
		}

		@Override
		public int compareTo(DDMFormAnalyticsEvent other) {
	
			return this.getTimestamp().compareTo(other.getTimestamp());
		}
	}

}