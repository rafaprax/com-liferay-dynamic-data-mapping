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

package com.liferay.dynamic.data.mapping.form.builder.context;

import com.liferay.dynamic.data.mapping.model.DDMForm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafael Praxedes
 */
public class DDMFormContextDeserializerRequest {

	public static DDMFormContextDeserializerRequest with(
		DDMForm ddmForm, String serializedFormContext) {

		DDMFormContextDeserializerRequest ddmFormContextDeserializerRequest =
			with(serializedFormContext);

		ddmFormContextDeserializerRequest.addProperty("ddmForm", ddmForm);

		return ddmFormContextDeserializerRequest;
	}

	public static DDMFormContextDeserializerRequest with(
		String serializedFormContext) {

		DDMFormContextDeserializerRequest ddmFormContextDeserializerRequest =
			new DDMFormContextDeserializerRequest();

		ddmFormContextDeserializerRequest.addProperty(
			"serializedFormContext", serializedFormContext);

		return ddmFormContextDeserializerRequest;
	}

	public void addProperty(String key, Object value) {
		_properties.put(key, value);
	}

	public <T> T getProperty(String name) {
		return (T)_properties.get(name);
	}

	private DDMFormContextDeserializerRequest() {
	}

	private final Map<String, Object> _properties = new HashMap<>();

}