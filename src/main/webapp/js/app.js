
App = Ember.Application.create({LOG_TRANSITIONS: true});

Ember.Route.reopen({
	getTargetRoute: function(){
		var infos = this.router.router.targetHandlerInfos;
		return infos[infos.length - 1].handler.routeName;
	}
});

App.Router.map(function() {
	this.resource("configurations", function() {
		this.resource("configuration", {path: "/:configuration_id"}, function() {
			this.route("rules");
   			this.route("assertions");
			this.route("plugins");
			this.route("validation");
			this.route("jatyta");
			this.route("forms");
			this.route("login");
			this.route("brokenlinks");
		});
		this.route("new");
		this.resource("new", {path: "new/:configuration_id"});
	});
	
	this.resource("plugin_management", {path: "plugins"});
	
	this.resource("jatyta_management", {path: "jatyta_management"}, function (){
		this.route("kbdata");
		this.route("propnames");
	});
	
	this.resource("crawlrecords", function(){ 
		this.resource("crawlrecord", {path: "/:crawlrecord_id"}, function() {
			this.resource("log");
			this.resource("plugin_output", {path: "plugin/:plugin_no"});
			this.resource("formfieldvalues");
			this.resource("validationrecords");
			this.resource("brokenstates");
			this.resource("statenames");
		});
		this.resource("config_filter", {path: "filter/:config_id"});
	});

});

App.ApplicationRoute = Ember.Route.extend({
	setupController: function(controller, model){
		controller.set("executionQueue", App.CrawlRecords.findAll(undefined, true));
		if(!("WebSocket" in window)) {
			alert('Need a browser that supports Sockets');
		} else {
			controller.connectSocket();

		}
	},
	actions: {
	    openModal: function(modalName, model) {
	      this.controllerFor(modalName).set('model', model);
	      return this.render(modalName, {
	        into: 'application',
	        outlet: 'modal'
	      });
	    },
	    closeModal: function() {
	      return this.disconnectOutlet({
	        outlet: 'modal',
	        parentView: 'application'
	    });
	  }
	}
});

App.IndexRoute = Ember.Route.extend({
	redirect: function() {
		this.transitionTo('configurations');
	}
});

App.ConfigurationsIndexRoute = Ember.Route.extend({
	model: function (params) {
		return App.Configurations.findAll();
	},
	setupController: function(controller, model) {
		controller.set('content', model);
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav",
			[App.Link.create({text:"New Configuration", target:"#/configurations/new", icon:"icon-pencil"})]);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "Configurations", target: "#/configurations"})]);
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});

App.ConfigurationRoute = Ember.Route.extend({
	model: function(params) {
		return App.Configurations.find(params.configuration_id);
	},
	setupController: function(controller, model) {

		
		controller.set("content", model);
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("rest", controller.rest);
		sideNav.set("content", controller.get("content"));
		sideNav.set("sidenav",
				[App.Link.create({text:"Run Configuration", target:"run", action:true, icon:"icon-play"}),
				App.Link.create({text:"Save Configuration", target:"save", action:true, icon:"icon-ok"}),
				App.Link.create({text:"Crawl History", target:"#/crawlrecords/filter/" + model.id, icon:"icon-book"}),
				App.Link.create({text:"New Copy", target:"#/configurations/new/" + model.id, icon:"icon-pencil"}),
				App.Link.create({text:"Delete Configuration", target:"delete", action:true, icon:"icon-remove"})]);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "Configurations", target: "#/configurations"}),
				App.Link.create({text: model.id, target: "#/configurations/" + model.id})]);
	},
	serialize: function(context) {
		return { configuration_id: context.id };
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});

App.ConfigurationIndexRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configuration'}); }
});

App.ConfigurationRulesRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configuration'}); }
});

App.ConfigurationAssertionsRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configuration'}); }
});

App.ConfigurationPluginsRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configuration'}); }
});

App.ConfigurationValidationRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configurationValidation'}); },
	setupController: function(controller, model) {
		var selectedConfigId =  this.controllerFor('configuration').get("content.id");
		controller.set('selectedConfigId', selectedConfigId);
		controller.set('validations', App.ValidationConfigurations.findByConfigId(selectedConfigId));
		controller.set('htmlElements', App.HtmlElements.findAll());
	}
});

App.ConfigurationBrokenlinksRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configurationBrokenlinks'}); },
	setupController: function(controller, model) {
		var selectedConfigId =  this.controllerFor('configuration').get("content.id");
		controller.set('selectedConfigId', selectedConfigId);
		controller.set('brokenLinks', App.BrokenLinksConfigurations.findByConfigId(selectedConfigId));
	}
});

App.ConfigurationJatytaRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configurationJatyta'}); },
	setupController: function(controller, model) {
		var selectedConfigId =  this.controllerFor('configuration').get("content.id");
		controller.set('selectedConfigId', selectedConfigId);
		//verificar si ya existe una configuracion o crear una nueva.
		model = App.CrawlConfigurations.find(selectedConfigId);
		model.estimatedTime = 0;
		model.idschema = 0;	
		if(model.id==null){
			model = App.CrawlConfigurations.getNew(selectedConfigId);
		}else if (model.schema!=null){
			model.idschema = model.schema.idSchema;
		}
		controller.set('content', model);
		var schemaList = [];
		schemaList.addObject(App.schemaNameALL);
		schemaList.addObjects(App.Schemas.selectItems());
		controller.set('schemasSelect', schemaList);
	}
});

App.ConfigurationFormsRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configurationForms'}); },
	setupController: function(controller, model) {
		var selectedConfigId =  this.controllerFor('configuration').get("content.id");
		controller.set('selectedConfigId', selectedConfigId);
		//verificar si ya existe una configuracion o crear una nueva.
		model = App.CrawlConfigurations.find(selectedConfigId);
		model.estimatedTime = 0;
		model.idschema =  0;
		if(model.id==null){
			model = App.CrawlConfigurations.getNew(selectedConfigId);
		}else if (model.schema!=null){
			model.idschema =  model.schema.idSchema;
		}
		controller.set('content', model);
		//forms list
		var formList = App.FormConfigurations.findByConfigId(selectedConfigId);
		controller.set('formList', formList);
	}
});

App.ConfigurationLoginRoute = Ember.Route.extend({
	renderTemplate: function(){ this.render({controller: 'configurationLogin'}); },
	setupController: function(controller, model) {
		var selectedConfigId =  this.controllerFor('configuration').get("content.id");
		controller.set('selectedConfigId', selectedConfigId);
		//verificar si ya existe una configuracion o crear una nueva.
		model = App.CrawlConfigurations.find(selectedConfigId);
		model.idschema =  0;
		if(model.id==null){
			model = App.CrawlConfigurations.getNew(selectedConfigId);
			
		}else if (model.schema!=null){
			model.idschema =  model.schema.idSchema;
		}
		controller.set('content', model);
		var schemaList = [];
	}
});

App.ConfigurationsNewRoute = Ember.Route.extend({
	setupController: function(controller, model) {
		var controller = this.controllerFor('configuration');
		var model = App.Configurations.getNew();
		controller.set('content', model);
		
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("rest", controller.rest);
		sideNav.set("content", model);
		sideNav.set("sidenav",
			[App.Link.create({text:"Save Configuration", target:"add", action:true, icon:"icon-ok"})]);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "Configurations", target: "#/configurations"}), App.Link.create({text: "New"})]);
	},
	renderTemplate: function() {
		this.render({controller: 'configuration'});
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});

App.NewRoute = Ember.Route.extend({
	model: function(params) {
		return {id: params.configuration_id};
	},
	setupController: function(controller, model) {
		var controller = this.controllerFor('configuration');
		var model = App.Configurations.getNew(model.id);
		controller.set('content', model);
		
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("rest", controller.rest);
		sideNav.set("content", model);
		sideNav.set("sidenav",
			[App.Link.create({text:"Save Configuration", target:"add", action:true, icon:"icon-ok"})]);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "Configurations", target: "#/configurations"}), App.Link.create({text: "New"})]);
	},
	renderTemplate: function() {
		this.render("configurations/new", {controller: 'configuration'});
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});

