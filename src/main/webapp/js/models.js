App.Link = Ember.Object.extend({
	text : null,
	icon : null,
	target : null,
	action : false
});

App.browsers = [ Ember.Object.create({
	name : "Mozilla Firefox",
	value : "FIREFOX"
}), Ember.Object.create({
	name : "Google Chrome",
	value : "CHROME"
}), Ember.Object.create({
	name : "Microsoft Internet Explorer",
	value : "INTERNET_EXPLORER"
}) ];

App.validationElements = [ Ember.Object.create({
	name : "div",
	value : "DIV"
}), Ember.Object.create({
	name : "span",
	value : "SPAN"
}) ];

App.validationAttributeFilters = [ Ember.Object.create({
	name : "class",
	value : "CLASS"
}) ];

App.proxyTypes = [ Ember.Object.create({
	name : "Nothing",
	value : "NOTHING"
}), Ember.Object.create({
	name : "Manual",
	value : "MANUAL"
}), Ember.Object.create({
	name : "Automatic",
	value : "AUTOMATIC"
}), Ember.Object.create({
	name : "System Default",
	value : "SYSTEM_DEFAULT"
}) ];

App.sourceTypes = [ Ember.Object.create({
	name : "Text File",
	value : "file"
}), Ember.Object.create({
	name : "Xml File",
	value : "xml"
}), Ember.Object.create({
	name : "Data Base",
	value : "db"
}) ];

App.formValueTypes = [ Ember.Object.create({
	name : "Valid",
	value : "VALID"
}), Ember.Object.create({
	name : "Invalid",
	value : "INVALID"
}), Ember.Object.create({
	name : "Both",
	value : "BOTH"
}) ];

App.schemaNameALL = Ember.Object.create({
	value : 0,
	name : "All Schemas"
});

App.nativeTypesSelect = [];
App.propNamesSelect = [];

App.crawlStatusTypeSelect = ["all", "idle", "queued", "initializing", 
                             "running", "success", "failure"];

App.datePatternSelect = ["yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd", 
                             "dd/MM/yyyy","dd/MM/yyyy HH:mm", "dd-MM-yyyy", "dd-MM-yyyy HH:mm"];


App.clickType = [ Ember.Object.create({
	name : "Click",
	value : "click"
}), Ember.Object.create({
	name : "Don't Click",
	value : "noClick"
}) ];

App.tags = [ "a", "abbr", "address", "area", "article", "aside", "audio",
		"button", "canvas", "details", "div", "figure", "footer", "form",
		"header", "img", "input", "label", "li", "nav", "ol", "section",
		"select", "span", "summary", "table", "td", "textarea", "th", "tr",
		"ul", "video" ];

App.clickConditions = [ Ember.Object.create({
	name : "With Attribute (name=value):",
	value : "wAttribute"
}), Ember.Object.create({
	name : "With Text:",
	value : "wText"
}), Ember.Object.create({
	name : "Under XPath:",
	value : "wXPath"
}), Ember.Object.create({
	name : "When URL contains:",
	value : "url"
}), Ember.Object.create({
	name : "When URL does not contain:",
	value : "notUrl"
}), Ember.Object.create({
	name : "When Regex:",
	value : "regex"
}), Ember.Object.create({
	name : "When not Regex:",
	value : "notRegex"
}), Ember.Object.create({
	name : "When XPath:",
	value : "xPath"
}), Ember.Object.create({
	name : "When not XPath:",
	value : "notXPath"
}), Ember.Object.create({
	name : "When element is visible with id:",
	value : "visibleId"
}), Ember.Object.create({
	name : "When element is not visible with id:",
	value : "notVisibleId"
}), Ember.Object.create({
	name : "When element is visible with text:",
	value : "visibleText"
}), Ember.Object.create({
	name : "When element is not visible with text:",
	value : "notVisibleText"
}), Ember.Object.create({
	name : "When element is visible with tag:",
	value : "visibleTag"
}), Ember.Object.create({
	name : "When element is not visible with tag:",
	value : "notVisibleTag"
}), Ember.Object.create({
	name : "When Javascript is true:",
	value : "javascript"
}) ];

