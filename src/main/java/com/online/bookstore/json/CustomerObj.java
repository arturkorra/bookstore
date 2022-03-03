package com.online.bookstore.json;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Json containing the data of Customer object")
public class CustomerObj extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3140451411892983041L;

	@NotNull(message = "id is null")
	@ApiModelProperty(value = "Customer ID")
	private Long id;
	@NotBlank(message = "firstName is blank")
	@ApiModelProperty(value = "Customer FistName")
	private String firstName;
	@NotBlank(message = "lastName is blank")
	@ApiModelProperty(value = "Customer LastName")
	private String lastName;
	@NotBlank(message = "country is blank")
	@ApiModelProperty(value = "Customer Country")
	private String country;
	@NotBlank(message = "city is blank")
	@ApiModelProperty(value = "Customer City")
	private String city;
	@NotBlank(message = "zip is blank")
	@ApiModelProperty(value = "Customer ZIP Code")
	private String zip;
	@NotBlank(message = "address is blank")
	@ApiModelProperty(value = "Customer Address")
	private String address;
	@NotNull(message = "loyaltyPoints is null")
	@ApiModelProperty(value = "Customer LoyaltyPoints")
	private BigDecimal loyaltyPoints;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
