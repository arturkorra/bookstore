package com.online.bookstore.json;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.online.bookstore.enums.BookType;
import com.online.bookstore.enums.CurrencyType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Json containing the data of Book object")
public class BookObj extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = -846186476521775426L;

	@NotNull(message = "id is null")
	@ApiModelProperty(value = "Book ID")
	private Long id;
	@NotBlank(message = "name is blank")
	@ApiModelProperty(value = "Book Name")
	private String name;
	@NotNull(message = "price is null")
	@ApiModelProperty(value = "Book Price")
	private BigDecimal price;
	@NotNull(message = "bookType is null")
	@ApiModelProperty(value = "Book Type")
	private BookType bookType;
	@NotNull(message = "inStock is null")
	@ApiModelProperty(value = "Book InStock")
	private int inStock;
	@NotNull(message = "CurrencyType is null")
	@ApiModelProperty(value = "CurrencyType")
	private CurrencyType currencyType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BookType getBookType() {
		return bookType;
	}

	public void setBookType(BookType bookType) {
		this.bookType = bookType;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

}
