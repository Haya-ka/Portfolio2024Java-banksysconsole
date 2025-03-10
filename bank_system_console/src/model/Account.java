package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
	private int id;
	private int userId;
	private BigDecimal balance;
	private Timestamp timestamp;
	private String name;
	
	// コンストラクタ
	public Account(int id, int userId, BigDecimal balance, Timestamp timestamp, String name) {
		this.id = id;
		this.userId = userId;
		this.balance = balance;
		this.timestamp = timestamp;
		this.name = name;
		}
	
	// Getter & Setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public Timestamp gettimestamp() {
		return timestamp;
	}
	public void settimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getUserName() {
		return name;
	}
	public void setUserName(String name) {
		this.name = name;
	}
	
	// デバッグ用オーバーライド
	@Override
	public String toString() {
		return "Account{" +
				"id='" + id + '\'' +
	            ". userId=" + userId +
                ", balance=" + balance +
                ", timestamp='" + timestamp + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