App.pageConditions = [ Ember.Object.create({
	name : "When URL contains:",
	value : "url"
}), Ember.Object.create({
	name : "When URL does not contain:",
	value : "notUrl"
}), Ember.Object.create({
	name : "When Regex:",
	value : "regex"
}), Ember.Object.create({
	name : "When not Regex:",
	value : "notRegex"
}), Ember.Object.create({
	name : "When XPath:",
	value : "xPath"
}), Ember.Object.create({
	name : "When not XPath:",
	value : "notXPath"
}), Ember.Object.create({
	name : "When element is visible with id:",
	value : "visibleId"
}), Ember.Object.create({
	name : "When element is not visible with id:",
	value : "notVisibleId"
}), Ember.Object.create({
	name : "When element is visible with text:",
	value : "visibleText"
}), Ember.Object.create({
	name : "When element is not visible with text:",
	value : "notVisibleText"
}), Ember.Object.create({
	name : "When element is visible with tag:",
	value : "visibleTag"
}), Ember.Object.create({
	name : "When element is not visible with tag:",
	value : "notVisibleTag"
}), Ember.Object.create({
	name : "When Javascript is true:",
	value : "javascript"
}) ];

App.comparators = [ Ember.Object.create({
	name : "Ignore Attribute:",
	value : "attribute"
}), Ember.Object.create({
	name : "Ignore White Space",
	value : "simple"
}), Ember.Object.create({
	name : "Ignore Dates",
	value : "date"
}), Ember.Object.create({
	name : "Ignore Scripts",
	value : "script"
}), Ember.Object.create({
	name : "Only observe plain DOM structure",
	value : "plain"
}), Ember.Object.create({
	name : "Ignore Regex:",
	value : "regex"
}), Ember.Object.create({
	name : "Ignore XPath:",
	value : "xPath"
}), Ember.Object.create({
	name : "Ignore within Distance Edit Threshold:",
	value : "distance"
}) ];

App.ajaxStatusCode = Ember.Object.create({
	500 : function() {
		throw "Error in Server, check the log for details.";
	}
});

App.booleanType = 'boolean';
App.numberType = 'number';
App.dateType = 'date';
App.textType = 'text';
App.datetimeType = 'datetime';
App.timeType = 'time';

App.imageSource = 'data:image/jpg;base64,';


