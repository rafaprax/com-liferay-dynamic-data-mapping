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

package com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.util;

import com.liferay.dynamic.data.mapping.form.builder.DDMFormBuilderSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(immediate = true)
public class FormTaglibContextUtil {

	public static String getDDMDataProviderInstanceParameterSettingsURL() {
		DDMFormBuilderSettings ddmFormBuilderContext =
			getDDMFormBuilderContext();
		
		return ddmFormBuilderContext.
			getDDMDataProviderInstanceParameterSettingsURL();
	}

	public static String getDDMDataProviderInstancesURL() {
		DDMFormBuilderSettings ddmFormBuilderContext =
			getDDMFormBuilderContext();
		
		return ddmFormBuilderContext.getDDMDataProviderInstancesURL();
	}

	public static String getDDMFieldSettingsDDMFormContextURL() {
		DDMFormBuilderSettings ddmFormBuilderContext =
			getDDMFormBuilderContext();
		
		return ddmFormBuilderContext.getDDMFieldSettingsDDMFormContextURL();
	}

	public static String getDDMFunctionsURL() {
		DDMFormBuilderSettings ddmFormBuilderContext =
			getDDMFormBuilderContext();
		
		return ddmFormBuilderContext.getDDMFunctionsURL();
	}

	public static String getRolesURL() {
		DDMFormBuilderSettings ddmFormBuilderContext =
			getDDMFormBuilderContext();
		
		return ddmFormBuilderContext.getRolesURL();
	}

	protected static DDMFormBuilderSettings getDDMFormBuilderContext() {
		if (_ddmFormBuilderContext == null) {
			throw new IllegalStateException();
		}

		return _ddmFormBuilderContext;
	}

	@Reference(unbind = "-")
	protected void setDDMFormBuilderContext(
		DDMFormBuilderSettings ddmFormBuilderContext) {

		_ddmFormBuilderContext = ddmFormBuilderContext;
	}

	private static DDMFormBuilderSettings _ddmFormBuilderContext;

}