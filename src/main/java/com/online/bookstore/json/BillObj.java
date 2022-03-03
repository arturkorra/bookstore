package com.online.bookstore.json;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Json containing the data of Bill object")
public class BillObj extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4213843336326688972L;

	@NotNull(message = "clientId is null")
	@ApiModelProperty(value = "Customer ID")
	private Long clientId;
	@NotNull(message = "totalPrice is null")
	@ApiModelProperty(value = "Total Price")
	private BigDecimal totalPrice;
	@NotNull(message = "loyaltyPoints is null")
	@ApiModelProperty(value = "Loyalty Points")
	private BigDecimal loyaltyPoints;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

}
