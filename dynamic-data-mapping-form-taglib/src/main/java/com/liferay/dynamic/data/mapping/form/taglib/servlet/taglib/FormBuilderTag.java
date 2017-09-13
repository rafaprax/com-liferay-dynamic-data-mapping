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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rafael Praxedes
 */
public class FormBuilderTag extends BaseFormBuilderTag {

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

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
	}

	private static final Log _log = LogFactoryUtil.getLog(FormBuilderTag.class);

}