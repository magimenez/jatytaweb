package com.crawljax.web.jatyta.model.entities;

import java.io.Serializable;

public interface EntityInterface<K extends Serializable> {
	K getId();
	void setId(K id);
	String getKeyName();
	void setKeyName(String keyName);
}
