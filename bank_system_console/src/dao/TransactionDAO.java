package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bank_system.DatabaseConnector;
import model.Transaction;

public class TransactionDAO {
	// 取引履歴を追加する
	public void addTransaction(Transaction transaction) {
		String sql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, transaction.getAccountId());
			pstmt.setString(2, transaction.getTransactionType());
			pstmt.setBigDecimal(3, transaction.getAmount());
			
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 特定の口座の取引履歴を取得する
	public List<Transaction> getTransactionsByAccount(int accountId) {
		List<Transaction> transactions = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
		
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setInt(1, accountId);
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()) {
				Transaction transaction = new Transaction(
						rs.getInt("account_id"),
						rs.getString("transaction_type"),
						rs.getBigDecimal("amount")
						);
				transaction.setId(rs.getInt("id"));
				transaction.setTimestamp(rs.getTimestamp("timestamp"));
				
				transactions.add(transaction);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
}
