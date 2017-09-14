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

import com.liferay.dynamic.data.mapping.form.builder.DDMFormBuilderSettings;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.DDMFormRuleToDDLFormRuleConverter;
import com.liferay.dynamic.data.mapping.form.builder.internal.converter.model.DDLFormRule;
import com.liferay.dynamic.data.mapping.form.builder.internal.util.DDMExpressionFunctionMetadataHelper;
import com.liferay.dynamic.data.mapping.form.builder.internal.util.DDMExpressionFunctionMetadataHelper.DDMExpressionFunctionMetadata;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONSerializer;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(immediate = true)
public class DDMFormBuilderSettingsImpl implements DDMFormBuilderSettings {

	@Override
	public String getDDMDataProviderInstanceParameterSettingsURL() {
		String servletContextPath = getServletContextPath(
			_ddmDataProviderInstanceParameterSettingsServlet);

		return servletContextPath.concat(
			"/dynamic-data-mapping-data-provider-instance-parameter-settings/");
	}

	@Override
	public String getDDMDataProviderInstancesURL() {
		String servletContextPath = getServletContextPath(
			_ddmDataProviderInstancesServlet);

		return servletContextPath.concat(
			"/dynamic-data-mapping-data-provider-instances/");
	}

	@Override
	public String getDDMFieldSettingsDDMFormContextURL() {
		String servletContextPath = getServletContextPath(
			_ddmFieldSettingsDDMFormContextServlet);

		return servletContextPath.concat(
			"/dynamic-data-mapping-field-settings-form-context/");
	}

	@Override
	public String getDDMFunctionsURL() {
		String servletContextPath = getServletContextPath(
			_ddmFormFunctionsServlet);

		return servletContextPath.concat(
			"/dynamic-data-mapping-data-form-functions/");
	}

	@Override
	public String getRolesURL() {
		String servletContextPath = getServletContextPath(_rolesServlet);

		return servletContextPath.concat("/dynamic-data-mapping-roles/");
	}

	@Override
	public String getSerializedDDMExpressionFunctionsMetadata(Locale locale) {
		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		Map<String, List<DDMExpressionFunctionMetadata>>
			ddmExpressionFunctionsMetadata =
				_ddmExpressionFunctionMetadataHelper.
					getDDMExpressionFunctionsMetadata(locale);

		return jsonSerializer.serializeDeep(ddmExpressionFunctionsMetadata);
	}

	@Override
	public String getSerializedDDMFormRules(DDMForm ddmForm) {
		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		List<DDLFormRule> ddlFormRules =
			_ddmFormRuleToDDLFormRuleConverter.convert(
				ddmForm.getDDMFormRules());

		return jsonSerializer.serializeDeep(ddlFormRules);
	}

	protected static String getServletContextPath(Servlet servlet) {
		ServletConfig servletConfig = servlet.getServletConfig();

		ServletContext servletContext = servletConfig.getServletContext();

		return servletContext.getContextPath();
	}

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.builder.internal.servlet.DDMDataProviderInstanceParameterSettingsServlet)",
		unbind = "-"
	)
	protected void setDataProviderInstanceParameterSettingsServlet(
		Servlet ddmDataProviderInstanceParameterSettingsServlet) {

		_ddmDataProviderInstanceParameterSettingsServlet =
			ddmDataProviderInstanceParameterSettingsServlet;
	}

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.builder.internal.servlet.DDMDataProviderInstancesServlet)",
		unbind = "-"
	)
	protected void setDataProviderInstancesServlet(
		Servlet ddmDataProviderInstancesServlet) {

		_ddmDataProviderInstancesServlet = ddmDataProviderInstancesServlet;
	}

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.builder.internal.servlet.DDMFieldSettingsDDMFormContextServlet)",
		unbind = "-"
	)
	protected void setDDMFieldSettingsDDMFormContextServlet(
		Servlet ddmFieldSettingsDDMFormContextServlet) {

		_ddmFieldSettingsDDMFormContextServlet =
			ddmFieldSettingsDDMFormContextServlet;
	}

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.builder.internal.servlet.DDMFormFunctionsServlet)",
		unbind = "-"
	)
	protected void setFormFunctionsServlet(Servlet ddmFormFunctionsServlet) {
		_ddmFormFunctionsServlet = ddmFormFunctionsServlet;
	}

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.builder.internal.servlet.RolesServlet)",
		unbind = "-"
	)
	protected void setRolesServlet(Servlet rolesServlet) {
		_rolesServlet = rolesServlet;
	}

	private Servlet _ddmDataProviderInstanceParameterSettingsServlet;
	private Servlet _ddmDataProviderInstancesServlet;

	@Reference
	private DDMExpressionFunctionMetadataHelper
		_ddmExpressionFunctionMetadataHelper;

	private Servlet _ddmFieldSettingsDDMFormContextServlet;
	private Servlet _ddmFormFunctionsServlet;

	@Reference
	private DDMFormRuleToDDLFormRuleConverter
		_ddmFormRuleToDDLFormRuleConverter;

	@Reference
	private JSONFactory _jsonFactory;

	private Servlet _rolesServlet;

}