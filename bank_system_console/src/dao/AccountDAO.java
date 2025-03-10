package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bank_system.DatabaseConnector;
import bank_system.SessionManager;
import model.Account;
import model.Transaction;

public class AccountDAO {
	
	public boolean createAccount(int user_id, BigDecimal initialDeposit) throws SQLException {
		String sql = "INSERT INTO accounts (user_id, balance) VALUES (?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, user_id);
			pStmt.setBigDecimal(2, initialDeposit);
			pStmt.executeUpdate();
			System.out.println("新規口座を開設しました。");
			return true;
		}catch (SQLException e) {
			throw new SQLException("口座開設中にシステムエラーが発生しました");
		}
	}
	
	public void getAccountsByUserId(int userId) throws SQLException {
		String sql = "SELECT * FROM accounts WHERE user_id = ?;";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pStmt = conn.prepareStatement(sql)) {
		
			pStmt.setInt(1, userId);
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("口座ID："+rs.getInt("id")+"\t残高："+rs.getDouble("balance")+"\t口座開設日："+rs.getTimestamp("created_at"));
				while (rs.next()) {
					System.out.println("口座ID："+rs.getInt("id")+"\t残高："+rs.getDouble("balance")+"\t口座開設日："+rs.getTimestamp("created_at"));
				}
			}
			else {
				System.out.println("口座の登録がありません。");
				System.out.println("新規口座を開設してください。");
			}
		}catch (SQLException e) {
			throw new SQLException("口座検索中にシステムエラーが発生しました。");
		}
	}
	
	public Account getAccountByAccountId(int id, int user_id) throws SQLException {
		String sql = "SELECT * FROM accounts WHERE id = ? AND user_id = ?";
		
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, id);
			pStmt.setInt(2, user_id);
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.next()) {
				Account account = new Account(
						rs.getInt("id"),
						rs.getInt("user_id"),
						rs.getBigDecimal("balance"),
						rs.getTimestamp("created_at"),
						SessionManager.getCurrentUser().getName()
						);
				System.out.println("口座情報を取得しました。");
				return account;
			}
		}catch (SQLException e) {
			throw new SQLException("口座情報の取得中にシステムエラーが発生しました。");
		}
		System.out.println("口座情報の取得に失敗しました。");
		return null;
	}

	public List<Account> getUserAccountDetails(int userId) {
		List<Account> accounts = new ArrayList<>();
		String sql = "SELECT u.id user_id, u.name, a.id account_id, a.balance , a.timestamp " +
					 "FROM users u JOIN accounts a ON u.id = a.user_id WHERE u.id = ?";
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, userId);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				Account account = new Account(
						rs.getInt("account_id"),
						rs.getInt("user_id"),
						rs.getBigDecimal("balance"),
						rs.getTimestamp("timestamp"),
						rs.getString("name")
						);
				accounts.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	
	public BigDecimal getBalance(int accountId) throws SQLException {
		String sql = "SELECT balance FROM accounts WHERE id = ?";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setInt(1, accountId);
			ResultSet rs = pStmt.executeQuery();
			
			if (rs.next()) {
				return rs.getBigDecimal("balance");
			} else {
				System.out.println("残高を確認する口座が見つかりません。");
			}
		} catch (SQLException e) {
			throw new SQLException("残高の取得中にシステムエラーが発生しました。");
		}
		return BigDecimal.ZERO;
	}
	
	public boolean deposit(int accountId, BigDecimal amount) throws SQLException {
		String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setBigDecimal(1, amount);
			pStmt.setInt(2, accountId);
			
			int rowsAffected = pStmt.executeUpdate();;
			// 取引履歴を保存
			if (rowsAffected > 0) {
				TransactionDAO transactionDAO = new TransactionDAO();
				transactionDAO.addTransaction(new Transaction(accountId, "deposit", amount));
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean withdraw(int accountId, BigDecimal amount) throws SQLException {
		String getBalanceSql = "SELECT balance FROM accounts WHERE id = ?";
		String updateSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
		
		try(Connection conn = DatabaseConnector.getConnection();
				PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceSql);
				PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
			
			//現在の残高を取得
			getBalanceStmt.setInt(1, accountId);
			ResultSet rs = getBalanceStmt.executeQuery();
			
			if(rs.next()) {
				BigDecimal currentBalance = rs.getBigDecimal("balance");
				//残高不足のチェック
				if(currentBalance.compareTo(amount) < 0) {
					System.out.println("残高不足のため出金できません。");
					return false;
				}
				//残高を減らす
				updateStmt.setBigDecimal(1, amount);
				updateStmt.setInt(2, accountId);
				
				int rowsAffected = updateStmt.executeUpdate();
				//取引履歴を保存
				if(rowsAffected > 0) {
					TransactionDAO transactionDAO = new TransactionDAO();
					transactionDAO.addTransaction(new Transaction(accountId, "withdraw", amount));
					return true;
				}
			}
			}catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException("出金処理中にシステムエラーが発生しました。");
			}
		return false;
	}
	
	public boolean transfer(int transferAccountId, int receiveAccountId, BigDecimal amount) {
		String getBalanceSql = "SELECT balance FROM accounts WHERE id = ?";
		String withdrawSQL = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
		String depositSQL = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
		String logSQL = "INSERT INTO transactions (id, where_id, amount, transaction_type, timestamp) VALUES (?, ?, ?, 'transfer', NOW())";
		
		try (Connection conn = DatabaseConnector.getConnection()) {
			//トランザクション開始
			conn.setAutoCommit(false);
			
			try (PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceSql);
					PreparedStatement withdrawStmt = conn.prepareStatement(withdrawSQL);
					PreparedStatement depositStmt = conn.prepareStatement(depositSQL);
					PreparedStatement logStmt = conn.prepareStatement(logSQL)) {
				//残高をチェックする
				getBalanceStmt.setInt(1, transferAccountId);
				ResultSet rs = getBalanceStmt.executeQuery();
				if(rs.next()) {
					BigDecimal currentBalance = rs.getBigDecimal("balance");
					//残高不足のチェック
					if(currentBalance.compareTo(amount) < 0) {
						System.out.println("残高不足のため出金できません。");
						return false;
					}
					//送金元の口座から減算
					withdrawStmt.setBigDecimal(1, amount);
					withdrawStmt.setInt(2, transferAccountId);
					withdrawStmt.executeUpdate();
					
					//送金先の口座へ加算
					depositStmt.setBigDecimal(1, amount);
					depositStmt.setInt(2, receiveAccountId);
					depositStmt.executeUpdate();
					
					//取引ログを記録
					logStmt.setInt(1, transferAccountId);
					logStmt.setInt(2, receiveAccountId);
					logStmt.setBigDecimal(3, amount);
					logStmt.executeUpdate();
					
					//トランザクションを確定
					conn.commit();
					
					int rowsAffected = withdrawStmt.executeUpdate();
					//取引履歴を保存
					if(rowsAffected > 0) {
						TransactionDAO transactionDAO = new TransactionDAO();
						transactionDAO.addTransaction(new Transaction(transferAccountId, "transfer", amount));
						//送金成功
						return true;
					}
				}
			}catch (Exception e) {
				//途中で失敗したらロールバック
				conn.rollback();
				e.printStackTrace();
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	// 拡張機能
}