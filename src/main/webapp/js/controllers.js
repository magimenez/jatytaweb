App.ApplicationController = Ember.Controller
		.extend({
			needs : [ 'crawlrecord' ],
			executionQueue : [],
			updateQueue : function(id, status) {
				var element = this.executionQueue.find(function(item) {
					return (item.id == id);
				});
				if (element != null)
					element.set('crawlStatus', status);
			},
			removeQueue : function(id) {
				var element = this.executionQueue.find(function(item) {
					return (item.id == id);
				});
				if (element != null)
					this.executionQueue.removeObject(element);
			},
			socket : null,
			connectSocket : function() {
				// create web socket url from current path
				var loc = window.location, host;
				if (loc.protocol === "https:") {
					host = "wss:";
				} else {
					host = "ws:";
				}
				host += "//" + loc.host;
				host += loc.pathname + "socket";

				try {
					var controller = this;
					this.socket = new WebSocket(host);

					this.socket.onmessage = function(msg) {
						if (msg.data.indexOf('log-') == 0)
							$('#logPanel').append(
									'<p>' + msg.data.slice(4) + '</p>');
						if (msg.data.indexOf('queue-') == 0) {
							var record = App.CrawlRecords.create({});
							record.setProperties(JSON.parse(msg.data.slice(6)));
							record.plugins = [];
							controller.get('executionQueue').pushObject(record);
						}
						if (msg.data.indexOf('init-') == 0)
							controller.updateQueue(msg.data.slice(5),
									"initializing");
						if (msg.data.indexOf('run-') == 0)
							controller
									.updateQueue(msg.data.slice(4), "running");
						if (msg.data.indexOf('fail-') == 0) {
							controller
									.updateQueue(msg.data.slice(5), "failure");
							setTimeout(function() {
								controller.removeQueue(msg.data.slice(5));
							}, 5000);
						}
						if (msg.data.indexOf('success-') == 0) {
							controller
									.updateQueue(msg.data.slice(8), "success");
							if (controller
									.get('controllers.crawlrecord.content.id') == msg.data
									.slice(8)) {
								controller.get('controllers.crawlrecord').set(
										'content',
										App.CrawlRecords
												.find(msg.data.slice(8)));
							}
							setTimeout(function() {
								controller.removeQueue(msg.data.slice(8));
							}, 5000);
						}
						if (msg.data.indexOf('message-') == 0) {
							var positivity = 0;
							if (msg.data.indexOf('success-') == 8) {
								positivity = 1;
								controller.displayMessage(msg.data.slice(16),
										positivity);
							} else if (msg.data.indexOf('error-') == 8) {
								positivity = -1;
								controller.displayMessage(msg.data.slice(14),
										positivity);
							} else {
								controller.displayMessage(msg.data.slice(8),
										positivity);
							}
						}
					};
					this.socket.onclose = function() {
						controller.connectSocket();
					};
				} catch (exception) {
					alert('Error' + exception);
				}
			},
			sendMsg : function(text) {
				try {
					var self = this;
					if (self.socket.readyState != 1) {
						setTimeout(function() {
							self.socket.send(text);
						}, 500);
					} else
						self.socket.send(text);
				} catch (exception) {
					alert("Socket Timed out. Refresh your browser. ");
				}
			},
			displayMessage : function(text, positivity) {
				var clazz = "info";
				if (positivity > 0)
					clazz = "success";
				if (positivity < 0)
					clazz = "error";
				$('#notification').removeClass().addClass("alert").addClass(
						"alert-" + clazz).text(text);
				clearTimeout(this.messageTimeout);
				this.messageTimeout = setTimeout(function() {
					$('#notification').removeClass().addClass("alert")
							.addClass("alert-mute");
				}, 3000);
			}
		});

App.BreadcrumbController = Ember.Controller.extend({
	needs : [ 'application' ]
});

App.SidenavController = Ember.Controller.extend({
	needs : [ 'application', 'configurationJatyta', 'configurationForms', 
	          'configurationLogin', 'configurationBrokenlinks', 
	          'configurationValidation' ]
// sidenav: null
});

App.ExecutionQueueController = Ember.Controller.extend({
	needs : [ 'application' ]
});

App.ConfigurationsIndexController = Ember.ArrayController.extend({
	needs : [ 'application' ],
	itemController : 'ConfigurationsIndexItem'
});

App.ConfigurationsIndexItemController = Ember.Controller.extend({
	lastCrawlFormatted : function() {
		var lastCrawl = this.get('content.lastCrawl');
		if (lastCrawl == null)
			return 'never';
		else
			return new Date(lastCrawl);
	}.property('content.lastCrawl'),
	lastDurationFormatted : function() {
		var lastDuration = this.get('content.lastDuration') / 1000;
		if (lastDuration == 0) {
			if (this.get('content.lastCrawl') == null)
				return 'n/a';
			else
				return 'running';
		}
		return Math.floor(lastDuration / 60) + ' min '
				+ Math.floor(lastDuration % 60) + ' sec';
	}.property('content.lastCrawl', 'content.lastDuration')
});

App.ConfigurationController = Ember.Controller
		.extend({
			needs : [ 'application', 'configurationJatyta', 
			          'configurationForms', 'configurationLogin', 
			          'configurationBrokenlinks', 'configurationValidation' ],
			rest : function(link) {
				switch (link.target) {
				case "add":
					if (validateForm('config_form')) {
						var router = this.get('target');
						App.Configurations.add(this.get("content"), function(
								data) {
							router.transitionToRoute('configuration', data);
						});
					}
					break;
				case "run":
					App.CrawlRecords.add(this.get("content.id"));
					break;
				case "save":
					if (validateForm("config_form")) {
						try {
							this.set('content', App.Configurations.update(this
									.get('content')));
							//actualiza jatyta configuration
							var jatyta = this.get("controllers.configurationJatyta").get('content');
							if(jatyta !=null){
								if (jatyta.idschema > 0) {
									var schema = App.Schemas.find(jatyta.idschema);
									jatyta.schema = schema;
								} else {
									jatyta.schema = null;
								}
							}else{
								//obtener desde el servicio rest
								jatyta = App.CrawlConfigurations.find(this.get('content').id);
							}
							// actualiza form configuration
							var jatytaForm = this.get("controllers.configurationForms").get('content');
							if(jatytaForm !=null) {
								jatyta.maxTextFieldSize = jatytaForm.maxTextFieldSize;
								jatyta.maxNumberFieldSize = jatytaForm.maxNumberFieldSize;
								jatyta.dateValuePattern = jatytaForm.dateValuePattern;
								jatyta.formSubmitElement = jatytaForm.formSubmitElement;
								jatyta.formSubmitAttribute = jatytaForm.formSubmitAttribute;
								jatyta.formSubmitAttributeValue =  jatytaForm.formSubmitAttributeValue;
								jatyta.formSubmitText = jatytaForm.formSubmitText;
							}
							//actualiza form List
							var jatytaFormList = this.get("controllers.configurationForms").get('formList');
							if(jatytaFormList!=null){
								for (var i = 0, l = jatytaFormList.length; i < l; i++) {
									var form = jatytaFormList[i];
									App.FormConfigurations.update(form);
								}
							}							
							//actualiza login configuration
							var jatytaLogin = this.get("controllers.configurationLogin").get('content');
							if(jatytaLogin!=null) {
								jatyta.loginUserNameXpath = jatytaLogin.loginUserNameXpath;
								jatyta.loginUserNameValue = jatytaLogin.loginUserNameValue;
								jatyta.loginPasswordXpath = jatytaLogin.loginPasswordXpath;
								jatyta.loginPasswordValue = jatytaLogin.loginPasswordValue;
								jatyta.loginSubmitXpath = jatytaLogin.loginSubmitXpath;
							}
							//update todos los datos.
							App.CrawlConfigurations.update(jatyta);
							
							//actualiza validation configuration
							var validations = this.get("controllers.configurationValidation").get('validations');
							if(validations!=null){
								for (var i = 0, l = validations.length; i < l; i++) {
									var validation = validations[i];
									App.ValidationConfigurations.update(validation);
								}
							}
							//actualiza broken link configuration
							var brokenLinks = this.get("controllers.configurationBrokenlinks").get('brokenLinks');
							if(brokenLinks!=null){
								for (var i = 0, l = brokenLinks.length; i < l; i++) {
									var brokenLink = brokenLinks[i];
									App.BrokenLinksConfigurations.update(brokenLink);
								}
							}

							this.get("controllers.application").displayMessage(
									"Configuration Saved", 1);
						} catch (exception) {
							this.get("controllers.application").displayMessage(
									exception, -1);
						}

					}
					break;
				case "delete":
					var r = confirm("Are you sure you want to permanently delete this configuration?");
					if (r == true) {
						var router = this.get('target');
						var _this = this;
						App.Configurations
								.remove(
										this.get("content"),
										function(data) {
											router
													.transitionToRoute('configurations');
											_this
													.get(
															"controllers.application")
													.displayMessage(
															"Configuration Deleted",
															1);
										});
					}
					break;
				}
			},
			moveTo : function(route) {
				if (validateForm('config_form')) {
					var router = this.get('target');
					router.transitionTo(route);
				}
			}
		});

