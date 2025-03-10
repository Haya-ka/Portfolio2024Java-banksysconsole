package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
	private int id;
	private int accountId;
	private Integer whereId;
	private String transactionType;
	private BigDecimal amount;
	private Timestamp timestamp;
	
	public Transaction(int accountId, String transactionType, BigDecimal amount) {
		this.accountId = accountId;
		this.whereId = null;
		this.transactionType = transactionType;
		this.amount = amount;
	}
	
	public Transaction(int accountId,Integer whereId, String transactionType, BigDecimal amount) {
		this.accountId = accountId;
		this.whereId = whereId;
		this.transactionType = transactionType;
		this.amount = amount;
	}
	
	// Setter
	public void setId(int id) {
		this.id = id;
	}
	public void setWhereId(int whereId) {
		this.whereId = whereId;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	// Getter
	public int getId() {
		return id;
	}
	public int getWhereId() {
		return whereId;
	}
	public int getAccountId() {
		return accountId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	//デバッグ用オーバーライド
	@Override
	public String toString() {
		return "Transaction{" +
				"id=" + id +
				", accountNumber='" + accountId + '\'' +
				", transactionType='" + transactionType + '\'' +
				", amount=" + amount +
				", timestamp=" + timestamp +
				'}';
	}
}
