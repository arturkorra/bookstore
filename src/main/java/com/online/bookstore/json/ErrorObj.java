package com.online.bookstore.json;

import com.online.bookstore.enums.BookStoreErrors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Output Json containing error informations")
public class ErrorObj extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2935879222835344829L;

	public ErrorObj(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public ErrorObj(Throwable cause, String mess) {
		if (cause != null) {
			this.message = cause.getMessage();
		} else {
			this.message = mess;
		}
		this.code = BookStoreErrors.GENERIC_ERROR.getCode();
	}

	@ApiModelProperty(value = "Error code")
	private Integer code;
	@ApiModelProperty(value = "Error message")
	private String message;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ ");
		if (code != null)
			builder.append("\"code\": ").append(code).append(", ");
		if (message != null)
			builder.append("\"message\": \"").append(message).append("\", ");
		builder.setLength(builder.length() - 2);
		builder.append(" }");
		return builder.toString();
	}
}
