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

@generated
--%>

<%@ include file="/init.jsp" %>

<%
java.lang.String portletNamespace = GetterUtil.getString((java.lang.String)request.getAttribute("liferay-form:define-form-builder-settings:portletNamespace"));
com.liferay.dynamic.data.mapping.model.DDMForm ddmForm = (com.liferay.dynamic.data.mapping.model.DDMForm)request.getAttribute("liferay-form:define-form-builder-settings:ddmForm");
Map<String, Object> dynamicAttributes = (Map<String, Object>)request.getAttribute("liferay-form:define-form-builder-settings:dynamicAttributes");
%>

<%@ include file="/define_form_builder_settings/init-ext.jspf" %>