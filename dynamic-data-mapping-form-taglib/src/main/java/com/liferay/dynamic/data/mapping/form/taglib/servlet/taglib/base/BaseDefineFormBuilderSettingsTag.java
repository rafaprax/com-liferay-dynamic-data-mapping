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

package com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.base;

import com.liferay.dynamic.data.mapping.form.taglib.internal.servlet.ServletContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Rafael Praxedes
 * @generated
 */
public abstract class BaseDefineFormBuilderSettingsTag extends com.liferay.taglib.util.IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public java.lang.String getPortletNamespace() {
		return _portletNamespace;
	}

	public com.liferay.dynamic.data.mapping.model.DDMForm getDdmForm() {
		return _ddmForm;
	}

	public void setPortletNamespace(java.lang.String portletNamespace) {
		_portletNamespace = portletNamespace;
	}

	public void setDdmForm(com.liferay.dynamic.data.mapping.model.DDMForm ddmForm) {
		_ddmForm = ddmForm;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_portletNamespace = null;
		_ddmForm = null;
	}

	@Override
	protected String getEndPage() {
		return _END_PAGE;
	}

	@Override
	protected String getStartPage() {
		return _START_PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("liferay-form:define-form-builder-settings:portletNamespace", _portletNamespace);
		request.setAttribute("liferay-form:define-form-builder-settings:ddmForm", _ddmForm);
	}

	protected static final String _ATTRIBUTE_NAMESPACE = "liferay-form:define-form-builder-settings:";

	private static final String _END_PAGE =
		"/define_form_builder_settings/end.jsp";

	private static final String _START_PAGE =
		"/define_form_builder_settings/start.jsp";

	private java.lang.String _portletNamespace = null;
	private com.liferay.dynamic.data.mapping.model.DDMForm _ddmForm = null;

}