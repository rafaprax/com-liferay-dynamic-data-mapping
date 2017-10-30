Liferay.on(
	'ddmFormView', function(event) {
		DDMFormAnalytics.track(
			'FORM_VIEW',
			{
				id: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFormSubmit', function(event) {
		DDMFormAnalytics.track(
			'FORM_SUBMIT',
			{
				id: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFormValidationError', function(event) {
		DDMFormAnalytics.track(
			'FORM_VALIDATION_ERROR',
			{
				id: event.formId
			}
		);
	}
);

Liferay.on(
	'ddmFieldValueChange', function(event) {
		DDMFormAnalytics.track(
			'FIELD_VALUE_CHANGE',
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