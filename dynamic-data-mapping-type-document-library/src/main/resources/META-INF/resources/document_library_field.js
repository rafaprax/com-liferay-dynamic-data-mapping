AUI.add(
	'liferay-ddm-form-field-document-library',
	function(A) {
		var Lang = A.Lang;

		var DocumentLibraryField = A.Component.create({

			ATTRS: {
				acceptedFileFormats: {
					value: ['*']
				},

				clearButtonVisible: {
					value: false
				},

				fileEntryTitle: {
					value: ''
				},

				fileEntryURL: {
					value: ''
				},

				groupId: {
					value: 0
				},

				lexiconIconsPath: {
					getter: '_getLexiconIconsPath',
					value: ''
				},

				strings: {
					value: {
						select: Liferay.Language.get('select')
					}
				},

				type: {
					value: 'document_library'
				},

				value: {
					value: ''
				}
			},

			EXTENDS: Liferay.DDM.Renderer.Field,

			NAME: 'liferay-ddm-form-field-document-library',

			prototype: {
				initializer: function() {
					var instance = this;

					instance._eventHandlers.push(
						instance.bindContainerEvent('click', instance._handleButtonsClick, '> .form-group .btn')
					);
				},

				getDocumentLibrarySelectorURL: function() {
					var instance = this;

					var portletNamespace = instance.get('portletNamespace');

					var portletURL = Liferay.PortletURL.createURL(themeDisplay.getLayoutRelativeControlPanelURL());

					portletURL.setParameter('criteria', 'com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion');
					portletURL.setParameter('doAsGroupId', instance.get('groupId'));
					portletURL.setParameter('itemSelectedEventName', portletNamespace + 'selectDocumentLibrary');

					var criterionJSON = {
						desiredItemSelectorReturnTypes: 'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType,com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType'
					};

					portletURL.setParameter('0_json', JSON.stringify(criterionJSON));
					portletURL.setParameter('1_json', JSON.stringify(criterionJSON));

					var uploadCriterionJSON = {
						desiredItemSelectorReturnTypes: 'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType,com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType',
						URL: instance.getUploadURL()
					};

					portletURL.setParameter('2_json', JSON.stringify(uploadCriterionJSON));

					portletURL.setPortletId(Liferay.PortletKeys.ITEM_SELECTOR);
					portletURL.setPortletMode('view');
					portletURL.setWindowState('pop_up');

					return portletURL.toString();
				},

				getInputNode: function() {
					var instance = this;

					var container = instance.get('container');

					var inputNode = container.one('input[type="hidden"]');

					return inputNode;
				},

				getParsedValue: function(value) {
					var instance = this;

					if (Lang.isString(value)) {
						if (value !== '') {
							value = JSON.parse(value);
						}
						else {
							value = {};
						}
					}

					return value;
				},

				getTemplateContext: function() {
					var instance = this;

					return A.merge(
						DocumentLibraryField.superclass.getTemplateContext.apply(instance, arguments),
						{
							clearButtonVisible: instance.get('clearButtonVisible'),
							fileEntryTitle: instance.get('fileEntryTitle'),
							lexiconIconsPath: instance.get('lexiconIconsPath'),
							strings: instance.get('strings')
						}
					);
				},

				getUploadURL: function() {
					var instance = this;

					var portletURL = Liferay.PortletURL.createURL(themeDisplay.getLayoutRelativeURL());

					portletURL.setLifecycle(Liferay.PortletURL.ACTION_PHASE);
					portletURL.setParameter('cmd', 'add_temp');
					portletURL.setParameter('javax.portlet.action', '/document_library/upload_file_entry');
					portletURL.setParameter('p_auth', Liferay.authToken);
					portletURL.setPortletId(Liferay.PortletKeys.DOCUMENT_LIBRARY);

					return portletURL.toString();
				},

				getValue: function() {
					var instance = this;

					return instance.get('value');
				},

				setValue: function(value) {
					var instance = this;

					var parsedValue = instance.getParsedValue(value);

					if (!parsedValue.title && !parsedValue.uuid) {
						value = '';
						instance.set('fileEntryTitle', '');
						instance.set('clearButtonVisible', false);
					}
					else {
						value = JSON.stringify(parsedValue);
						instance.set('fileEntryTitle', parsedValue.title);
						instance.set('clearButtonVisible', true);
					}

					instance.set('value', value);

					instance.render();
				},

				showErrorMessage: function() {
					var instance = this;

					var container = instance.get('container');

					DocumentLibraryField.superclass.showErrorMessage.apply(instance, arguments);

					container.all('.help-block').appendTo(container.one('.form-group'));
				},

				_getLexiconIconsPath: function() {
					var instance = this;

					return themeDisplay.getPathThemeImages() + '/lexicon/icons.svg#';
				},

				_handleButtonsClick: function(event) {
					var instance = this;

					if (!instance.get('readOnly')) {
						var currentTarget = event.currentTarget;

						if (currentTarget.test('.select-button')) {
							instance._handleSelectButtonClick(event);
						}
						else if (currentTarget.test('.clear-button')) {
							instance._handleClearButtonClick(event);
						}
					}
				},

				_handleClearButtonClick: function(event) {
					var instance = this;

					instance.setValue('');
				},

				_handleSelectButtonClick: function(event) {
					var instance = this;

					var portletNamespace = instance.get('portletNamespace');

					var itemSelectorDialog = new A.LiferayItemSelectorDialog(
						{
							eventName: portletNamespace + 'selectDocumentLibrary',
							on: {
								selectedItemChange: function(event) {
									var selectedItem = event.newVal;

									if (selectedItem) {
										var itemValue = JSON.parse(selectedItem.value);

										instance.setValue(
											{
												groupId: itemValue.groupId,
												title: itemValue.title,
												type: itemValue.type,
												uuid: itemValue.uuid
											}
										);
									}
								}
							},
							url: instance.getDocumentLibrarySelectorURL()
						}
					);

					itemSelectorDialog.open();
				}
			}
		});

		Liferay.namespace('DDM.Field').DocumentLibrary = DocumentLibraryField;
	},
	'', {
		requires: ['liferay-ddm-form-renderer-field', 'liferay-item-selector-dialog']
	}
);