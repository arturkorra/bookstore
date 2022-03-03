package com.online.bookstore.json;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Json containing the data of Cart object")
public class CartObj extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4899891611707678662L;

	@NotNull(message = "bookId is null")
	@ApiModelProperty(value = "Book ID")
	private Long bookId;
	@NotNull(message = "quantity is null")
	@ApiModelProperty(value = "Book Quantity")
	private int quantity;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
