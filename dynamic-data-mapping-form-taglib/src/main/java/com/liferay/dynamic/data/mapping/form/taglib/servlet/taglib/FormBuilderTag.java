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

import com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.base.BaseFormBuilderTag;
import com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.util.FormTaglibContextUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rafael Praxedes
 */
public class FormBuilderTag extends BaseFormBuilderTag {

	public String getDDMFormBuilderContext() {
		String ddmFormBuilderContext = super.getDdmFormBuilderContext();

		if (ddmFormBuilderContext == null) {
			return StringPool.BLANK;
		}

		return ddmFormBuilderContext;
	}

	protected DDMForm getDDMForm() {
		DDMForm ddmForm = super.getDdmForm();

		if (ddmForm == null) {
			return new DDMForm();
		}

		return ddmForm;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		setNamespacedAttribute(
			request, "evaluatorURL",
			FormTaglibContextUtil.getDDMFormContextProviderURL());
		setNamespacedAttribute(
			request, "formBuilderContext", getDDMFormBuilderContext());
		setNamespacedAttribute(
			request, "functionsMetadata",
			FormTaglibContextUtil.getSerializedDDMExpressionFunctionsMetadata(
				themeDisplay.getLocale()));
		setNamespacedAttribute(
			request, "ddmDataProviderInstanceParameterSettingsURL",
			FormTaglibContextUtil.
				getDDMDataProviderInstanceParameterSettingsURL());
		setNamespacedAttribute(
			request, "ddmDataProviderInstancesURL",
			FormTaglibContextUtil.getDDMDataProviderInstancesURL());
		setNamespacedAttribute(
			request, "ddmFieldSettingsDDMFormContextURL",
			FormTaglibContextUtil.getDDMFieldSettingsDDMFormContextURL());
		setNamespacedAttribute(
			request, "ddmFunctionsURL",
			FormTaglibContextUtil.getDDMFunctionsURL());
		setNamespacedAttribute(
			request, "rolesURL", FormTaglibContextUtil.getRolesURL());
		setNamespacedAttribute(
			request, "serializedDDMFormRules",
			FormTaglibContextUtil.getSerializedDDMFormRules(getDDMForm()));
	}

	private static final Log _log = LogFactoryUtil.getLog(FormBuilderTag.class);

}