// Configuration Model
App.Configurations = Ember.Object.extend();
App.Configurations.reopenClass({
	findAll : function() {
		var configurations = [];
		$.ajax({
			url : '/rest/configurations',
			async : false,
			dataType : 'json',
			context : configurations,
			success : function(response) {
				response.forEach(function(configuration) {
					this.addObject(App.Configurations.create(configuration))
				}, this);
			}
		});
		return configurations;
	},
	find : function(id) {
		var configuration = App.Configurations.create({
			id : id
		});
		$.ajax({
			url : '/rest/configurations/' + id,
			async : false,
			dataType : 'json',
			context : configuration,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return configuration;
	},
	getNew : function(id) {
		var config = App.Configurations.create({});
		$.ajax({
			url : '/rest/configurations/new' + (id ? '/' + id : ""),
			async : false,
			dataType : 'json',
			context : config,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return config;
	},
	add : function(config, callback) {
		$.ajax({
			url : '/rest/configurations',
			async : false,
			type : 'POST',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			context : config,
			success : function(response) {
				this.setProperties(response);
				if (callback !== undefined)
					callback(this);
			}
		});
		return config;
	},
	update : function(config) {
		$.ajax({
			url : '/rest/configurations/' + config.id,
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			context : config,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return config;
	},
	remove : function(config, callback) {
		$.ajax({
			url : '/rest/configurations/' + config.id,
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			context : config,
			success : function(response) {
				if (callback !== undefined)
					callback(config);
			}
		});
	},
	cleanJSON : function(key, value) {
		return value;
	}
});

App.Plugins = Ember.Object.extend();
App.Plugins.reopenClass({
	findAll : function() {
		var plugins = [];
		$.ajax({
			url : '/rest/plugins',
			async : false,
			dataType : 'json',
			context : plugins,
			success : function(response) {
				response.forEach(function(plugin) {
					this.addObject(App.Plugins.create(plugin));
				}, this);
			}
		});
		return plugins;
	},
	refresh : function(callback) {
		var plugins = [];
		$.ajax({
			url : '/rest/plugins',
			async : true,
			type : 'PUT',
			dataType : 'json',
			context : plugins,
			success : function(response) {
				response.forEach(function(plugin) {
					this.addObject(App.Plugins.create(plugin));
				}, this);
				if (callback !== undefined)
					callback(plugins);
			}
		});
	},
	find : function(id) {
		var plugin = App.Plugins.create({
			id : id
		});
		$.ajax({
			url : '/rest/plugins/' + id,
			async : false,
			dataType : 'json',
			context : plugin,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return plugin;
	},
	selectItems : function() {
		selectItems = App.Plugins.findAll();
		for (var i = 0, l = selectItems.length; i < l; i++) {
			selectItems[i] = Ember.Object.create({
				name : selectItems[i].id,
				value : selectItems[i].id
			});
		}
		selectItems.splice(0, 0, Ember.Object.create({
			name : "Select Plugin",
			value : ""
		}));
		return selectItems;
	},
	add : function(fileName, data, url, callback) {
		var fd = new FormData();
		fd.append("name", fileName);
		if (data) {
			fd.append("file", data);
		} else if (url) {
			fd.append("url", url);
		}
		$.ajax({
			url : '/rest/plugins',
			async : true,
			type : 'POST',
			data : fd,
			processData : false,
			contentType : false,
			success : function(response) {
				if (callback !== undefined)
					callback();
			}
		});
	},
	remove : function(plugin, callback) {
		$.ajax({
			url : '/rest/plugins/' + plugin.id,
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(plugin, this.cleanJSON),
			dataType : 'json',
			context : plugin,
			success : function(response) {
				if (callback !== undefined)
					callback(plugin);
			}
		});
	}
});

App.CrawlRecords = Ember.Object.extend();
App.CrawlRecords.reopenClass({
	findAll : function(configId, active) {
		var records = [];
		var data = '';
		if (configId !== undefined)
			data = {
				config : configId
			};
		if (active)
			data = {
				active : true
			};
		$.ajax({
			url : '/rest/history',
			async : false,
			dataType : 'json',
			context : records,
			data : data,
			success : function(response) {
				response.forEach(function(record) {
					var pluginArray = [];
					pluginArray.push({
						key : "0",
						name : "Crawl Overview"
					});
					for (key in record.plugins) {
						record.plugins[key].key = key;
						pluginArray.push(record.plugins[key]);
					}
					record.plugins = pluginArray;
					this.addObject(App.CrawlRecords.create(record))
				}, this);
			}
		});
		return records;
	},
	find : function(id) {
		var record = App.CrawlRecords.create({
			id : id
		});
		$.ajax({
			url : '/rest/history/' + id,
			async : false,
			dataType : 'json',
			context : record,
			success : function(response) {
				var pluginArray = [];
				pluginArray.push({
					key : "0",
					name : "Crawl Overview"
				});
				for (key in response.plugins) {
					response.plugins[key].key = key;
					pluginArray.push(response.plugins[key]);
				}
				response.plugins = pluginArray;
				this.setProperties(response);
			}
		});
		return record;
	},
	add : function(configId, callback) {
		var record = App.CrawlRecords.create({
			configurationId : configId
		});
		$.ajax({
			url : '/rest/history',
			async : false,
			type : 'POST',
			contentType : "application/json;",
			data : configId,
			dataType : 'json',
			context : record,
			success : function(response) {
				var pluginArray = [];
				pluginArray.push({
					key : "0",
					name : "Crawl Overview"
				});
				for (key in response.plugins) {
					response.plugins[key].key = key;
					pluginArray.push(response.plugins[key]);
				}
				response.plugins = pluginArray;
				this.setProperties(response);
				if (callback !== undefined)
					callback(response);
			}
		});
		return record;
	}
});

App.CrawlConfigurations = Ember.Object.extend();
App.CrawlConfigurations.reopenClass({
	getNew : function(configId) {
		var configuration = App.CrawlConfigurations.create({});
		data = {
			configId : configId	
		};
		$.ajax({
			url : '/rest/jatyta/config/newconfiguration',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : configuration,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return configuration;
	},
	find : function(configId) {
		var configuration = App.CrawlConfigurations.create({});
		data = {
			configId : configId
		};
		$.ajax({
			url : '/rest/jatyta/config/configurationbyconfigid',
			async : false,
			type : 'GET',
			dataType : 'json',
			context : configuration,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return configuration;
	},	
	estimateTime : function(configId, maxValues) {
		var estimatedTime = Ember.Object.create({
			time : "0"});
		data = {
			id : configId,
			maxvalues : maxValues
		};
		$.ajax({
			url : '/rest/jatyta/config/estimatedtime',
			async : false,
			type : 'GET',
			dataType : 'text',
			context : estimatedTime,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.time = response;
			}
		});
		return estimatedTime.time;
	},	
	update : function(configuration) {
		$.ajax({
			url : '/rest/jatyta/config/updateconfiguration',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(configuration, this.cleanJSON),
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : configuration,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return configuration;
	},
	
});

App.HtmlElements = Ember.Object.extend();
App.HtmlElements.reopenClass({
	findAll : function() {
		var elements = [];
		$.ajax({
			url : '/rest/jatyta/config/htmlelements',
			async : false,
			dataType : 'json',
			context : elements,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(element) {
					this.addObject(Ember.Object.create({
						value : element,
						name : element
					}))
				}, this);
			}
		});
		return elements;
	}
});

App.ValidationConfigurations = Ember.Object.extend();
App.ValidationConfigurations.reopenClass({
	getNew : function() {
		var validation = App.ValidationConfigurations.create({});
		$.ajax({
			url : '/rest/jatyta/config/newvalidation',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : validation,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return validation;
	},
	update : function(validation) {
		$.ajax({
			url : '/rest/jatyta/config/updatevalidation',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(validation, this.cleanJSON),
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : validation,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return validation;
	},
	remove : function(validation) {
		$.ajax({
			url : '/rest/jatyta/config/deletevalidation',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(validation, this.cleanJSON),
			dataType : 'json',
			context : validation,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	},
	findByConfigId : function(id) {
		var validations = [];
		data = {
			configId : id
		};
		$.ajax({
			url : '/rest/jatyta/config/validationbyconfigid',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : validations,
			success : function(response) {
				response.forEach(function(validation) {
					this.addObject(App.ValidationConfigurations.create(validation))
				}, this);
			}
		});
		return validations;
	},
});


App.BrokenLinksConfigurations = Ember.Object.extend();
App.BrokenLinksConfigurations.reopenClass({
	getNew : function() {
		var config = App.BrokenLinksConfigurations.create({});
		$.ajax({
			url : '/rest/jatyta/config/newbrokenlinksconfig',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : config,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return config;
	},
	getDefaults : function(id) {
		var configurations = [];
		data = {
			configId : id
		};
		$.ajax({
			url : '/rest/jatyta/config/brokenlinksbydefault',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : configurations,
			success : function(response) {
				response.forEach(function(config) {
					this.addObject(App.BrokenLinksConfigurations.create(config))
				}, this);
			}
		});
		return configurations;
	},
	update : function(config) {
		$.ajax({
			url : '/rest/jatyta/config/updatebrokenlinksconfig',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : config,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return config;
	},
	remove : function(config) {
		$.ajax({
			url : '/rest/jatyta/config/deletebrokenlinksconfig',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			context : config,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	},
	findByConfigId : function(id) {
		var configurations = [];
		data = {
			configId : id
		};
		$.ajax({
			url : '/rest/jatyta/config/brokenlinksconfigbyconfigid',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : configurations,
			success : function(response) {
				response.forEach(function(config) {
					this.addObject(App.BrokenLinksConfigurations.create(config))
				}, this);
			}
		});
		return configurations;
	},
});

App.FormConfigurations = Ember.Object.extend();
App.FormConfigurations.reopenClass({
	getNew : function() {
		var config = App.FormConfigurations.create({});
		$.ajax({
			url : '/rest/jatyta/config/newformconfig',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : config,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return config;
	},
	getDefaults : function(id) {
		var configurations = [];
		data = {
			configId : id
		};
		$.ajax({
			url : '/rest/jatyta/config/formbydefault',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : configurations,
			success : function(response) {
				response.forEach(function(config) {
					this.addObject(App.FormConfigurations.create(config))
				}, this);
			}
		});
		return configurations;
	},
	update : function(config) {
		$.ajax({
			url : '/rest/jatyta/config/updateformconfig',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : config,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return config;
	},
	remove : function(config) {
		$.ajax({
			url : '/rest/jatyta/config/deleteformconfig',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(config, this.cleanJSON),
			dataType : 'json',
			context : config,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	},
	findByConfigId : function(id) {
		var configurations = [];
		data = {
			configId : id
		};
		$.ajax({
			url : '/rest/jatyta/config/formconfigbyconfigid',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : configurations,
			success : function(response) {
				response.forEach(function(config) {
					this.addObject(App.FormConfigurations.create(config))
				}, this);
			}
		});
		return configurations;
	},
});


App.Schemas = Ember.Object.extend();
App.Schemas.reopenClass({
	selectItems : function() {
		var schemasToADD = [];
		App.Schemas.findAll().forEach(function(schema) {
			this.addObject(Ember.Object.create({
				value : schema.idSchema,
				name : schema.schemaName
			}))
		}, schemasToADD);
		return schemasToADD;
	},
	findAll : function() {
		var schemas = [];
		$.ajax({
			url : '/rest/jatyta/kb/schemas',
			async : false,
			dataType : 'json',
			context : schemas,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(schema) {
					this.addObject(App.Schemas.create(schema))
				}, this);
			}
		});
		return schemas;
	},
	page : function(number,search) {
		var page = {
			entities : [],
			pagination : null,
			sorting: null	
		}; 
		data = {
			search : search
		};
		$.ajax({
			url : '/rest/jatyta/kb/schemas/'+number,
			async : false,
			dataType : 'json',
			context : page,
			data: data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.entities.forEach(function(schema) {
					this.entities.addObject(App.Schemas.create(schema));
				}, this);
				this.pagination = response.pagination;
				this.sorting = response.sorting;
			}
		});
		return page;
	},
	find : function(id) {
		var schema = App.Schemas.create({});
		data = {
			id : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/schema',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : schema,
			data : data,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return schema;
	},
	getNew : function() {
		var schema = App.Schemas.create({});
		$.ajax({
			url : '/rest/jatyta/kb/newschema',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : schema,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return schema;
	},
	update : function(schema) {
		$.ajax({
			url : '/rest/jatyta/kb/updateschema',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(schema, this.cleanJSON),
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : schema,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return schema;
	},
	remove : function(schema) {
		$.ajax({
			url : '/rest/jatyta/kb/deleteschema',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(schema, this.cleanJSON),
			dataType : 'json',
			context : schema,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	}
});

App.ItemTypes = Ember.Object.extend();
App.ItemTypes.reopenClass({
	selectItems : function() {
		var select = [];
		App.ItemTypes.findAll().forEach(function(itemType) {
			this.addObject(Ember.Object.create({
				value : itemType.idItemType,
				name : itemType.schema.schemaName + " - " + itemType.typeName
			}))
		}, select);
		return select;
	},
	findAll : function() {
		var itemtypes = [];
		$.ajax({
			url : '/rest/jatyta/kb/itemtypes',
			async : false,
			dataType : 'json',
			context : itemtypes,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(itemtype) {
					this.addObject(App.ItemTypes.create(itemtype))
				}, this);
			}
		});
		return itemtypes;
	},
	findBySchema : function(id) {
		var itemtypes = [];
		data = {
			idSchema : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/itemtypesbyschema',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : itemtypes,
			success : function(response) {
				response.forEach(function(itemtype) {
					this.addObject(App.ItemTypes.create(itemtype))
				}, this);
			}
		});
		return itemtypes;
	},
	page : function(number,search, id) {
		var page = {
			entities : [],
			pagination : null,
			sorting: null	
		}; 
		data = {
			search : search,
			idSchema : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/itemtypesbyschema/'+number,
			async : false,
			dataType : 'json',
			context : page,
			data: data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.entities.forEach(function(itemtype) {
					this.entities.addObject(App.ItemTypes.create(itemtype));
				}, this);
				this.pagination = response.pagination;
				this.sorting = response.sorting;
			}
		});
		return page;
	},
	find : function(id) {
		var itemtype = App.ItemTypes.create({});
		data = {
			id : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/itemtype',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : itemtype,
			data : data,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return itemtype;
	},
	update : function(itemtype) {
		$.ajax({
			url : '/rest/jatyta/kb/updateitemtype',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(itemtype, this.cleanJSON),
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : itemtype,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return itemtype;
	},
	getNew : function() {
		var itemtype = App.ItemTypes.create({});
		$.ajax({
			url : '/rest/jatyta/kb/newitemtype',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : itemtype,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return itemtype;
	},
	remove : function(itemtype) {
		$.ajax({
			url : '/rest/jatyta/kb/deleteitemtype',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(itemtype, this.cleanJSON),
			dataType : 'json',
			context : itemtype,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	}
});

App.ItemProps = Ember.Object.extend();
App.ItemProps.reopenClass({
	findAll : function() {
		var itemprops = [];
		$.ajax({
			url : '/rest/jatyta/kb/itemprops',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : itemprops,
			success : function(response) {
				response.forEach(function(itemprop) {
					this.addObject(App.ItemProps.create(itemprop))
				}, this);
			}
		});
		return itemprops;
	},
	findByItemType : function(id) {
		var itemprops = [];
		data = {
			idItemType : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/itempropsbyitemtype',
			async : false,
			dataType : 'json',
			context : itemprops,
			statusCode : App.ajaxStatusCode,
			data : data,
			success : function(response) {
				response.forEach(function(itemprop) {
					this.addObject(App.ItemProps.create(itemprop))
				}, this);
			}
		});
		return itemprops;
	},
	page : function(number,search, id) {
		var page = {
			entities : [],
			pagination : null,
			sorting: null	
		}; 
		data = {
			search : search,
			idItemType : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/itempropsbyitemtype/'+number,
			async : false,
			dataType : 'json',
			context : page,
			data: data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.entities.forEach(function(itemprop) {
					this.entities.addObject(App.ItemProps.create(itemprop));
				}, this);
				this.pagination = response.pagination;
				this.sorting = response.sorting;
			}
		});
		return page;
	},
	update : function(itemprop) {
		$.ajax({
			url : '/rest/jatyta/kb/updateitemprop',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(itemprop, this.cleanJSON),
			statusCode : App.ajaxStatusCode,
			dataType : 'json',
			context : itemprop,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return itemprop;
	},
	getNew : function() {
		var itemprop = App.ItemProps.create({});
		$.ajax({
			url : '/rest/jatyta/kb/newitemprop',
			async : false,
			dataType : 'json',
			statusCode : App.ajaxStatusCode,
			context : itemprop,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return itemprop;
	},
	remove : function(itemprop) {
		$.ajax({
			url : '/rest/jatyta/kb/deleteitemprop',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(itemprop, this.cleanJSON),
			dataType : 'json',
			context : itemprop,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	}
});

App.PropNames = Ember.Object.extend();
App.PropNames.reopenClass({
	selectItems : function() {
		var select = [];
		App.PropNames.findAll().forEach(function(propname) {
			this.addObject(Ember.Object.create({
				value : propname.idPropName,
				name : propname.name
			}))
		}, select);
		return select;
	},
	findAll : function() {
		var propnames = [];
		$.ajax({
			url : '/rest/jatyta/kb/propnames',
			async : false,
			dataType : 'json',
			context : propnames,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(propname) {
					this.addObject(App.PropNames.create(propname));
				}, this);
			}
		});
		return propnames;
	},
	page : function(number,search) {
		var page = {
			entities : [],
			pagination : null,
			sorting: null	
		}; 
		data = {
			search : search
		};
		$.ajax({
			url : '/rest/jatyta/kb/propnames/'+number,
			async : false,
			dataType : 'json',
			context : page,
			data: data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.entities.forEach(function(propname) {
					this.entities.addObject(App.PropNames.create(propname));
				}, this);
				this.pagination = response.pagination;
				this.sorting = response.sorting;
			}
		});
		return page;
	},
	update : function(propname) {
		$.ajax({
			url : '/rest/jatyta/kb/updatepropname',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(propname, this.cleanJSON),
			dataType : 'json',
			context : propname,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return propname;
	},
	getNew : function() {
		var propname = App.PropNames.create({});
		$.ajax({
			url : '/rest/jatyta/kb/newpropname',
			async : false,
			dataType : 'json',
			context : propname,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return propname;
	},
	remove : function(propname) {
		$.ajax({
			url : '/rest/jatyta/kb/deletepropname',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(propname, this.cleanJSON),
			dataType : 'json',
			context : propname,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	},
	find : function(id) {
		var propname = App.PropNames.create({});
		data = {
			id : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/propname',
			async : false,
			dataType : 'json',
			context : propname,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return propname;
	},
});

App.PropValues = Ember.Object.extend();
App.PropValues.reopenClass({
	findAll : function() {
		var propvalues = [];
		$.ajax({
			url : '/rest/jatyta/kb/propvalues',
			async : false,
			dataType : 'json',
			context : propvalues,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(propvalue) {
					this.addObject(App.PropValues.create(propvalue))
				}, this);
			}
		});
		return propvalues;
	},
	find : function(id) {
		var propvalue = App.PropValues.create({});
		data = {
			idPropValues : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/propvalue',
			async : false,
			dataType : 'json',
			context : propvalue,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return propvalue;
	},
	findByItemProp : function(id) {
		var propvalues = [];
		data = {
			idItemProp : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/propvaluesbyitemprop',
			async : false,
			dataType : 'json',
			context : propvalues,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(propvalue) {
					this.addObject(App.PropValues.create(propvalue))
				}, this);
			}
		});
		return propvalues;
	},
	findByPropName : function(id) {
		var propvalues = [];
		data = {
			idPropName : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/propvaluesbypropname',
			async : false,
			dataType : 'json',
			context : propvalues,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(propvalue) {
					this.addObject(App.PropValues.create(propvalue))
				}, this);
			}
		});
		return propvalues;
	},
	update : function(propvalue) {
		$.ajax({
			url : '/rest/jatyta/kb/updatepropvalue',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(propvalue, this.cleanJSON),
			dataType : 'json',
			context : propvalue,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return propvalue;
	},
	getNew : function() {
		var propvalue = App.PropValues.create({});
		$.ajax({
			url : '/rest/jatyta/kb/newpropvalue',
			async : false,
			dataType : 'json',
			context : propvalue,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return propvalue;
	},
	remove : function(propvalue) {
		$.ajax({
			url : '/rest/jatyta/kb/deletepropvalue',
			async : false,
			type : 'DELETE',
			contentType : "application/json;",
			data : JSON.stringify(propvalue, this.cleanJSON),
			dataType : 'json',
			context : propvalue,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
	},
	savePropNamesAndPropValues : function(propvalueList) {
		var map = [];
		$.ajax({
			url : '/rest/jatyta/kb/savepropnamesandvalues',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(propvalueList, this.cleanJSON),
			dataType : 'json',
			context : map,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return map;
	},
});

App.NativeTypes = Ember.Object.extend();
App.NativeTypes.reopenClass({
	selectItems : function() {
		var select = [];
		App.NativeTypes.findAll().forEach(function(nativeType) {
			this.addObject(Ember.Object.create({
				value : nativeType.idNativeType,
				name : nativeType.typeName
			}))
		}, select);
		return select;
	},
	findAll : function() {
		var nativetypes = [];
		$.ajax({
			url : '/rest/jatyta/kb/nativetypes',
			async : false,
			dataType : 'json',
			context : nativetypes,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(nativetype) {
					this.addObject(App.NativeTypes.create(nativetype))
				}, this);
			}
		});
		return nativetypes;
	},
	find : function(id) {
		var nativeType = App.NativeTypes.create({});
		data = {
			id : id
		};
		$.ajax({
			url : '/rest/jatyta/kb/nativetype',
			async : false,
			dataType : 'json',
			context : nativeType,
			data : data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return nativeType;
	},
	update : function(nativetype) {
		$.ajax({
			url : '/rest/jatyta/kb/updatenativetype',
			async : false,
			type : 'PUT',
			contentType : "application/json;",
			data : JSON.stringify(nativetype, this.cleanJSON),
			dataType : 'json',
			context : nativetype,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return nativetype;
	}
});

App.JatytaCrawlRecords = Ember.Object.extend();
App.JatytaCrawlRecords.reopenClass({
	findAll : function() {
		var crawlrecords = [];
		$.ajax({
			url : '/rest/jatyta/record/crawls',
			async : false,
			dataType : 'json',
			context : crawlrecords,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(crawlrecord) {
					this.addObject(App.JatytaCrawlRecords.create(crawlrecord))
				}, this);
			}
		});
		return crawlrecords;
	}
});

App.FormValueRecords = Ember.Object.extend();
App.FormValueRecords.reopenClass({
	find : function(id, state) {
		var formvalues = [];
		data = {
			id : id,
			state: state
		};
		$.ajax({
			url : '/rest/jatyta/record/formvalues',
			async : false,
			dataType : 'json',
			data : data,
			context : formvalues,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(formvalue) {
					if(formvalue.formFieldRecord.image != null){
						formvalue.imageUrl = App.imageSource + formvalue.formFieldRecord.image;	
					}else{
						formvalue.imageUrl = '';
					}
					if(formvalue.formFieldRecord.labelImage != null){
						formvalue.labelUrl = App.imageSource + formvalue.formFieldRecord.labelImage;
					}else{
						formvalue.labelUrl = '';	
					}	
					this.addObject(App.FormValueRecords.create(formvalue))
				}, this);
			}
		});
		return formvalues;
	},
	findGroupBy : function(id) {
		var formvalues = [];
		data = {
			id : id
		};
		$.ajax({
			url : '/rest/jatyta/record/formvaluesgroupby',
			async : false,
			dataType : 'json',
			data : data,
			context : formvalues,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(formvalue) {
					this.addObject(App.FormValueRecords.create(formvalue))
				}, this);
			}
		});
		return formvalues;
	},
	formStates : function(id) {
		var states = [];
		data = {
			id : id
		};
		$.ajax({
			url : '/rest/jatyta/record/formstates',
			async : false,
			dataType : 'json',
			data : data,
			context : states,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.forEach(function(state) {
					this.addObject(App.FormValueRecords.create(state))
				}, this);
			}
		});
		return states;
	}

});

App.ValidationRecords = Ember.Object.extend();
App.ValidationRecords.reopenClass({
	find : function(id, state) {
		var validations = [];
		data = {
			crawlId : id,
			state: state
		};
		$.ajax({
			url : '/rest/jatyta/record/validations',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : validations,
			success : function(response) {
				response.forEach(function(validation) {
					if(validation.image != null){
						validation.imageUrl = App.imageSource + validation.image;	
					}else{
						validation.imageUrl = '';
					}
					this.addObject(App.ValidationRecords.create(validation))
				}, this);
			}
		});
		return validations;
	},
	validationStates : function(id) {
		var states = [];
		data = {
			crawlId : id
		};
		$.ajax({
			url : '/rest/jatyta/record/validationstates',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : states,
			success : function(response) {
				response.forEach(function(state) {
					this.addObject(App.ValidationRecords.create(state))
				}, this);
			}
		});
		return states;
	},
});

App.BrokenStatesRecords = Ember.Object.extend();
App.BrokenStatesRecords.reopenClass({
	find : function(id) {
		var brokenStates = [];
		data = {
			crawlId : id
		};
		$.ajax({
			url : '/rest/jatyta/record/brokenstates',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : brokenStates,
			success : function(response) {
				response.forEach(function(brokenState) {
					if(brokenState.image != null){
						brokenState.imageUrl = App.imageSource + brokenState.image;	
					}else{
						brokenState.imageUrl = '';
					}
					this.addObject(App.BrokenStatesRecords.create(brokenState))
				}, this);
			}
		});
		return brokenStates;
	},
});

App.StateNamesRecords = Ember.Object.extend();
App.StateNamesRecords.reopenClass({
	find : function(id) {
		var states = [];
		data = {
			crawlId : id
		};
		$.ajax({
			url : '/rest/jatyta/record/statenames',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : states,
			success : function(response) {
				response.forEach(function(state) {
					this.addObject(App.StateNamesRecords.create(state))
				}, this);
			}
		});
		return states;
	},
	page : function(id,number,search) {
		var page = {
			entities : [],
			pagination : null,
			sorting: null	
		}; 
		data = {
			crawlId : id,	
			search : search
		};
		$.ajax({
			url : '/rest/jatyta/record/statenames/'+number,
			async : false,
			dataType : 'json',
			context : page,
			data: data,
			statusCode : App.ajaxStatusCode,
			success : function(response) {
				response.entities.forEach(function(state) {
					this.entities.addObject(App.StateNamesRecords.create(state))
				}, this);
				this.pagination = response.pagination;
				this.sorting = response.sorting;
			}
		});
		return page;
	},
});

App.Reports = Ember.Object.extend();
App.Reports.reopenClass({
	crawlSummary : function(id) {
		var report =  App.Reports.create({});;
		data = {
			crawl_id : id
		};
		$.ajax({
			url : '/rest/jatyta/report/crawlsummary',
			async : false,
			dataType : 'json',
			data : data,
			statusCode : App.ajaxStatusCode,
			context : report,
			success : function(response) {
				this.setProperties(response);
			}
		});
		return report;
	},
});

Ember.Handlebars.registerHelper('ifDisabledBack', function(index){
    if (index = 1) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});