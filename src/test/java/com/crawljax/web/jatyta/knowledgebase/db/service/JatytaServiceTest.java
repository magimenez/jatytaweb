package com.crawljax.web.jatyta.knowledgebase.db.service;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemProp;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.NativeType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.Schema;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;

public class JatytaServiceTest {

	public static void main(String[] args) {

		// Get the session from the Session factory

		try {
			JatytaService service = new JatytaService();

			NativeType nativeType = new NativeType();
			nativeType.setKeyName("number");
			service.saveEntity(nativeType);
			nativeType = new NativeType();
			nativeType.setKeyName("date");
			service.saveEntity(nativeType);
			nativeType = new NativeType();
			nativeType.setKeyName("boolean");
			service.saveEntity(nativeType);
			nativeType = new NativeType();
			nativeType.setKeyName("datetime");
			service.saveEntity(nativeType);
			nativeType = new NativeType();
			nativeType.setKeyName("time");
			service.saveEntity(nativeType);
			nativeType = new NativeType();
			nativeType.setKeyName("text");
			service.saveEntity(nativeType);

			Schema schema = new Schema();
			schema.setSchemaName("schema.org");
			schema.setIsSchemaOrg(true);
			service.saveEntity(schema);

			ItemType itemType = new ItemType();
			itemType.setKeyName("jatyta");
			itemType.setComments("prueba");
			itemType.setSchema(schema);
			service.saveEntity(itemType);

			itemType = (ItemType) service.getEntityByKeyName(itemType).get(0);
			nativeType = (NativeType) service.getEntityByKeyName(nativeType)
					.get(0);

			ItemProp itemProp = new ItemProp();
			itemProp.setKeyName("errorPagePatern");
			itemProp.setItemType(itemType);
			// itemProp.setNativeType(nativeType);
			service.saveEntity(itemProp);

			itemProp = (ItemProp) service.getEntityByKeyName(itemProp).get(0);

			PropValue propValue = new PropValue();
			// propValue.setItemProp(itemProp);
			propValue.setIsValid(true);
			propValue.setKeyName("404");
			service.saveEntity(propValue);
			propValue = new PropValue();
			// propValue.setItemProp(itemProp);
			propValue.setIsValid(true);
			propValue.setKeyName("Error [0-9]+");
			service.saveEntity(propValue);
			propValue = new PropValue();
			// propValue.setItemProp(itemProp);
			propValue.setIsValid(true);
			propValue.setKeyName("no encontrada");
			service.saveEntity(propValue);
			propValue = new PropValue();
			// propValue.setItemProp(itemProp);
			propValue.setIsValid(true);
			propValue.setKeyName("not found");
			service.saveEntity(propValue);
		} catch (JatytaException e) {

			e.printStackTrace();
		}

		/*
		 * List<Object> list = service.getAllTestingValues(); for(Object
		 * valueRegArray : list) { // propValue = (PropValue) valueRegArray;
		 * System.out.println(valueRegArray.toString()); }
		 */
	}
}
