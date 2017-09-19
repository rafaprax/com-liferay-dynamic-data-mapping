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

package com.liferay.dynamic.data.mapping.form.builder.internal.settings;

import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsRequest;
import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsResponse;
import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsRetriever;
import com.liferay.dynamic.data.mapping.model.DDMForm;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(immediate = true, service = DDMFormBuilderSettingsRetriever.class)
public class DDMFormBuilderSettingsRetrieverImpl
	implements DDMFormBuilderSettingsRetriever {

	@Override
	public DDMFormBuilderSettingsResponse getSettings(
		DDMFormBuilderSettingsRequest ddmFormBuilderSettingsRequest) {

		DDMFormBuilderSettingsResponse ddmFormBuilderSettings =
			new DDMFormBuilderSettingsResponse();

		ddmFormBuilderSettings.addSettings(
			"dataProviderInstanceParameterSettingsURL",
			_ddmFormBuilderSettingsRetrieverHelper.
				getDDMDataProviderInstanceParameterSettingsURL());

		ddmFormBuilderSettings.addSettings(
			"dataProviderInstancesURL",
			_ddmFormBuilderSettingsRetrieverHelper.
				getDDMDataProviderInstancesURL());
		ddmFormBuilderSettings.addSettings(
			"fieldSettingsDDMFormContextURL",
			_ddmFormBuilderSettingsRetrieverHelper.
				getDDMFieldSettingsDDMFormContextURL());
		ddmFormBuilderSettings.addSettings(
			"formContextProviderURL",
			_ddmFormBuilderSettingsRetrieverHelper.
				getDDMFormContextProviderURL());
		ddmFormBuilderSettings.addSettings(
			"functionsURL",
			_ddmFormBuilderSettingsRetrieverHelper.getDDMFunctionsURL());
		ddmFormBuilderSettings.addSettings(
			"rolesURL", _ddmFormBuilderSettingsRetrieverHelper.getRolesURL());

		Locale locale = ddmFormBuilderSettingsRequest.getProperty("locale");

		ddmFormBuilderSettings.addSettings(
			"functionsMetadata",
			_ddmFormBuilderSettingsRetrieverHelper.
				getSerializedDDMExpressionFunctionsMetadata(locale));

		DDMForm ddmForm = ddmFormBuilderSettingsRequest.getProperty("ddmForm");

		ddmFormBuilderSettings.addSettings(
			"serializedDDMFormRules",
			_ddmFormBuilderSettingsRetrieverHelper.getSerializedDDMFormRules(
				ddmForm));

		return ddmFormBuilderSettings;
	}

	@Reference
	private DDMFormBuilderSettingsRetrieverHelper
		_ddmFormBuilderSettingsRetrieverHelper;

}