App.ClickRulesController = Ember.ArrayController.extend({
	add : function() {
		this.content.pushObject({
			rule : 'click',
			elementTag : 'a',
			conditions : []
		});
	},
	remove : function(item) {
		this.content.removeObject(item);
	},
	itemController : 'clickRuleItem'
});

App.ClickRuleItemController = Ember.Controller.extend({
	addCondition : function() {
		this.content.conditions.pushObject({
			condition : 'wAttribute',
			expression : ''
		});
	},
	removeCondition : function(item) {
		this.content.conditions.removeObject(item);
	}
});

App.ConditionsController = Ember.ArrayController.extend({
	add : function() {
		this.content.pushObject({
			condition : 'url',
			expression : ''
		});
	},
	remove : function(item) {
		this.content.removeObject(item);
	},
	itemController : "conditionItem"
});

App.ConditionItemController = Ember.Controller.extend({

});

App.FormInputsController = Ember.ArrayController.extend({
	add : function() {
		this.content.pushObject({
			name : '',
			value : ''
		});
	},
	remove : function(item) {
		this.content.removeObject(item);
	},
	itemController : "formInputItem"
});

App.FormInputItemController = Ember.Controller.extend({

});

App.ComparatorsController = Ember.ArrayController.extend({
	add : function() {
		this.content.pushObject({
			type : 'attribute',
			expression : ''
		});
	},
	remove : function(item) {
		this.content.removeObject(item);
	},
	itemController : 'comparatorItem'
});

App.ComparatorItemController = Ember.Controller
		.extend({
			needsExpression : function() {
				var type = this.get('content.type');
				return (type == 'attribute' || type == 'xpath'
						|| type == 'distance' || type == 'regex');
			}.property('content.type')
		});

App.PluginsController = Ember.ArrayController.extend({
	add : function() {
		this.content.pushObject({
			id : null
		});
	},
	remove : function(item) {
		this.content.removeObject(item);
	},
	itemController : 'pluginItem'
});

App.PluginItemController = Ember.Controller.extend({
	contentChanged : function() {
		this.set("selectItems", App.Plugins.selectItems());
	}.observes("content"),
	selectionChanged : function(id) {
		var content = this.get('content');
		this.set('content.id', id);
		if (id) {
			var plugin = App.Plugins.find(id);
			this.set('plugin', plugin);
			var params = [];
			var values = content.parameters;
			if (plugin.parameters) {
				for (var i = 0, l = plugin.parameters.length; i < l; i++) {
					var parameter = plugin.parameters[i];
					for (var j = 0, l_2 = values.length; j < l_2; j++) {
						if (values[j].id === parameter.id) {
							parameter.value = values[j].value;
						}
					}
					params.push(parameter);
				}
			}
			this.set('content.parameters', params);
		} else {
			this.set('content.parameters', []);
		}
	}
});

App.PluginManagementController = Ember.ArrayController.extend({
	needs : [ 'application' ],
	refresh : function() {
		this.get("controllers.application").displayMessage(
				"Refreshing List...", 0);
		var _this = this;
		App.Plugins.refresh(function(plugins) {
			_this.set('content', plugins);
			_this.get("controllers.application").displayMessage(
					"List Refreshed", 1);
		});
	},
	addFile : function() {
		var file = this.get("pluginFile");
		if (!file) {
			alert("Please select a file");
			return;
		}
		if (file.name.indexOf(".jar") === -1
				|| file.name.indexOf(".jar") !== file.name.length - 4) {
			alert("Please select a .jar file");
			return;
		}
		this.get("controllers.application").displayMessage(
				"Uploading Plugin...", 0);
		var _this = this;
		var reader = new FileReader();
		reader.onload = function(e) {
			App.Plugins.add(file.name, e.target.result, undefined, function() {
				_this.set('content', App.Plugins.findAll());
			});
		}
		reader.readAsDataURL(file);
	},
	addURL : function() {
		var url = this.get("url");
		if (!url) {
			alert("Please enter a url");
			return;
		}
		var name = url.split("/").pop();
		this.get("controllers.application").displayMessage(
				"Downloading Plugin...", 0);
		var _this = this;
		App.Plugins.add(name, undefined, url, function() {
			_this.set('content', App.Plugins.findAll());
		});
	},
	itemController : 'pluginManagementItem'
});

App.PluginManagementItemController = Ember.Controller.extend({
	needs : [ 'application', 'pluginManagement' ],
	remove : function() {
		var r = confirm("Are you sure you want to remove "
				+ this.get('content.name') + " (id: " + this.get('content.id')
				+ ")");
		if (r == true) {
			var _this = this;
			App.Plugins.remove(this.get('content'), function() {
				_this.get('controllers.pluginManagement').set('content',
						App.Plugins.findAll());
				_this.get("controllers.application").displayMessage(
						"Plugin Deleted", 1);
			});
		}
	}
});

