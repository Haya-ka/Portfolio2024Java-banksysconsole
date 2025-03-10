package bank_system;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

import dao.AccountDAO;
import dao.AuthDAO;
import dao.LogDAO;

public class Menu {
	private static Scanner scanner = new Scanner(System.in);
	private AuthDAO authDAO = new AuthDAO();
	private static AccountDAO accountDAO = new AccountDAO();
	
	public static void showMenu() {
		System.out.println("=== メニュー ===");
		System.out.println("1. 残高照会");
		System.out.println("2. 入金");
		System.out.println("3. 出金");
		System.out.println("4. 送金");
		System.out.println("6. 新規ユーザー登録");
		System.out.println("7. 新規口座開設");
		System.out.println("8. 口座の変更");
		System.out.println("9. ログアウト");
		System.out.print("選択肢を入力してください: ");
	}
	
	public void execute() {
		boolean running = true;
		//操作口座の選択
		selectAccount();
		
		while (running) {
			showMenu();
			int select = scanner.nextInt();
			// 改行の消化
			scanner.nextLine();
			
			switch (select) {
				case 1:
					//残高照会
					showBalance();
					break;
				case 2:
					//入金
					deposit();
					break;
				case 3:
					//出金
					withdraw();
					break;
				case 4:
					//送金
					transfer();
					break;
				case 6:
					//新規ユーザ登録
					createUser();
					break;
				case 7:
					//新規口座開設
					createAccount();
					break;
				case 8:
					//口座変更
					selectAccount();
					break;
				case 9:
					// ログアウト
					logout();
					running = false;
					break;
				default:
					System.out.println("無効な選択肢です。");
					System.out.println("再度選択してください。");
			}
		}
	}
	
	private static void selectAccount() {
		boolean running = true;
		while(running) {
			//口座一覧を表示し選択・保存する
			System.out.println("口座一覧を取得中...");
			try {
				accountDAO.getAccountsByUserId(SessionManager.getCurrentUser().getId());
			}catch (SQLException e) {
				System.out.println("口座一覧の取得中にシステムエラーが発生しました。");
				e.printStackTrace();
			}
			
			System.out.print("操作する口座IDを選択：");
			int select = scanner.nextInt();
			//改行の消化
			scanner.nextLine();
			
			try{
				SessionManager.setCurrentAccount(accountDAO.getAccountByAccountId(select, SessionManager.getCurrentUser().getId()));
				running = false;
			}catch (SQLException e) {
				System.out.println("口座の選択中にシステムエラーが発生しました。");
				e.printStackTrace();
			}
		}
	}
	
	public static void showBalance() {
		//残高を表示
		System.out.println("残高照会中...");
		BigDecimal balance = null;
		try {
			balance = accountDAO.getBalance(SessionManager.getCurrentAccount().getId());
		} catch (SQLException e) {
			System.out.println("残高の取得中にシステムエラーが発生しました。");
			e.printStackTrace();
		}
		System.out.println("現在の残高: " + balance + "円");
	}
	
	public static void deposit() {
		//入金処理
		System.out.println("入金処理中...");
		int id = SessionManager.getCurrentAccount().getId();
		System.out.print("入金額を入力してください: ");
		BigDecimal amount = scanner.nextBigDecimal();
		//改行の消化
		scanner.nextLine();
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("入金額は1円以上である必要があります。");
			return;
		}
		try {
			if (accountDAO.deposit(id, amount)) {
				System.out.println(amount + "円を入金しました。");
			}else {
				System.out.println("入金に失敗しました。");
			}
		} catch (SQLException e) {
			System.out.println("入金処理中にシステムエラーが発生しました。");
			e.printStackTrace();
		}
	}
	
	public static void withdraw() {
		//出金処理
		System.out.println("出金処理中...");
		int id = SessionManager.getCurrentAccount().getId();
		System.out.print("出金額を入力してください: ");
		BigDecimal amount = scanner.nextBigDecimal();
		//改行の消化
		scanner.nextLine();
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("出金額は1円以上である必要があります。");
			return;
		}
		try {
			if (accountDAO.withdraw(id, amount)) {
				System.out.println(amount + "円を出金しました。");
			}else {
				System.out.println("残高不足のため出金できません。");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void transfer() {
		//送金処理
		System.out.println("送金手続きを開始します。");
		int transferAccountId = SessionManager.getCurrentAccount().getId();
		System.out.print("送金先の口座番号: ");
		int receiveAccountId = scanner.nextInt();
		//改行の消化
		scanner.nextLine();
		System.out.print("送金額: ");
		BigDecimal amount = scanner.nextBigDecimal();
		//改行の消化
		scanner.nextLine();
		if (accountDAO.transfer(transferAccountId , receiveAccountId, amount)) {
			System.out.println(amount + "円を口座番号: " + receiveAccountId + " へ送金しました。");
		}else {
			System.out.println("残高不足のため送金できません。");
		}
	}
	
	private void createUser() {		
		System.out.println("ユーザ登録を行います。");
		System.out.print("お名前: ");
		String newname = scanner.nextLine();
		System.out.print("パスワード: ");
		String newpassword = scanner.nextLine();
		System.out.print("メールアドレス: ");
		String newemail = scanner.nextLine();
		try {
			authDAO.createUser(newname, newemail, newpassword);
			System.out.println("ユーザ登録が完了しました。");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void createAccount() {
		//入金処理
		System.out.println("口座新規登録中...");
		System.out.print("初期入金額を入力してください: ");
		BigDecimal amount = scanner.nextBigDecimal();
		//改行の消化
		scanner.nextLine();
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			System.out.println("入金額は1円以上である必要があります。");
			return;
		}
		try {
			if (accountDAO.createAccount(SessionManager.getCurrentUser().getId(), amount)) {
				System.out.println(amount + "円を新規口座に入金しました。");
			}else {
				System.out.println("新規口座を開設しました。");
			}
		} catch (SQLException e) {
			System.out.println("新規口座の開設中にシステムエラーが発生しました。");
			e.printStackTrace();
		}
	}
	
	public static void logout() {
		//ログアウト処理
		LogDAO.insertLog(SessionManager.getCurrentUser().getId(), "LOGOUT");
		System.out.println("ログアウトしました。");
		SessionManager.logout();
	}
}
