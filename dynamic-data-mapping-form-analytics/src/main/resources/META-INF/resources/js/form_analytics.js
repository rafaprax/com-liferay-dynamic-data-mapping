var DDMFormAnalytics = {
	track: function(event, attributes) {
		var instance = this;

		if (!attributes.formId) {
			return;
		}

		jQuery.post({
			url: '/o/dynamic-data-mapping-form-analytics-event/',
			data: {
				event: event,
				attributes: JSON.stringify(attributes),
				userId: themeDisplay.getUserId()
			}
		}).done(function() {
			console.log("done");
		});
	}
};

window.DDMFormAnalytics = DDMFormAnalytics;