App.ConfigurationValidationController = Ember.Controller.extend({
	needs : [ 'application', 'configuration' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		newValidation : function(configId, list) {
			var validation = App.ValidationConfigurations.getNew();
			validation.configurationId = configId;
			list.addObject(validation);
		},
		saveAll : function(validations) {
			if (validateForm("config_form")) {
				try {
					for (var i = 0, l = validations.length; i < l; i++) {
						var validation = validations[i];
						App.ValidationConfigurations.update(validation);
					}
					this.get("controllers.application").displayMessage(
							"Validation Configuration Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		}
	}
});

App.ConfigurationJatytaController = Ember.Controller.extend({
	needs : [ 'application', 'configuration' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		save : function(configuration) {
			if (validateForm("config_form")) {
				try {
					if (configuration.idschema > 0) {
						var schema = App.Schemas.find(configuration.idschema);
						configuration.schema = schema;
					} else {
						configuration.schema = null;
					}
					App.CrawlConfigurations.update(configuration);
					this.get("controllers.application").displayMessage(
							"Crawl Configuration Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		},
		estimateTime : function(configuration) {
			if (validateForm("config_form")) {
				try {
					var estimatedTime = App.CrawlConfigurations.estimateTime(
							configuration.configurationId,
							configuration.maxValuesForFormInput);
					Ember.set(configuration, 'estimatedTime', estimatedTime);
						
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}

			}

		}

	}
});

App.ConfigurationFormsController = Ember.Controller.extend({
	needs : [ 'application', 'configuration' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		save : function(configuration) {
			if (validateForm("config_form")) {
				try {
					App.CrawlConfigurations.update(configuration);
					this.get("controllers.application").displayMessage(
							"Forms Configuration Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		},
		newForm : function(configId, list) {
			var form = App.FormConfigurations.getNew();
			form.configurationId = configId;
			list.addObject(form);
		},
		saveAll : function(forms) {
			if (validateForm("config_form")) {
				try {
					for (var i = 0, l = forms.length; i < l; i++) {
						var form = forms[i];
						App.FormConfigurations.update(form);
					}
					this.get("controllers.application").displayMessage(
						"Form Configuration Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
						exception, -1);
				}
			}
		},
		remove: function (model){
			try {
				if (model.id != null) {
					App.FormConfigurations.remove(model);
					this.get("controllers.application").displayMessage(
							"Form Deleted", 1);
				} else {
					this.get("controllers.application").displayMessage(
							"Form Removed", 1);
				}
				this.get('formList').removeObject(model);
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		}
	}
});

App.ConfigurationLoginController = Ember.Controller.extend({
	needs : [ 'application', 'configuration' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		save : function(configuration) {
			if (validateForm("config_form")) {
				try {
					App.CrawlConfigurations.update(configuration);
					this.get("controllers.application").displayMessage(
							"Login Configuration Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		}
	}
});

App.ConfigurationBrokenlinksController = Ember.Controller.extend({
	needs : [ 'application', 'configuration' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		newBrokenLink : function(configId, list) {
			var brokenLink = App.BrokenLinksConfigurations.getNew();
			brokenLink.configurationId = configId;
			list.addObject(brokenLink);
		},
		saveAll : function(configurations) {
			if (validateForm("config_form")) {
				try {
					for (var i = 0, l = configurations.length; i < l; i++) {
						var brokenLink = configurations[i];
						App.BrokenLinksConfigurations.update(brokenLink);
					}
					this.get("controllers.application").displayMessage(
							"Broken links Configuration Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		}
	}
});

App.CrawlrecordsIndexController = Ember.ArrayController.extend({
	needs : [ 'application' ],
	itemController : 'CrawlrecordsIndexItem',
	filter : '',
	filteredContent : function() {
		var filter = this.get('filter');
		var statusFilter = this.get('statusFilter');
		var rx = new RegExp(filter, 'gi');
		var list = this.get('content');

		if (!filter && !statusFilter) {
			return list;
		}
		return list.filter(function(item) {
			
			var status = String(item.get('crawlStatus'));
			if (statusFilter != 'all' && ( status == null || !status.match(statusFilter))) {
				return false;
			} else {
				var id = String(item.get('id'));
				if (id != null && id.match(rx)) {
					return true;
				} else {
					var configurationName = item.get('configurationName');
					if (configurationName != null && configurationName.match(rx)){
						return true;
					} else {
						var startTimeFormatted = String(item.get('startTimeFormatted'));
						if (startTimeFormatted != null && startTimeFormatted.match(rx)){
							return true;
						}
					}
					return false;
				}				
			}
		});
	}.property('content','statusFilter', 'filter'),
});

App.CrawlrecordsIndexItemController = Ember.Controller.extend({
	/*startTimeFormatted : function() {
		var startTime = this.get('content.startTime');
		if (startTime == null)
			return 'queued';
		else
			return new Date(startTime);
	}.property('content.startTime'),*/

	configURL : function() {
		return '#/configurations/' + this.get('content.configurationId');
	}.property('content.configurationId')
});

App.CrawlrecordController = Ember.Controller.extend({
	needs : [ 'application' ],
	isFinished : function() {
		return (this.get('content.crawlStatus') == 'success');
	}.property('content.crawlStatus')
});

App.FormfieldvaluesController = Ember.Controller.extend({
	needs : [ 'crawlrecord', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);

	},
	actions : {
		setupNewSchemaModal : function() {
			var schema = App.Schemas.getNew();
			return this.send('openModal', 'new-schema-modal', schema);

		},
		setupNewItemTypeModal : function() {
			var itemtype = App.ItemTypes.getNew();
			itemtype.idSchema = 0;
			App.schemaNames = App.Schemas.selectItems();
			return this.send('openModal', 'new-itemtype-modal', itemtype);
		},
		setupAddItemPropsModal : function(formValueRecords) {
			var model = Ember.Object.create({
				details : []
			});
			formValueRecords.forEach(function(valuerecord) {
				var type = null
				if (valuerecord.type == 'valid') {
					type = true;
				} else {
					type = false;
				}

				this.addObject(Ember.Object.create({
					idSchema : null,
					idItemType : null,
					propName : valuerecord.idFromXPathExpresion,
					value : valuerecord.value,
					idNativeType : null,
					comments : null,
					valid : type
				}))
			}, model.details);
			Ember.set('App.itemTypesSelect', App.ItemTypes.selectItems());
			Ember.set('App.nativeTypesSelect', App.NativeTypes.selectItems());
			return this.send('openModal', 'add-kb-itemprops-modal', model);
		},
		next: function() {
			var index = this.get('index');
			var formStates = this.get('formStates');
			if( (index+1) < formStates.length){
				index++;
				var result = this.get('formStates')[index];
				this.set('formState', result);
				this.set('index', index);
				this.set('formValueRecords', App.FormValueRecords.find(this.controllerFor('crawlrecord').get('content.id'),this.get('formState').get('state') ));
			}
		},
		back: function() {
			var index = this.get('index');
			var formStates = this.get('formStates');
			if( (index-1)>-1 ){
				index--;
				var result = this.get('formStates')[index];
				this.set('formState', result);
				this.set('index', index);
				this.set('formValueRecords', App.FormValueRecords.find(this.controllerFor('crawlrecord').get('content.id'),this.get('formState').get('state') ));
			}
		},
		select: function(formState){
			var formStates = this.get('formStates');
			var index = formStates.indexOf(formState);
			this.set('index', index);
			this.set('formState', formState);
			this.set('formValueRecords', App.FormValueRecords.find(this.controllerFor('crawlrecord').get('content.id'),this.get('formState').get('state') ));
		}
	},
	disabledBack: function () {
		var index = this.get('index');
		var result = true;
		if( index > 0 ){
			result = false;
		}
		return result;
	}.property('index'),
	disabledNext: function () {
		var index = this.get('index');
		var formStates = this.get('formStates');
		if( (index + 1) < formStates.length ){
			return false;
		}
		return true;
	}.property('index')
});

App.FormfieldvaluesItemController = Ember.ObjectController.extend({
	needs : [ 'formfieldvalues' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	selected: function(){
		var selected = this.get('controllers.formfieldvalues.index');
		var formStates = this.get('controllers.formfieldvalues.formStates');
		var formState = this.get('content');
		var index = formStates.indexOf(formState);
		if(index==selected){
			return true;
		}else{
			return false;
		}
	}.property('content', 'controllers.formfieldvalues.index')
});

App.ValidationrecordsController = Ember.Controller.extend({
	needs : [ 'crawlrecord' ],
	//itemController : 'ValidationrecordsItem',
	setupController : function(controller, model) {
		controller.set("content", model);

	},
	actions : {
		next: function() {
			var index = this.get('index');
			var validationStates = this.get('validationStates');
			if( (index+1) < validationStates.length){
				index++;
				var result = this.get('validationStates')[index];
				this.set('validationState', result);
				this.set('index', index);
				this.set('validationRecords', App.ValidationRecords.find(this.controllerFor('crawlrecord').get('content.id'),this.get('validationState').get('state') ));
			}
		},
		back: function() {
			var index = this.get('index');
			var validationStates = this.get('validationStates');
			if( (index-1)>-1 ){
				index--;
				var result = this.get('validationStates')[index];
				this.set('validationState', result);
				this.set('index', index);
				this.set('validationRecords', App.ValidationRecords.find(this.controllerFor('crawlrecord').get('content.id'),this.get('validationState').get('state') ));
			}
		},
		select: function(validationState){
			var validationStates = this.get('validationStates');
			var index = validationStates.indexOf(validationState);
			this.set('index', index);
			this.set('validationState', validationState);
			this.set('validationRecords', App.ValidationRecords.find(this.controllerFor('crawlrecord').get('content.id'),this.get('validationState').get('state') ));
		}
	},
	disabledBack: function () {
		var index = this.get('index');
		var result = true;
		if( index > 0 ){
			result = false;
		}
		return result;
	}.property('index'),
	disabledNext: function () {
		var index = this.get('index');
		var validationStates = this.get('validationStates');
		if( (index + 1) < validationStates.length ){
			return false;
		}
		return true;
	}.property('index')
});

App.ValidationrecordsItemController = Ember.ObjectController.extend({
	needs : [ 'validationrecords' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	selected: function(){
		var selected = this.get('controllers.validationrecords.index');
		var validationStates = this.get('controllers.validationrecords.validationStates');
		var validationState = this.get('content');
		var index = validationStates.indexOf(validationState);
		if(index==selected){
			return true;
		}else{
			return false;
		}
	}.property('content', 'controllers.validationrecords.index')
});

App.BrokenstatesController = Ember.Controller.extend({
	needs : [ 'crawlrecord' ],
	//itemController : 'BrokenstatesItem',
	setupController : function(controller, model) {
		controller.set("content", model);

	},
	actions : {
		next: function() {
			var index = this.get('index');
			var brokenStates = this.get('brokenStates');
			if( (index+1) < brokenStates.length){
				index++;
				var result = this.get('brokenStates')[index];
				this.set('brokenState', result);
				this.set('index', index);
			}
		},
		back: function() {
			var index = this.get('index');
			var brokenStates = this.get('brokenStates');
			if( (index-1)>-1 ){
				index--;
				var result = this.get('brokenStates')[index];
				this.set('brokenState', result);
				this.set('index', index);
			}
		},
		select: function(brokenState){
			var brokenStates = this.get('brokenStates');
			var index = brokenStates.indexOf(brokenState);
			this.set('index', index);
			this.set('brokenState', brokenState);
		}
	},
	disabledBack: function () {
		var index = this.get('index');
		var result = true;
		if( index > 0 ){
			result = false;
		}
		return result;
	}.property('index'),
	disabledNext: function () {
		var index = this.get('index');
		var brokenStates = this.get('brokenStates');
		if( (index + 1) < brokenStates.length ){
			return false;
		}
		return true;
	}.property('index')
});

App.BrokenstatesItemController = Ember.ObjectController.extend({
	needs : [ 'brokenstates' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	selected: function(){
		var selected = this.get('controllers.brokenstates.index');
		var brokenStates = this.get('controllers.brokenstates.brokenStates');
		var brokenState = this.get('content');
		var index = brokenStates.indexOf(brokenState);
		if(index==selected){
			return true;
		}else{
			return false;
		}
	}.property('content', 'controllers.brokenstates.index')
});

App.PluginOutputController = Ember.Controller.extend({
	needs : [ 'crawlrecord' ],
	url : function() {
		return "/output/crawl-records/"
				+ this.get('controllers.crawlrecord.content.id') + "/plugins/"
				+ this.get('content.key') + "/";
	}.property('content.key', 'controllers.crawlrecord.content.id')
});

App.JatytaManagementController = Ember.Controller.extend({
	needs : [ 'application' ],
	moveTo : function(route) {
		if (validateForm('config_form')) {
			var router = this.get('target');
			router.transitionTo(route);
		}
	}
});

App.JatytaManagementIndexController = Ember.Controller.extend({
	needs : [ 'application' ]

});

App.JatytaManagementKbdataController = Ember.ArrayController.extend({
	needs : [ 'application' ],
	itemController : 'JatytaManagementKbdataIndex',
	filterSchemasOld : '',
	filterSchemas : '',
	filteredSchemas : function() {
		var filter = this.get('filterSchemas');
		var filterOld = this.get('filterSchemasOld')
		if (filterOld != filter) {
			this.set('filterSchemasOld', filter);	
			var page = App.Schemas.page(1, filter);
			this.set('schemas',page);
			this.set('indexSchema', 1);
			this.set('countSchema', page.pagination.count);
			this.set('totalPagesSchema', Math.ceil( page.pagination.count/page.pagination.limit));
			return page.entities;
		}else{
			var page = this.get('schemas');
			return page.entities;
		}
	}.property('schemas', 'filterSchemas'),
	filterItemTypes : '',
	filterItemTypesOld : '',
	filteredItemTypes : function() {
		var filter = this.get('filterItemTypes');
		var filterOld = this.get('filterItemTypesOld')
		if (filterOld != filter) {
			this.set('filterItemTypesOld', filter);	
			var page = App.ItemTypes.page(1, filter, this.get('selectedSchema').id);
			this.set('itemtypes',page);
			this.set('indexItemtype', 1);
			this.set('countItemtype', page.pagination.count);
			this.set('totalPagesItemtype', Math.ceil( page.pagination.count/page.pagination.limit));
			return page.entities;
		}else{
			var page = this.get('itemtypes');
			return page.entities;
		}
	}.property('itemtypes', 'filterItemTypes'),
	filterItemProps : '',
	filterItemPropsOld : '',
	filteredItemProps : function() {
		var filter = this.get('filterItemProps');
		var filterOld = this.get('filterItemPropsOld')
		if (filterOld != filter) {
			this.set('filterItemTypesOld', filter);	
			var page = App.ItemProps.page(1, filter, this.get('selectedItemType').id);
			this.set('itemprops',page);
			this.set('indexItemprop', 1);
			this.set('countItemprop', page.pagination.count);
			this.set('totalPagesItemprop', Math.ceil( page.pagination.count/page.pagination.limit));
			return page.entities;
		}else{
			var page = this.get('itemprops');
			return page.entities;
		}
	}.property('itemprops', 'filterItemProps'),
	actions : {
		refreshItemTypes : function(schema) {
			var page = App.ItemTypes.page(1, null, schema.id);
			this.set('itemtypes',page);
			this.set('indexItemtype', 1);
			this.set('countItemtype', page.pagination.count);
			this.set('totalPagesItemtype', Math.ceil( page.pagination.count/page.pagination.limit));
			
			this.set('filterItemTypes', '');
			
			this.set('filterItemProps', '');
			this.set('itemprops', []);
			this.set('propvalues', []);
			this.set('selectedSchema', schema);
			this.set('selectedItemType', null);
			this.set('selectedItemProp', null);

		},
		refreshItemProps : function(itemtype) {
			var page = App.ItemProps.page(1, null, itemtype.id);
			this.set('itemprops',page);
			this.set('indexItemprop', 1);
			this.set('countItemprop', page.pagination.count);
			this.set('totalPagesItemprop', Math.ceil( page.pagination.count/page.pagination.limit));
			
			this.set('filterItemProps', '');
			this.set('propvalues', []);
			this.set('selectedItemType', itemtype);
			this.set('selectedItemProp', null);

		},
		refreshPropValues : function(itemprop) {
			this.set('selectedItemProp', itemprop);
			var propname = itemprop.propName;
			var propvalues = App.PropValues.findByPropName(propname.idPropName);
			propname.propvalues = propvalues;
			return this.send('openModal', 'propvalues-modal', propname);

		},
		setupNewSchemaModal : function() {
			var schema = App.Schemas.getNew();
			return this.send('openModal', 'new-schema-modal', schema);
		},
		setupNewItemTypeModal : function(schema) {
			var itemtype = App.ItemTypes.getNew();
			itemtype.idSchema = schema.idSchema;
			App.schemaNames = [ Ember.Object.create({
				value : schema.idSchema,
				name : schema.schemaName
			}) ];
			return this.send('openModal', 'new-itemtype-modal', itemtype);
		},
		setupNewItemPropModal : function(itemtype) {
			var itemprop = App.ItemProps.getNew();
			itemprop.itemType = itemtype;
			itemprop.idNativeType = 0;
			Ember.set('App.nativeTypesSelect', App.NativeTypes.selectItems());
			return this.send('openModal', 'new-itemprop-modal', itemprop);
		},
		setupNewPropValueModal : function(propName) {
			var propvalue = App.PropValues.getNew();
			propvalue.propName = propName;
			return this.send('openModal', 'new-propvalue-modal', propvalue);
		},
		editItemProp : function(itemProp) {
			itemProp.set('editable', true);
			itemProp.set('idPropName', itemProp.propName.idPropName);
			return itemProp;
		},
		cancelItemProp : function(itemProp) {
			var itemPropOld = App.PropNames.find(itemProp.idItemProp);
			itemProp.set('editable', false);
			itemProp.set('idPropName', itemPropOld.propName.idPropName);
			itemProp.set('comments', itemPropOld.comments);
			return itemProp;
		},
		newItemProp : function(list) {
			itemProp = App.ItemProps.getNew();
			itemProp.itemType = this.get('selectedItemType');
			itemProp.editable = true;
			list.addObject(itemProp);
			return list;
		},
		removeNewItemProp : function(list, itemProp) {
			list.removeObject(itemProp);
			return list;
		},
		saveItemProp : function(model) {
			if (validateForm("itemprop_form")) {
				try {
					var propName = App.PropNames.find(model.idPropName);
					model.propName = propName;
					App.ItemProps.update(model);
					this.set('itemprops', App.ItemProps
							.findByItemType(model.itemType.idItemType));
					this.get("controllers.application").displayMessage(
							"Itemprop Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		},
		nextSchema: function() {
			var index = this.get('indexSchema');
			var totalPages = this.get('totalPagesSchema')
			if( (index+1) <= totalPages){
				index++;
				var page = App.Schemas.page(index, this.get('filterSchemas'));
				this.set('indexSchema', index);
				this.set('schemas', page);
			}
		},
		backSchema: function() {
			var index = this.get('indexSchema');
			if( (index-1)>0 ){
				index--;
				var page = App.Schemas.page(index, this.get('filterSchemas'));
				this.set('indexSchema', index);
				this.set('schemas', page);	
			}
		},
		nextItemtype: function() {
			var index = this.get('indexItemtype');
			var totalPages = this.get('totalPagesItemtype')
			if( (index+1) <= totalPages){
				index++;
				var page = App.ItemTypes.page(index, this.get('filterItemTypes'), this.get('selectedSchema').id);
				this.set('indexItemtype', index);
				this.set('itemtypes', page);
			}
		},
		backItemtype: function() {
			var index = this.get('indexItemtype');
			if( (index-1)>0 ){
				index--;
				var page = App.ItemTypes.page(index, this.get('filterItemTypes'), this.get('selectedSchema').id);
				this.set('indexItemtype', index);
				this.set('itemtypes', page);	
			}
		},
		nextItemprop: function() {
			var index = this.get('indexItemprop');
			var totalPages = this.get('totalPagesItemprop')
			if( (index+1) <= totalPages){
				index++;
				var page = App.ItemProps.page(index, this.get('filterItemProps'), this.get('selectedItemType').id);
				this.set('indexItemprop', index);
				this.set('itemprops', page);
			}
		},
		backItemprop: function() {
			var index = this.get('indexItemprop');
			if( (index-1)>0 ){
				index--;
				var page = App.ItemProps.page(index, this.get('filterItemProps'),this.get('selectedItemType').id);
				this.set('indexItemprop', index);
				this.set('itemprops', page);	
			}
		}
	},
	disabledBackSchema: function () {
		var index = this.get('indexSchema');
		var result = true;
		if( index > 1 ){
			result = false;
		}
		return result;
	}.property('indexSchema'),
	disabledNextSchema: function () {
		var index = this.get('indexSchema');
		var totalPages = this.get('totalPagesSchema');
		if( index < totalPages ){
			return false;
		}
		return true;
	}.property('indexSchema'),		
	disabledBackItemType: function () {
		var index = this.get('indexItemtype');
		var result = true;
		if( index > 1 ){
			result = false;
		}
		return result;
	}.property('indexItemtype'),
	disabledNextItemType: function () {
		var index = this.get('indexItemtype');
		var totalPages = this.get('totalPagesItemtype');
		if( index < totalPages ){
			return false;
		}
		return true;
	}.property('indexItemtype'),
	disabledBackItemProp: function () {
		var index = this.get('indexItemprop');
		var result = true;
		if( index > 1 ){
			result = false;
		}
		return result;
	}.property('indexItemprop'),
	disabledNextItemProp: function () {
		var index = this.get('indexItemprop');
		var totalPages = this.get('totalPagesItemprop');
		if( index < totalPages ){
			return false;
		}
		return true;
	}.property('indexItemprop')
});

App.JatytaManagementKbdataIndexController = Ember.Controller.extend({
	needs : [ 'application' ]
});

App.JatytaManagementCrawlhistoryController = Ember.ArrayController.extend({
	needs : [ 'application' ],
	itemController : 'JatytaManagementCrawlhistoryIndex'
});

App.JatytaManagementCrawlhistoryIndexController = Ember.Controller.extend({
	needs : [ 'application' ]
});

App.JatytaManagementPropnamesController = Ember.Controller
		.extend({
			needs : [ 'application', 'propvaluesModal' ],
			filter : '',
			filterOld: '',
			filteredContent : function() {
				var filter = this.get('filter');
				var filterOld = this.get('filterOld')
				if (filterOld != filter) {
					this.set('filterOld', filter);	
					var page = App.PropNames.page(1, filter);
					this.set('content',page);
					this.set('index', 1);
					this.set('count', page.pagination.count);
					this.set('totalPages', Math.ceil( page.pagination.count/page.pagination.limit));
					return page.entities;
				}else{
					var page = this.get('content');
					return page.entities;
				}
				
			}.property('content', 'filter'),
			actions : {
				setupNewPropnameModal : function() {
					propname = App.PropNames.getNew();
					return this.send('openModal', 'new-propname-modal', propname);
				},
				newPropName : function(list) {
					propName = App.PropNames.getNew();
					propName.editable = true;
					list.addObject(propName);
					return list;
				},
				removeNew : function(list, propname) {
					list.removeObject(propname);
					return list;
				},
				save : function(propname) {
					if (validateForm("propname_form")) {
						try {
							var nativeType = App.NativeTypes
									.find(propname.idNativeType);
							propname.nativeType = nativeType;
							propname = App.PropNames.update(propname);
							this.get("controllers.application").displayMessage(
									"Propname Saved", 1);
							propname.set('editable', false);
							return propname;
						} catch (exception) {
							this.get("controllers.application").displayMessage(
									exception, -1);
						}
					}
				},
				edit : function(propname) {
					propname.set('editable', true);
					return propname;
				},
				cancel : function(propname) {
					var propnameold = App.PropNames.find(propname.idPropName);
					propname.set('editable', false);
					propname.set('name', propnameold.name);
					propname.set('nativeType', propnameold.nativeType);
					propname.set('comments', propnameold.comments);
					return propname;
				},
				setupPropValues : function(propname) {
					var propvalues = App.PropValues
							.findByPropName(propname.idPropName);
					propname.propvalues = propvalues;
					// propvaluesController.set("propvalues", propvalues);
					return this.send('openModal', 'propvalues-modal', propname);
				},
				next: function() {
					var index = this.get('index');
					var totalPages = this.get('totalPages')
					if( (index+1) <= totalPages){
						index++;
						var page = App.PropNames.page(index, this.get('filter'));
						this.set('index', index);
						this.set('content', page);
					}
				},
				back: function() {
					var index = this.get('index');
					var validationStates = this.get('validationStates');
					if( (index-1)>0 ){
						index--;
						var page = App.PropNames.page(index, this.get('filter'));
						this.set('index', index);
						this.set('content', page);	
					}
				}
			},
			disabledBack: function () {
				var index = this.get('index');
				var result = true;
				if( index > 1 ){
					result = false;
				}
				return result;
			}.property('index'),
			disabledNext: function () {
				var index = this.get('index');
				var totalPages = this.get('totalPages');
				if( index < totalPages ){
					return false;
				}
				return true;
			}.property('index')
		});

App.DeletePropnameModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementPropnames', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		cancel : function() {
			return this.send('closeModal');
		},
		remove : function(propname) {
			try {
				App.PropNames.remove(propname);
				this.get("controllers.jatytaManagementPropnames").set('filter', '');
				this.get("controllers.application").displayMessage(
						"Propname Deleted", 1);
				return this.send('closeModal');
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		}
	}
});

App.PropvaluesModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementPropnames', 'application', 'propvaluesModal' ],
	setupController : function(controller, model) {
		controller.set("content", model);
		/*controller.set("date", null);
		controller.set("dateDisplay", "");
		controller.set("valid", false);*/
		controller.set("propvalues", App.PropValues
				.findByPropName(model.idPropName));
	},
	actions : {
		close : function() {
			return this.send('closeModal');
		},
		removeNew : function(propvalue, propname) {
			try {
				propname.propvalues.removeObject(propvalue);
				return propname;
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		},
		remove : function(propvalue, propname) {
			try {
				propname.propvalues.removeObject(propvalue);
				App.PropValues.remove(propvalue);
				this.get("controllers.application").displayMessage(
						"Propvalue Deleted", 1);
				return propname;
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		},
		save : function(propvalue) {
			try {
				if(propvalue.propName.nativeType.typeName == App.booleanType){
					if(propvalue.booleanValue == true){
						propvalue.value = 'true';
					}else{
						propvalue.value = 'false';
					}	
				}else if (propvalue.propName.nativeType.typeName == App.dateType){
					//propvalue.value = this.get('dateDisplay');
				}
				var propname = App.PropNames.find(propvalue.idPropName);
				propvalue.propName = propname;
				App.PropValues.update(propvalue);
				this.get("controllers.application").displayMessage(
						"Propvalue Saved", 1);
				propvalue.set('editable', false);
				return propvalue;
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		},
		add : function(propname) {
			var propValue = App.PropValues.getNew();
			propValue.editable = true;
			propValue.idPropName = propname.idPropName;
			propValue.propName = propname;
			
			if(propname.nativeType.typeName == App.booleanType){
				propValue.value = false;	
			}
			
			propname.propvalues.addObject(propValue);
			return propname;
		},
		edit : function(propvalue) {
			propvalue.idPropName = propvalue.propName.idPropName;
			Ember.set(propvalue, 'editable', true);
			if(propvalue.propName.nativeType.typeName == App.booleanType){
				if(propvalue.value == 'false'){
					propvalue.booleanValue = false;
				}else{
					propvalue.booleanValue = true;
				}	
			}else if (propvalue.propName.nativeType.typeName == App.dateType){
				/*var date;
			    date = moment(propvalue.get("value"));
			    this.set("date",date.toDate());
			    this.set("valid",true);
			    this.set("dateDisplay",date.format("MMM Do YYYY"));*/
			}
			return propvalue;
		},
		cancel : function(propvalue) {
			var propvalueold = App.PropValues.find(propvalue.idPropValues);
			propvalue.set('editable', false);
			propvalue.set('value', propvalueold.value);
			propvalue.set('isValid', propvalueold.isValid);
			return propvalue;
		}
	},
	isBoolean : function() {
		propName = this.get('content');
		if (propName.nativeType.typeName == App.booleanType) {
			return true;
		}
		return false;
	}.property('content'),
	isNumber : function() {
		propName = this.get('content');
		if (propName.nativeType.typeName == App.numberType) {
			return true;
		}
		return false;
	}.property('content'),
	isDate : function() {
		propName = this.get('content');
		if (propName.nativeType.typeName == App.dateType) {
			return true;
		}
		return false;
	}.property('content'),
	isText : function() {
		propName = this.get('content');
		if (propName.nativeType.typeName == App.textType) {
			return true;
		}
		return false;
	}.property('content')
});

// Modal Controllers
App.NewSchemaModalController = Ember.ObjectController.extend({
	needs : [ 'application', 'jatytaManagementKbdata' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		save : function(model) {
			if (validateForm("modal_form")) {
				App.Schemas.update(model);
				this.get('controllers.jatytaManagementKbdata').set('schemas',
						App.Schemas.findAll());
				this.get("controllers.application").displayMessage(
						"Schema Saved", 1);
				return this.send('closeModal');
			}
		}
	}
});

App.EditSchemaModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		refresh : function(schema) {
			this.get('controllers.jatytaManagementKbdata').set('schemas',
					App.Schemas.findAll());
			return this.send('closeModal');
		},
		update : function(schema) {
			if (validateForm("modal_form")) {
				App.Schemas.update(schema);
				this.get("controllers.application").displayMessage(
						"Schema Updated", 1);
				return this.send('closeModal');
			}
		}
	}
});

App.DeleteSchemaModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		cancel : function(schema) {
			return this.send('closeModal');
		},
		remove : function(schema) {
			try {
				App.Schemas.remove(schema);
				this.get('controllers.jatytaManagementKbdata').set('schemas',
						App.Schemas.findAll());
				this.get('controllers.jatytaManagementKbdata').set('itemtypes',
						[]);
				this.get('controllers.jatytaManagementKbdata').set('itemprops',
						[]);
				this.get('controllers.jatytaManagementKbdata').set(
						'propvalues', []);
				this.get("controllers.application").displayMessage(
						"Schema Deleted", 1);
				return this.send('closeModal');
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		}
	}
});

App.NewItemtypeModalController = Ember.ObjectController.extend({
	needs : [ 'application', 'jatytaManagementKbdata' ],
	setupController : function(controller, model) {
		controller.set("content", model);

	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		save : function(model) {
			if (validateForm("modal_form")) {
				var schema = App.Schemas.find(model.idSchema);
				model.schema = schema;
				App.ItemTypes.update(model);
				this.get('controllers.jatytaManagementKbdata').set('itemtypes',
						App.ItemTypes.findBySchema(model.idSchema));
				this.get("controllers.application").displayMessage(
						"Itemtype Saved", 1);
				return this.send('closeModal');
			}
		}
	}
});

App.EditItemtypeModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		refresh : function(itemtype) {
			this.get('controllers.jatytaManagementKbdata').set('itemtypes',
					App.ItemTypes.findBySchema(itemtype.schema.idSchema));
			return this.send('closeModal');
		},
		update : function(itemtype) {
			if (validateForm("modal_form")) {
				App.ItemTypes.update(itemtype);
				this.get("controllers.application").displayMessage(
						"ItemType Updated", 1);
				return this.send('closeModal');
			}
		}
	}
});

App.DeleteItemtypeModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		remove : function(itemtype) {
			try {
				App.ItemTypes.remove(itemtype);
				this.get('controllers.jatytaManagementKbdata').set('itemtypes',
						App.ItemTypes.findBySchema(itemtype.schema.idSchema));
				this.get('controllers.jatytaManagementKbdata').set('itemprops',
						[]);
				this.get('controllers.jatytaManagementKbdata').set(
						'propvalues', []);
				this.get("controllers.application").displayMessage(
						"Itemtype Deleted", 1);
				return this.send('closeModal');
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		}
	}
});

App.NewItempropModalController = Ember.ObjectController
		.extend({
			needs : [ 'application', 'jatytaManagementKbdata' ],
			setupController : function(controller, model) {
				controller.set("content", model);
			},
			actions : {
				cancel : function(model) {
					return this.send('closeModal');
				},
				save : function(model) {
					if (validateForm("modal_form")) {
						try {
							var propName = App.PropNames.find(model.idPropName);
							model.propName = propName;
							App.ItemProps.update(model);
							this
									.get('controllers.jatytaManagementKbdata')
									.set(
											'itemprops',
											App.ItemProps
													.findByItemType(model.itemType.idItemType));
							this.get("controllers.application").displayMessage(
									"Itemprop Saved", 1);
							return this.send('closeModal');
						} catch (exception) {
							this.get("controllers.application").displayMessage(
									exception, -1);
						}

					}
				}
			}
		});

App.EditItempropModalController = Ember.ObjectController
		.extend({
			needs : [ 'jatytaManagementKbdata', 'application' ],
			setupController : function(controller, model) {
				controller.set("content", model);
			},
			actions : {
				refresh : function(itemprop) {
					this
							.get('controllers.jatytaManagementKbdata')
							.set(
									'itemprops',
									App.ItemProps
											.findByItemType(itemprop.itemType.idItemType));
					return this.send('closeModal');
				},
				update : function(itemprop) {
					if (validateForm("modal_form")) {
						App.ItemProps.update(itemprop);
						this.get("controllers.application").displayMessage(
								"ItemProp Updated", 1);
						return this.send('closeModal');
					}
				}
			}
		});

App.DeleteItempropModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		remove : function(itemprop) {
			try {
				App.ItemProps.remove(itemprop);
				this.get('controllers.jatytaManagementKbdata').set(
						'itemprops',
						App.ItemProps
								.findByItemType(itemprop.itemType.idItemType));
				this.get('controllers.jatytaManagementKbdata').set(
						'propvalues', []);
				this.get("controllers.application").displayMessage(
						"Itemprop Deleted", 1);
				return this.send('closeModal');
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
		}
	}
});

App.NewPropnameModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementPropnames', 'application' ],
	setupController : function(controller, model) {
		controller.set('content', model);
	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		save : function(model) {
			if (validateForm("modal_form")) {
				try {
					var nativeType = App.NativeTypes
					.find(model.idNativeType);
					model.nativeType = nativeType;
					App.PropNames.update(model);
					this.get("controllers.application").displayMessage(
						"PropName Saved", 1);
					return this.send('closeModal');
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
			}
		}
	}
});

App.NewPropvalueModalController = Ember.ObjectController
		.extend({
			needs : [ 'application', 'jatytaManagementKbdata' ],
			setupController : function(controller, model) {
				controller.set("content", model);

			},
			actions : {
				cancel : function(model) {
					return this.send('closeModal');
				},
				save : function(model) {
					if (validateForm("modal_form")) {
						try {
							App.PropValues.update(model);
							this
									.get('controllers.jatytaManagementKbdata')
									.set(
											'propvalues',
											App.PropValues
													.findByItemProp(model.itemProp.idItemProp));
							this.get("controllers.application").displayMessage(
									"Propvalue Saved", 1);
							return this.send('closeModal');
						} catch (exception) {
							this.get("controllers.application").displayMessage(
									exception, -1);
						}

					}
				}
			}
		});

App.EditPropvalueModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		refresh : function(propvalue) {
			this.get('controllers.jatytaManagementKbdata').set(
					'propvalues',
					App.PropValues
							.findByItemProp(propvalue.itemProp.idItemProp));
			return this.send('closeModal');
		},
		update : function(propvalue) {
			if (validateForm("modal_form")) {
				App.PropValues.update(propvalue);
				this.get("controllers.application").displayMessage(
						"PropValue Updated", 1);
				return this.send('closeModal');
			}
		}
	}
});

App.DeletePropvalueModalController = Ember.ObjectController
		.extend({
			needs : [ 'jatytaManagementKbdata', 'application' ],
			setupController : function(controller, model) {
				controller.set("content", model);
			},
			actions : {
				cancel : function(model) {
					return this.send('closeModal');
				},
				remove : function(propvalue) {
					try {
						App.PropValues.remove(propvalue);
						this
								.get('controllers.jatytaManagementKbdata')
								.set(
										'propvalues',
										App.PropValues
												.findByItemProp(propvalue.itemProp.idItemProp));
						this.get("controllers.application").displayMessage(
								"Itemprop Deleted", 1);
						return this.send('closeModal');
					} catch (exception) {
						this.get("controllers.application").displayMessage(
								exception, -1);
					}
				}
			}
		});

App.EditNativetypeModalController = Ember.ObjectController.extend({
	needs : [ 'jatytaManagementKbdata', 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		refresh : function(nativetype) {
			this.get('controllers.jatytaManagementKbdata').set('nativetypes',
					App.NativeTypes.findAll());
			return this.send('closeModal');
		},
		update : function(nativetype) {
			if (validateForm("modal_form")) {
				App.NativeTypes.update(nativetype);
				this.get("controllers.application").displayMessage(
						"NativeType Updated", 1);
				return this.send('closeModal');
			}
		}
	}
});

App.ViewStateModalController = Ember.ObjectController.extend({
	needs : [ 'crawlrecord' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	url : function() {
		var result = "/output/crawl-records/"
			+ this.get('controllers.crawlrecord.content.id')
			+ "/plugins/0/states/";
		if (this.get('content') == "0"){
			result = result + "index.html";
		} else {
			result = result + "state" + this.get('content') + ".html";
		}
		return result; 
	}.property('content.key', 'controllers.crawlrecord.content.id')
});

App.AddKbItempropsModalController = Ember.ObjectController.extend({
	needs : [ 'application' ],
	setupController : function(controller, model) {
		controller.set("content", model);

	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		save : function(model) {
			if (validateForm("list_form")) {
				var details = model.get('details');
				// save details

				var propvalueList = [];
				var propnameList = [];
				// var itempropList = [];
				for (var i = 0, l = details.length; i < l; i++) {
					var detail = details[i];
					// create propName
					var propName = App.PropNames.getNew();
					propName.name = detail.propName;
					propName.comments = detail.comments;
					propName.nativeType = App.NativeTypes
							.find(detail.idNativeType);

					// add propname to list
					propnameList.addObject(propName);

					// create propvalue
					var propvalue = App.PropValues.getNew();
					propvalue.value = detail.value;
					propvalue.isValid = detail.valid;
					propvalue.propName = propName;

					// add propvalue to list
					propvalueList.addObject(propvalue);
					// propvalue = App.PropValues.update(propvalue);

					/*
					 * var itemprop.itemType =
					 * App.ItemTypes.find(detail.idItemType); itemprop.propName =
					 * propName; itemprop.comments = detail.comments; //add
					 * itemprop to list itempropList.addObject(itemprop);
					 * //itemprop = App.ItemProps.update(itemprop);
					 */

				}
				try {
					App.PropValues.savePropNamesAndPropValues(propvalueList);
					this.get("controllers.application").displayMessage(
							"Propnames and Values Saved", 1);
				} catch (exception) {
					this.get("controllers.application").displayMessage(
							exception, -1);
				}
				return this.send('closeModal');
			}
		},
		removeDetail : function(model, detail) {
			model.removeObject(detail);
		},
		newItemProp : function(model){
			var newObj = Ember.Object.create({
					idSchema : null,
					idItemType : null,
					propName : null,
					value : null,
					idNativeType : null,
					comments : null,
					valid : null
				});
			model.addObject(newObj);
		}
	}
});

App.DeleteValidationModalController = Ember.ObjectController.extend({
	needs : [ 'application', 'configurationValidation' ],
	setupController : function(controller, model) {
		controller.set("content", model);

	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		remove : function(model) {
			try {
				this.get("controllers.configurationValidation").get(
						'validations').removeObject(model);
				if (model.id != null) {
					App.ValidationConfigurations.remove(model);
					this.get("controllers.application").displayMessage(
							"Validation Configuration Deleted", 1);
				} else {
					this.get("controllers.application").displayMessage(
							"Validation Configuration Removed", 1);
				}
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
			return this.send('closeModal');
		}
	}
});

App.DeleteBrokenlinkModalController = Ember.ObjectController.extend({
	needs : [ 'application', 'configurationBrokenlinks' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	},
	actions : {
		cancel : function(model) {
			return this.send('closeModal');
		},
		remove : function(model) {
			try {
				if (model.id != null) {
					App.BrokenLinksConfigurations.remove(model);
					this.get("controllers.application").displayMessage(
							"Broken link pattern Deleted", 1);
				} else {
					this.get("controllers.application").displayMessage(
							"Broken link pattern Removed", 1);
				}
				this.get("controllers.configurationBrokenlinks").get(
						'brokenLinks').removeObject(model);
			} catch (exception) {
				this.get("controllers.application").displayMessage(exception,
						-1);
			}
			return this.send('closeModal');
		}
	}
});

App.StatenamesController = Ember.ObjectController.extend({
	needs : [ 'crawlrecord' ],
	filter : '',
	filterOld: '',
	filteredContent : function() {
		var cr_controller = this.controllerFor('crawlrecord');
		var idcrawlrecord = cr_controller.get('content.id');
		var filter = this.get('filter');
		var filterOld = this.get('filterOld')
		if (filterOld != filter) {
			this.set('filterOld', filter);	
			var page = App.StateNamesRecords.page(idcrawlrecord,1, filter);
			this.set('content',page);
			this.set('index', 1);
			this.set('count', page.pagination.count);
			this.set('totalPages', Math.ceil( page.pagination.count/page.pagination.limit));
			return page.entities;
		}else{
			var page = this.get('content');
			return page.entities;
		}
		
	}.property('content', 'filter'),

	actions: { 				
		 next: function() {
			 var idcrawlrecord = this.get('idCrawlRecord');
			var index = this.get('index');
			var totalPages = this.get('totalPages')
			if( (index+1) <= totalPages){
				index++;
				var page = App.StateNamesRecords.page(idcrawlrecord ,index, this.get('filter'));
				this.set('index', index);
				this.set('content', page);
			}
		},
		back: function() {
			var idcrawlrecord = this.get('idCrawlRecord');
			var index = this.get('index');
			if( (index-1)>0 ){
				index--;
				var page = App.StateNamesRecords.page(idcrawlrecord, index, this.get('filter'));
				this.set('index', index);
				this.set('content', page);	
			}
		}
	},
	disabledBack: function () {
		var index = this.get('index');
		var result = true;
		if( index > 1 ){
			result = false;
		}
		return result;
	}.property('index'),
	disabledNext: function () {
		var index = this.get('index');
		var totalPages = this.get('totalPages');
		if( index < totalPages ){
			return false;
		}
		return true;
	}.property('index')	
});

App.AboutModalController = Ember.ObjectController.extend({
	needs : [ 'crawlrecord' ],
	setupController : function(controller, model) {
		controller.set("content", model);
	}
});