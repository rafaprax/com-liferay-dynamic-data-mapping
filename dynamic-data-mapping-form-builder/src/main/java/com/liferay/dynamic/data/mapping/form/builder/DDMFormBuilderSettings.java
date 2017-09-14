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

package com.liferay.dynamic.data.mapping.form.builder;

import aQute.bnd.annotation.ProviderType;

import com.liferay.dynamic.data.mapping.model.DDMForm;

import java.util.Locale;

/**
 * @author Rafael Praxedes
 */
@ProviderType
public interface DDMFormBuilderSettings {

	public String getDDMDataProviderInstanceParameterSettingsURL();

	public String getDDMDataProviderInstancesURL();

	public String getDDMFieldSettingsDDMFormContextURL();

	public String getDDMFunctionsURL();

	public String getRolesURL();

	public String getSerializedDDMExpressionFunctionsMetadata(Locale locale);

	public String getSerializedDDMFormRules(DDMForm ddmForm);

}