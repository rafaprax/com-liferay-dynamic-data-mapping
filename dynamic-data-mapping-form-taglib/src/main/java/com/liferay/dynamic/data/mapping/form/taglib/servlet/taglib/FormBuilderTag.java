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

package com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib;

import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsRequest;
import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsResponse;
import com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.base.BaseFormBuilderTag;
import com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.util.FormTaglibContextUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rafael Praxedes
 */
public class FormBuilderTag extends BaseFormBuilderTag {

	public String getDDMFormBuilderContext(ThemeDisplay themeDisplay) {
		return FormTaglibContextUtil.getFormBuilderContext(
			GetterUtil.getLong(getDdmStructureId()), themeDisplay);
	}

	protected DDMForm getDDMForm() {
		return FormTaglibContextUtil.getDDMForm(
			GetterUtil.getLong(getDdmStructureId()));
	}

	protected DDMFormBuilderSettingsResponse getDDMFormBuilderSettings(
		HttpServletRequest request) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return FormTaglibContextUtil.getDDMFormBuilderSettings(
			DDMFormBuilderSettingsRequest.with(
				getDDMForm(), themeDisplay.getLocale()));
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

		DDMFormBuilderSettingsResponse ddmFormBuilderSettings =
			getDDMFormBuilderSettings(request);

		setNamespacedAttribute(
			request, "dataProviderInstancesURL",
			ddmFormBuilderSettings.getSetting("dataProviderInstancesURL"));
		setNamespacedAttribute(
			request, "dataProviderInstanceParameterSettingsURL",
			ddmFormBuilderSettings.getSetting(
				"dataProviderInstanceParameterSettingsURL"));
		setNamespacedAttribute(
			request, "evaluatorURL",
			ddmFormBuilderSettings.getSetting("formContextProviderURL"));
		setNamespacedAttribute(
			request, "fieldSettingsDDMFormContextURL",
			ddmFormBuilderSettings.getSetting(
				"fieldSettingsDDMFormContextURL"));

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		setNamespacedAttribute(
			request, "formBuilderContext",
			getDDMFormBuilderContext(themeDisplay));

		setNamespacedAttribute(
			request, "functionsMetadata",
			ddmFormBuilderSettings.getSetting("functionsMetadata"));
		setNamespacedAttribute(
			request, "functionsURL",
			ddmFormBuilderSettings.getSetting("dataProviderInstancesURL"));
		setNamespacedAttribute(
			request, "rolesURL", ddmFormBuilderSettings.getSetting("rolesURL"));
		setNamespacedAttribute(
			request, "serializedDDMFormRules",
			ddmFormBuilderSettings.getSetting("serializedDDMFormRules"));
	}

}