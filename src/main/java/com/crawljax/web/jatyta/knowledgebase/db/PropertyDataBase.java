/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase.db;

import java.util.List;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.knowledgebase.KnowledgeBase;
import com.crawljax.web.jatyta.knowledgebase.db.util.HibernateUtil;

/**
 * @author Beto
 * 
 */
public class PropertyDataBase extends KnowledgeBase {

	protected final Logger logger = LoggerFactory.getLogger(PropertyDataBase.class);

	/**
	 * @param configProperties
	 */
	public PropertyDataBase(Properties configProperties) {
		super(configProperties);

	}

	@Override
	public void loadPropertyFile() {
		
		Properties properties = new Properties();

		final Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			final Transaction transaction = session.beginTransaction();
		  	try {

				String hql = "select distinct "
						+ "ip.itemType.schema.schemaName"
						+ ", ip.itemType.typeName" + ", ip.propName.name"
						+ ", ip.propName.nativeType.typeName" + ", p.isValid, p.value "
						+ "from ItemProp ip, PropValue p "
						+ " where p.propName = ip.propName " + "order by "
						+ "ip.itemType.schema.schemaName"
						+ ", ip.itemType.typeName" + ", ip.propName.name "
						+ ", ip.propName.nativeType.typeName" + ", p.isValid"
						+ ", p.value ";

				Query query = session.createQuery(hql);

				List<Object[]> valueRegArrayList = query.list();
				String lastKey = "";
				String values = "";
				for (Object[] valueRegArray : valueRegArrayList) {
					String key = valueRegArray[0]
							+ KnowledgeBase.PROPERTY_SEPARATOR.toString()
							+ valueRegArray[1]
							+ KnowledgeBase.PROPERTY_SEPARATOR.toString()
							+ valueRegArray[2]
							+ KnowledgeBase.PROPERTY_SEPARATOR.toString()
							+ valueRegArray[3]
							+ KnowledgeBase.PROPERTY_SEPARATOR.toString()
							+ valueRegArray[4];
					if (key.equals(lastKey)) {
						values += KnowledgeBase.VALUE_SEPARATOR.toString()
								+ valueRegArray[5];
					} else {
						if (!values.isEmpty()) {
							properties.setProperty(lastKey, values);
						}
						if(valueRegArray[5]!=null){
							values = valueRegArray[5].toString();	
						}else{
							values = "null";
						}
						
					}

					lastKey = key;
				}

				if (!values.isEmpty()) {
					properties.setProperty(lastKey, values);
				}
		    	transaction.commit();
		  	} catch (Exception ex) {
		    	// Log the exception here
		    	transaction.rollback();
		    	throw ex;
		  	}
		} finally {
		  	session.close();
		}

		setProperties(properties);
		storePropertyFile();
	}

	@Override
	public void storePropertyFile() {

		Properties properties = getProperties();

		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			//TODO corregir el error de null pointer exception.
			/*if(value !=null){
				logger.debug("KnowledgeBase item : " + key + " => " + value);	
			}else{
				logger.debug("KnowledgeBase item : " + key + " => null");
			}*/
			
		}
	}

}
