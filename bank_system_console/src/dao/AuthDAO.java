package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bank_system.DatabaseConnector;
import bank_system.PasswordHasher;
import model.User;

public class AuthDAO {
	
	public void createUser(String name, String email, String password) throws SQLException {
		String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
		String hashedPassword = PasswordHasher.hashPassword(password);
		
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, name);
			pStmt.setString(2, email);
			pStmt.setString(3, hashedPassword);
			pStmt.executeUpdate();
			System.out.println("ユーザー情報を登録しました。");
		}catch (SQLException e) {
			throw new SQLException("ユーザー登録中にシステムエラーが発生しました");
		}
	}
	
	public boolean login(int id, String password) {
		String sql = "SELECT * FROM users WHERE id = ? AND password = ?";
		String hashedPassword = PasswordHasher.hashPassword(password);
		
		//DatabaseContainerでデータベースに接続
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, id);
			pStmt.setString(2, hashedPassword);
			ResultSet rs = pStmt.executeQuery();
			// 該当データがあればtrueを返す
			boolean loginOK = rs.next();
			if(loginOK) {
				System.out.println("ログインが成功しました。");
				return true;
			}
			else {
				System.out.println("ユーザ名とパスワードが一致しません。");
				return false;
			}
		}catch (Exception e) {
			System.out.println("ログインに失敗しました");
			e.printStackTrace();
			return false;
		}
	}
	
	public User getUserById(int id) {
		String sql = "SELECT * FROM users WHERE id = ?";
		
		//DatabaseContainerでデータベースに接続
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();
			//ログイン確認済のため該当データをUserにまとめ返す
			if(rs.next()) {
				System.out.println("ユーザ情報が見つかりました。");
				String name = rs.getString("name");
				String email = rs.getString("email");
				return new User(id, name, email);
			}
			else {
				System.out.println("ユーザ情報が見つかりませんでした。");
				return null;
			}
			
		}catch (Exception e) {
			System.out.println("ユーザ情報の確認中にシステムエラーが発生しました。");
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers() throws SQLException {
		String sql = "SELECT id, name, email FROM users";
		List<User> userList = new ArrayList<>();
		
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql);
				ResultSet rs = pStmt.executeQuery()) {
			while (rs.next()) {
				User user = new User(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("email")
						);
				userList.add(user);
			}
		}catch (SQLException e) {
			throw new SQLException("ユーザー一覧の取得中にエラーが発生しました", e);
		}
		return userList;
	}
}
