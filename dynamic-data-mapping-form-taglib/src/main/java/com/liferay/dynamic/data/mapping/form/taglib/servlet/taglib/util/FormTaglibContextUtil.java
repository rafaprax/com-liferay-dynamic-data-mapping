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
import com.liferay.dynamic.data.mapping.model.DDMForm;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(immediate = true)
public class FormTaglibContextUtil {

	public static String getDDMDataProviderInstanceParameterSettingsURL() {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.
			getDDMDataProviderInstanceParameterSettingsURL();
	}

	public static String getDDMDataProviderInstancesURL() {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.getDDMDataProviderInstancesURL();
	}

	public static String getDDMFieldSettingsDDMFormContextURL() {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.getDDMFieldSettingsDDMFormContextURL();
	}

	public static String getDDMFunctionsURL() {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.getDDMFunctionsURL();
	}

	public static String getRolesURL() {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.getRolesURL();
	}

	public static String getSerializedDDMExpressionFunctionsMetadata() {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.
			getSerializedDDMExpressionFunctionsMetadata();
	}

	public static String getSerializedDDMFormRules(DDMForm ddmForm) {
		DDMFormBuilderSettings ddmFormBuilderSettings =
			getDDMFormBuilderSettings();

		return ddmFormBuilderSettings.getSerializedDDMFormRules(ddmForm);
	}

	protected static DDMFormBuilderSettings getDDMFormBuilderSettings() {
		if (_ddmFormBuilderSettings == null) {
			throw new IllegalStateException();
		}

		return _ddmFormBuilderSettings;
	}

	@Reference(unbind = "-")
	protected void setDDMFormBuilderSettings(
		DDMFormBuilderSettings ddmFormBuilderSettings) {

		_ddmFormBuilderSettings = ddmFormBuilderSettings;
	}

	private static DDMFormBuilderSettings _ddmFormBuilderSettings;

}