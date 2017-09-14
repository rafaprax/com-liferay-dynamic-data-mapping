<%--
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
--%>

<%@ include file="/form_builder/init.jsp" %>

<aui:script>
	Liferay.namespace('DDM').Settings = {
		portletNamespace: '<%= refererPortletNamespace %>',
		functionsMetadata: <%= functionsMetadata %>,
		getDataProviderInstancesURL: '<%= ddmDataProviderInstancesURL %>',
		getDataProviderParametersSettingsURL: '<%= ddmDataProviderInstanceParameterSettingsURL %>',
		getFieldTypeSettingFormContextURL: '<%= ddmFieldSettingsDDMFormContextURL %>',
		getFunctionsURL: '<%= ddmFunctionsURL %>',
		getRolesURL: '<%= rolesURL %>'
	}
</aui:script>

<aui:script use="liferay-ddm-form-builder, liferay-ddm-form-builder-rule-builder">

	var <%= refererPortletNamespace %>formBuilder = new Liferay.DDM.FormBuilder(
			{
				context: <%= ddmFormBuilderContext %>,
				defaultLanguageId: '<%= defaultLanguageId %>',
				editingLanguageId: '<%= editingLanguageId %>'
			}
		);

	Liferay.component('<%= refererPortletNamespace %>formBuilder', <%= refererPortletNamespace %>formBuilder);

	var <%= refererPortletNamespace %>ruleBuilder = new Liferay.DDM.FormBuilderRuleBuilder(
			{
				formBuilder: <%= refererPortletNamespace %>formBuilder,
				rules: <%= serializedDDMFormRules %>,
				visible: false
			}
		);

	Liferay.component('<%= refererPortletNamespace %>ruleBuilder', <%= refererPortletNamespace %>ruleBuilder);
</aui:script>