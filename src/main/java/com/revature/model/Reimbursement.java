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
import javax.persistence.Lob;
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
	
	@Column(name = "receipt_image", columnDefinition="LONGBlob")
	@Lob
	private byte[] recieptImage;
	
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

	public Reimbursement(double amount, Timestamp resolved, String description, byte[] recieptImage) {
		super();
		this.amount = amount;
		this.submitted = submitTimestamp;
		this.resolved = resolved;
		this.description = description;
		this.recieptImage = recieptImage;
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

	public byte[] getRecieptImage() {
		return recieptImage;
	}

	public void setRecieptImage(byte[] recieptImage) {
		this.recieptImage = recieptImage;
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

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public Timestamp getSubmitTimestamp() {
		return submitTimestamp;
	}

	public void setSubmitTimestamp(Timestamp submitTimestamp) {
		this.submitTimestamp = submitTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(recieptImage);
		result = prime * result + Objects.hash(amount, author, description, id, now, resolved, resolver, status,
				submitTimestamp, submitted, type);
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
		Reimbursement other = (Reimbursement) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(author, other.author) && Objects.equals(description, other.description)
				&& id == other.id && now == other.now && Arrays.equals(recieptImage, other.recieptImage)
				&& Objects.equals(resolved, other.resolved) && Objects.equals(resolver, other.resolver)
				&& Objects.equals(status, other.status) && Objects.equals(submitTimestamp, other.submitTimestamp)
				&& Objects.equals(submitted, other.submitted) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submitted=" + submitted + ", resolved=" + resolved
				+ ", description=" + description + ", recieptImage=" + Arrays.toString(recieptImage) + ", author="
				+ author + ", resolver=" + resolver + ", status=" + status + ", type=" + type + ", now=" + now
				+ ", submitTimestamp=" + submitTimestamp + "]";
	}

	
	
}
