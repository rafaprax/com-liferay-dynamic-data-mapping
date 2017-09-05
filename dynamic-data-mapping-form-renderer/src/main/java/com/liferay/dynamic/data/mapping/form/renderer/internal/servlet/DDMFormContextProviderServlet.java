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

package com.liferay.dynamic.data.mapping.form.renderer.internal.servlet;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextProcessor;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextProcessorResult;
import com.liferay.dynamic.data.mapping.form.renderer.internal.DDMFormPagesTemplateContextFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.ParamUtil;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/dynamic-data-mapping-form-context-provider",
		"osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.renderer.internal.servlet.DDMFormContextProviderServlet",
		"osgi.http.whiteboard.servlet.pattern=/dynamic-data-mapping-form-context-provider/*"
	},
	service = Servlet.class
)
public class DDMFormContextProviderServlet extends HttpServlet {

	protected List<Object> createDDMFormPagesTemplateContext(
		HttpServletRequest request, HttpServletResponse response,
		String portletNamespace) {

		try {
			DDMFormRenderingContext ddmFormRenderingContext =
				createDDMFormRenderingContext(
					request, response, Locale.US, portletNamespace);

			DDMFormTemplateContextProcessorResult
				ddmFormTemplateContextProcessorResult =
					getDDMFormTemplateContextProcessorResult(request);

			DDMFormValues ddmFormValues =
				ddmFormTemplateContextProcessorResult.getProperty(
					"ddmFormValues");

			ddmFormRenderingContext.setDDMFormValues(ddmFormValues);

			ddmFormRenderingContext.setGroupId(
				ddmFormTemplateContextProcessorResult.getProperty("groupId"));

			_prepareThreadLocal(Locale.US);

			DDMForm ddmForm = ddmFormTemplateContextProcessorResult.getProperty(
				"ddmForm");

			DDMFormLayout ddmFormLayout =
				ddmFormTemplateContextProcessorResult.getProperty(
					"ddmFormLayout");

			DDMFormPagesTemplateContextFactory
				ddmFormPagesTemplateContextFactory =
					new DDMFormPagesTemplateContextFactory(
						ddmForm, ddmFormLayout, ddmFormRenderingContext);

			ddmFormPagesTemplateContextFactory.setDDMFormEvaluator(
				_ddmFormEvaluator);
			ddmFormPagesTemplateContextFactory.
				setDDMFormFieldTypeServicesTracker(
					_ddmFormFieldTypeServicesTracker);

			return ddmFormPagesTemplateContextFactory.create();
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}

		return null;
	}

	protected DDMFormRenderingContext createDDMFormRenderingContext(
		HttpServletRequest request, HttpServletResponse response, Locale locale,
		String portletNamespace) {

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(request);
		ddmFormRenderingContext.setHttpServletResponse(response);
		ddmFormRenderingContext.setLocale(locale);
		ddmFormRenderingContext.setPortletNamespace(portletNamespace);

		return ddmFormRenderingContext;
	}

	@Override
	protected void doPost(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		String portletNamespace = ParamUtil.getString(
			request, "portletNamespace");

		List<Object> ddmFormPagesTemplateContext =
			createDDMFormPagesTemplateContext(
				request, response, portletNamespace);

		if (ddmFormPagesTemplateContext == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}

		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		response.setContentType(ContentTypes.APPLICATION_JSON);
		response.setStatus(HttpServletResponse.SC_OK);

		ServletResponseUtil.write(
			response,
			jsonSerializer.serializeDeep(ddmFormPagesTemplateContext));
	}

	protected DDMFormTemplateContextProcessorResult
			getDDMFormTemplateContextProcessorResult(HttpServletRequest request)
		throws Exception {

		String serializedFormContext = ParamUtil.getString(
			request, "serializedFormContext");

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			serializedFormContext);

		return _ddmFormTemplateContextProcessor.process(jsonObject);
	}

	private void _prepareThreadLocal(Locale locale)
		throws Exception, PortalException {

		LocaleThreadLocal.setThemeDisplayLocale(locale);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormContextProviderServlet.class);

	private static final long serialVersionUID = 1L;

	@Reference
	private DDMFormEvaluator _ddmFormEvaluator;

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private DDMFormTemplateContextProcessor _ddmFormTemplateContextProcessor;

	@Reference
	private JSONFactory _jsonFactory;

}