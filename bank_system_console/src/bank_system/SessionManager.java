package bank_system;

import model.Account;
import model.User;

public class SessionManager {
	private static User currentUser = null;
	private static Account currentAccount = null;
	
	//ログイン中のユーザ制御
	public static void setCurrentUser(User user) {
		currentUser = user;
	}
	public static User getCurrentUser() {
		return currentUser;
	}
	public static boolean isLoggedIn() {
		return currentUser != null;
	}
	public static void logout() {
		currentUser = null;
		currentAccount = null;
	}
	
	//操作中の口座情報
	public static void setCurrentAccount(Account account) {
		currentAccount = account;
	}
	public static Account getCurrentAccount() {
		return currentAccount;
	}
}
