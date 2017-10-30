Liferay.on(
	'ddmFieldBlur', function(event) {
		DDMFormAnalytics.track(
			'FIELD_BLUR',
			{
				fieldName: event.fieldName,
				formId: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFieldFocus', function(event) {
		DDMFormAnalytics.track(
			'FIELD_FOCUS',
			{
				fieldName: event.fieldName,
				formId: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFieldStartedFilling', function(event) {
		DDMFormAnalytics.track(
			'FIELD_STARTED_FILLING',
			{
				fieldName: event.fieldName,
				formId: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFormPageShow', function(event) {
		DDMFormAnalytics.track(
			'FORM_PAGE_SHOW',
			{
				formId: event.formId,
				page: event.page
			}
		);
	}
);

Liferay.on(
	'ddmFormPageHide', function(event) {
		DDMFormAnalytics.track(
			'FORM_PAGE_HIDE',
			{
				formId: event.formId,
				page: event.page
			}
		);
	}
);

Liferay.on(
	'ddmFormValidationError', function(event) {
		DDMFormAnalytics.track(
			'FORM_VALIDATION_ERROR',
			{
				formId: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFormView', function(event) {
		DDMFormAnalytics.track(
			'FORM_VIEW',
			{
				formId: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFormSubmit', function(event) {
		DDMFormAnalytics.track(
			'FORM_SUBMIT',
			{
				formId: event.formId,
				page: event.page
			}
		);
	}
);