App.PluginManagementRoute = Ember.Route.extend({
	model: function(params) {
		return App.Plugins.findAll();
	},
	setupController: function(controller, model) {
		controller.set('content', model);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "Plugins", target: "#/plugins"})]);
		if(!(window.File && window.FileReader && window.FileList && window.Blob)){
			alert('The File APIs are not fully supported in this browser.');
		}
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});

App.JatytaManagementRoute = Ember.Route.extend({
	model: function(params) {
		return [];
	},
	setupController: function(controller, model) {
		controller.set('content', model);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "Jatyta", target: "#/jatyta"})]);
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});


App.JatytaManagementIndexRoute = Ember.Route.extend({
	renderTemplate: function(){this.render({controller: 'jatytaManagementIndex'});  }
});

App.JatytaManagementKbdataRoute = Ember.Route.extend({
	renderTemplate: function(){this.render({controller: 'jatytaManagementKbdata'});  },
	setupController: function(controller, model) {
		var pagSchema = App.Schemas.page(1, '');
		controller.set('indexSchema', 1);
		controller.set('countSchema', pagSchema.pagination.count);
		controller.set('totalPagesSchema', Math.ceil( pagSchema.pagination.count/pagSchema.pagination.limit));
		controller.set('schemas', pagSchema);
		
		controller.set('itemtypes', []);
		controller.set('itemprops', []);
		controller.set('propvalues', []);
		controller.set('nativetypes', App.NativeTypes.findAll());
		controller.set('selectedSchema', null);
		controller.set('selectedItemType', null);
		controller.set('selectedItemProp', null);
		Ember.set('App.propNamesSelect', App.PropNames.selectItems());
		
	}
});

App.JatytaManagementCrawlhistoryRoute = Ember.Route.extend({
	renderTemplate: function(){this.render({controller: 'jatytaManagementCrawlhistory'});  },
	setupController: function(controller, model) {
		controller.set('jatytaCrawlRecords', App.JatytaCrawlRecords.findAll());
		
	}
});

App.JatytaManagementPropnamesRoute = Ember.Route.extend({
	//renderTemplate: function(){this.render({controller: 'jatytaManagementPropnames'});  },
	model : function(params){
		var propnames = App.PropNames.page(1, ''); 
		return propnames;
	},
	setupController: function(controller, model) {
		controller.set('content',model);
		controller.set('index', 1);
		controller.set('count', model.pagination.count);
		controller.set('totalPages', Math.ceil( model.pagination.count/model.pagination.limit));
		Ember.set('App.nativeTypesSelect', App.NativeTypes.selectItems());
	} 
});

App.CrawlrecordsIndexRoute = Ember.Route.extend({
	model: function(params) {
		return App.CrawlRecords.findAll();
	},
	setupController: function(controller, model) {
		controller.set('content', model);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "History", target: "#/crawlrecords"})]);
	}
});

App.CrawlrecordRoute = Ember.Route.extend({
	model: function(params) {
		return App.CrawlRecords.find(params.crawlrecord_id);
	},
	setupController: function(controller, model) {
		controller.set('content', model);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "History", target: "#/crawlrecords"}), App.Link.create({text: "Crawl " + model.id, target: "#/crawlrecords/" + model.id})]);
		var sideNav = this.controllerFor("sidenav");
		var links = [App.Link.create({text:"Configuration", target:"#/configurations/" + model.configurationId, icon:"icon-wrench"})];
		var linkReport = '/rest/jatyta/report/crawlsummary?'+'crawl_id='+controller.get('content.id');
		links.addObject(App.Link.create({text:"Crawl Summary Report", target:linkReport, icon:"icon-download"}));
		sideNav.set("sidenav", links);
		//sideNav.set("sidenav", [App.Link.create({text:"Add new Schema", target:"#/configurations/", icon:"icon-plus"})]);
		//sideNav.set("sidenav", [App.Link.create({text:"Add new Item Type", target:"#/configurations/", icon:"icon-plus"})]);
		//sideNav.set("sidenav", [App.Link.create({text:"Add new PropNames from Value Records", target:"#/configurations/" + model.configurationId, icon:"icon-plus"})]);
		//sideNav.set("sidenav", []);


	},
	serialize: function(context) {
		return { crawlrecord_id: context.get("id") };
	},
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});

