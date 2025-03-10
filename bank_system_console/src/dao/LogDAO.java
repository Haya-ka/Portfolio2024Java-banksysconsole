package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import bank_system.DatabaseConnector;

public class LogDAO {
	public static void insertLog(int userId, String action) {
		String sql = "INSERT INTO logs (user_id, action) VALUES (?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, userId);
			pStmt.setString(2, action);
			pStmt.executeUpdate();
			System.out.println("システムログを記録しました。");
		}catch (Exception e) {
			System.out.println("システムログを記録中にエラーが発生しました。");
			e.printStackTrace();
		}
	}
}