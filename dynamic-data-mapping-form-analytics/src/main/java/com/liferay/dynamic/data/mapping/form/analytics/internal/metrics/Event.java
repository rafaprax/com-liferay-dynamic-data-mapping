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

/**
 * @author Leonardo Barros
 */
public enum Event {

	FIELD_BLUR, FIELD_EMPTY, FIELD_FOCUS, FIELD_LOADED, FIELD_STARTED_FILLING,
	FIELD_VALIDATION_ERROR, FORM_PAGE_SHOW, FORM_PAGE_HIDE,
	FORM_VALIDATION_ERROR, FORM_VIEW, FORM_SUBMIT
}