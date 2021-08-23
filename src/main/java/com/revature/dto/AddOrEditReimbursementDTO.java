package com.revature.dto;

import java.sql.Blob;
import java.util.Objects;

public class AddOrEditReimbursementDTO {
	private double amount;
	private String description;
	private Blob recipt;
	private String type;

	public AddOrEditReimbursementDTO() {
		super();
	}

	public AddOrEditReimbursementDTO(double amount, String description, Blob recipt, String type) {
		super();
		this.amount = amount;
		this.description = description;
		this.recipt = recipt;
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Blob getRecipt() {
		return recipt;
	}

	public void setRecipt(Blob recipt) {
		this.recipt = recipt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, description, recipt, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddOrEditReimbursementDTO other = (AddOrEditReimbursementDTO) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(description, other.description) && Objects.equals(recipt, other.recipt)
				&& Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "AddOrEditReimbursementDTO [amount=" + amount + ", description=" + description + ", recipt=" + recipt
				+ ", type=" + type + "]";
	}

}
