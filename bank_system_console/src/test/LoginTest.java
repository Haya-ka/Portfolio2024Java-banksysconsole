package test;

import dao.AuthDAO;

public class LoginTest {
	
	public static void main(String[] args) {
		AuthDAO authDAO = new AuthDAO();
		boolean success = authDAO.login(1, "password123");
		if (success) {
			System.out.println("ログイン成功！");
		} else {
			System.out.println("ログイン失敗！");
		}
	}
}
