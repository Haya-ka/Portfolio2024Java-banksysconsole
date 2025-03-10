package bank_system;

import java.util.Scanner;

import dao.AuthDAO;
import dao.LogDAO;

public class Main {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		AuthDAO authDAO = new AuthDAO();
		
		//ログイン処理
		while(true){
			System.out.print("ログインID: ");
			int id = scanner.nextInt();
			//改行の消化
			scanner.nextLine();
			System.out.print("パスワード: ");
			String password = scanner.nextLine();
			
			if(authDAO.login(id, password)) {
				//ログイン情報をセッションに保存
				SessionManager.setCurrentUser(authDAO.getUserById(id));
				LogDAO.insertLog(SessionManager.getCurrentUser().getId(), "LOGIN");
				break;
			}
		}
		
		//メニューの表示と操作
		Menu menu = new Menu();
		menu.execute();
		
		scanner.close();
		System.out.println("=== 銀行システムを終了します ===");
	}
}
