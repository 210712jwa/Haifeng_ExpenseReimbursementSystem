package com.revature.dto;

import java.sql.Blob;
import java.util.Arrays;
import java.util.Objects;

public class AddOrEditReimbursementDTO {
	private double amount;
	private String description;
	private byte[] reciept;
	private String type;

	public AddOrEditReimbursementDTO() {
		super();
	}

	public AddOrEditReimbursementDTO(double amount, String description, byte[] reciept, String type) {
		super();
		this.amount = amount;
		this.description = description;
		this.reciept = reciept;
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

	public byte[] getReciept() {
		return reciept;
	}

	public void setReciept(byte[] reciept) {
		this.reciept = reciept;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(reciept);
		result = prime * result + Objects.hash(amount, description, type);
		return result;
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
				&& Objects.equals(description, other.description) && Arrays.equals(reciept, other.reciept)
				&& Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "AddOrEditReimbursementDTO [amount=" + amount + ", description=" + description + ", reciept="
				+ Arrays.toString(reciept) + ", type=" + type + "]";
	}

}