App.CrawlrecordIndexRoute = Ember.Route.extend({
	redirect: function(arg) {
		if(this.getTargetRoute() === this.routeName)
			this.transitionTo('log');
	}
});

App.LogRoute = Ember.Route.extend({
	setupController: function(controller, model) {
		var controller = this.controllerFor('crawlrecord');
		var appController = this.controllerFor('application');
		if (controller.isLogging) {
			appController.sendMsg('stoplog');
		}
		setTimeout(function(){ 
			$('#logPanel').css({'height':(($(document).height())-162)+'px'});
			$('#logPanel').empty();
			appController.sendMsg('startlog-' + controller.get('content.id'));
			controller.set("isLogging", true);
			}, 0);
	},
	deactivate: function() {
		this.controllerFor('application').sendMsg('stoplog');
	},
	renderTemplate: function() {
		this.render({controller: 'crawlrecord'});
	}
});

App.PluginOutputRoute = Ember.Route.extend({
	model: function(params) {
		return {key: params.plugin_no};
	},
	serialize: function(context) {
		return { plugin_no: context.key };
	}
});

App.FormfieldvaluesRoute = Ember.Route.extend({
	setupController: function(controller, model) {
		var cr_controller = this.controllerFor('crawlrecord');
		
		controller.set('formValueGroupBy', App.FormValueRecords.findGroupBy(cr_controller.get('content.id')));
		var states = App.FormValueRecords.formStates(cr_controller.get('content.id'));
		controller.set('formStates', states);
		if (states.length > 0) {
			controller.set('formState', states[0]);
			controller.set('index', 0);
			controller.set('formValueRecords', App.FormValueRecords.find(cr_controller.get('content.id'), states[0].state));
		}
		
	}
});

App.ValidationrecordsRoute = Ember.Route.extend({
	setupController: function(controller, model) {
		var cr_controller = this.controllerFor('crawlrecord');
		
		controller.set('validationStates', App.ValidationRecords.validationStates(cr_controller.get('content.id')))
		if (controller.get('validationStates').length > 0) {
			controller.set('validationState', controller.get('validationStates')[0]);
			controller.set('index', 0);
			controller.set('validationRecords', App.ValidationRecords.find(cr_controller.get('content.id'),controller.get('validationState').get('state') ));
		}
	}
});

App.BrokenstatesRoute = Ember.Route.extend({
	setupController: function(controller, model) {
		var cr_controller = this.controllerFor('crawlrecord');
		controller.set('brokenStates', App.BrokenStatesRecords.find(cr_controller.get('content.id')));
		if (controller.get('brokenStates').length > 0) {
			controller.set('brokenState', controller.get('brokenStates')[0]);
			controller.set('index', 0);
		}
		
	}
});

App.StatenamesRoute = Ember.Route.extend({
	model : function(params){
		var cr_controller = this.controllerFor('crawlrecord');
		var stateNames = App.StateNamesRecords.page(cr_controller.get('content.id'), 1, ''); 
		return stateNames;
	},
	setupController: function(controller, model) {
		var cr_controller = this.controllerFor('crawlrecord');
		controller.set('content',model);
		controller.set('idCrawlRecord',cr_controller.get('content.id'));
		controller.set('stateNames', App.StateNamesRecords.find(controller.get('idCrawlRecord')));
		controller.set('index', 1);
		controller.set('count', model.pagination.count);
		controller.set('totalPages', Math.ceil( model.pagination.count/model.pagination.limit));
	}
});

App.ConfigFilterRoute = Ember.Route.extend({
	model: function(params) {
		return {config_id: params.config_id}
	},
	setupController: function(controller, model) {
		var controller = this.controllerFor("crawlrecords.index");
		var model = App.CrawlRecords.findAll(model.config_id);
		controller.set('content', model);
		
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", [App.Link.create({text:"All Crawl Records", target:"#/crawlrecords", icon:"icon-book"})]);
		this.controllerFor('breadcrumb').set("breadcrumb", [App.Link.create({text: "History", target: "#/crawlrecords"})]);
	},
	renderTemplate: function(){ this.render("crawlrecords/index", {controller: "crawlrecords.index"}); },
	exit: function(router){
		var sideNav = this.controllerFor("sidenav");
		sideNav.set("sidenav", []);
	}
});