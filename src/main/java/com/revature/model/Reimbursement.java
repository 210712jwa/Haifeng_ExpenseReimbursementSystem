package com.revature.model;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Table(name = "reimbursement")
public class Reimbursement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reimb_id")
	private int id;
	
	@Column(name = "amount", nullable = false, columnDefinition = "Decimal(30,2)")
	private double amount;
	
	@Column(name = "submitted_time", nullable = false)
	private Timestamp submitted;
	
	@Column(name = "resolved_time")
	private Timestamp resolved;
	
	@Column(name = "description", length = 250)
	private String description;
	
	@Column(name = "receipt")
	private Blob reciept;
	
	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private Users author;
	
	@ManyToOne
	@JoinColumn(name = "resolver_id")
	private Users resolver;
	
	@ManyToOne
	@JoinColumn(name = "reimb_status_id", nullable = false)
	private ReimbursementStatus status;
	
	@ManyToOne
	@JoinColumn(name = "reimb_type_id", nullable = false)
	private ReimbursementType type;
	
	@Transient
	private long now = System.currentTimeMillis();
	@Transient
    private Timestamp submitTimestamp = new Timestamp(now);

	public Reimbursement() {
		super();
	}

	public Reimbursement(double amount, Timestamp resolved, String description, Blob reciept) {
		super();
		this.amount = amount;
		this.submitted = submitTimestamp;
		this.resolved = resolved;
		this.description = description;
		this.reciept = reciept;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Blob getReciept() {
		return reciept;
	}

	public void setReciept(Blob reciept) {
		this.reciept = reciept;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Users getResolver() {
		return resolver;
	}

	public void setResolver(Users resolver) {
		this.resolver = resolver;
	}

	public ReimbursementStatus getStatus() {
		return status;
	}

	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}

	public ReimbursementType getType() {
		return type;
	}

	public void setType(ReimbursementType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, author, description, id, reciept, resolved, resolver, status, submitted, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(author, other.author) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(reciept, other.reciept) && Objects.equals(resolved, other.resolved)
				&& Objects.equals(resolver, other.resolver) && Objects.equals(status, other.status)
				&& Objects.equals(submitted, other.submitted) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submitted=" + submitted + ", resolved=" + resolved
				+ ", description=" + description + ", reciept=" + reciept + ", author=" + author + ", resolver="
				+ resolver + ", status=" + status + ", type=" + type + "]";
	}	
	
}
