package com.online.bookstore.json;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SerializableObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8436995572461192018L;

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
