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
import com.liferay.dynamic.data.mapping.form.analytics.DDMFormAnalyticsTracker;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true)
public class DDMFormAnalyticsTrackerImpl implements DDMFormAnalyticsTracker {

	@Override
	public DDMFormAnalytics getMetric(String metric) {
		return _ddmFormAnalyticsTrackerMap.getService(metric);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_ddmFormAnalyticsTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DDMFormAnalytics.class,
				"ddm.form.analytics.metric");
	}

	@Deactivate
	protected void deactivate() {
		_ddmFormAnalyticsTrackerMap.close();
	}

	private ServiceTrackerMap<String, DDMFormAnalytics>
		_ddmFormAnalyticsTrackerMap;

}