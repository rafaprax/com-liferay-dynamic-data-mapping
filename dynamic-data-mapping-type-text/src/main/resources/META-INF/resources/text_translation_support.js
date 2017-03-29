AUI.add(
	'liferay-ddm-form-field-text-translation-support',
	function(A) {
		var AArray = A.Array;

		var Lang = A.Lang;

		var SoyTemplateUtil = Liferay.DDM.SoyTemplateUtil;

		var TextFieldTranslationSupport = function() {};

		TextFieldTranslationSupport.prototype = {
			destructor: function() {
				var instance = this;

				if (instance.get('localizable')) {
					Liferay.InputLocalized.unregister(instance.getQualifiedName());
				}
			},

			initializer: function() {
				var instance = this;

				if (instance.get('localizable')) {
					instance._eventHandlers.push(
						instance.bindContainerEvent('mouseenter', instance._showLocalizedPanel, '*'),
						instance.bindContainerEvent('focus', instance._showLocalizedPanel, '*'),
						instance.bindContainerEvent('mouseleave', instance._hideLocalizedPanel, '*')
					);
				}
			},

			getLocalizedValues: function() {
				var instance = this;

				var value = {};

				var inputLocalizedInstance = instance._getInputLocalizedInstance();

				if (inputLocalizedInstance) {
					var availableLocalesMetadata = instance.get('availableLocalesMetadata');

					availableLocalesMetadata.forEach(function(availableLocaleMetadata) {
						var languageValue =  inputLocalizedInstance.getValue(availableLocaleMetadata.languageId);

						if (languageValue) {
							value[availableLocaleMetadata.languageId] = languageValue;
						}
					});
				}

				return value;
			},

			_createInputLocalized: function() {
				var instance = this;

				var inputParentNode = instance.getInputNode().get('parentNode');

				Liferay.InputLocalized.register(
					instance.getQualifiedName(),
					{
						boundingBox: inputParentNode,
						columns: 20,
						contentBox: inputParentNode.one('.input-localized-content'),
						defaultLanguageId: instance.get('locale'),
						fieldPrefix: "",
						fieldPrefixSeparator: "",
						id: instance.get('fieldName'),
						inputPlaceholder: instance.getInputSelector(),
						instanceId: instance.getQualifiedName(),
						items: ["en_US", "zh_CN", "es_ES", "ja_JP", "nl_NL", "hu_HU", "pt_BR", "de_DE", "iw_IL", "fi_FI", "ca_ES", "fr_FR"],
						itemsError: [],
						lazy: true,
						name: instance.get('fieldName'),
						namespace: instance.get('portletNamespace'),
						toggleSelection: false,
						translatedLanguages: instance.get('locale')
					}
				);
			},

			_getInputLocalizedInstance: function() {
				var instance = this;

				return Liferay.InputLocalized._instances[instance.getQualifiedName()];
			},

			_showLocalizedPanel: function() {
				var instance = this;

				if (!instance._getInputLocalizedInstance()) {
					instance._createInputLocalized();
				}

				instance.get('container').one('.input-localized-content').removeClass('hidden');
			},

			_hideLocalizedPanel: function() {
				var instance = this;

				instance.get('container').one('.input-localized-content').addClass('hidden');
			},

			_getTemplate: function(context) {
				var instance = this;

				var renderer = SoyTemplateUtil.getTemplateRenderer('ddm.available_locales');

				return renderer(context);
			}
		};

		Liferay.namespace('DDM.Field').TextFieldTranslationSupport = TextFieldTranslationSupport;
	},
	'',
	{
		requires: ['liferay-ddm-form-field-text-template', 'liferay-ddm-soy-template-util', 'liferay-input-localized']
	}
);