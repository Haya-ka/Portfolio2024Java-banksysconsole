package test;

public class AccountTest {
	/*
	public static void main(String[] args) {
		//テスト
		AccountDAO accountDAO = new AccountDAO();
		TransactionDAO transactionDAO = new TransactionDAO();
		try {
			// 上で取得したユーザーIDを設定
			int user_id = 1;
			BigDecimal initialDeposit = 1000;
			accountDAO.createAccount(user_id, initialDeposit);
			System.out.println("口座が作成されました！");
			// 口座ID 1 の残高を表示
			System.out.println("口座ID " + 1 + " の残高: " + accountDAO.getBalance(1) + " 円");
			// deposit()テスト（口座ID 1 に1000を入金する
			accountDAO.deposit(1, new BigDecimal("1000"));
			System.out.println("口座ID " + 1 + " の残高: " + accountDAO.getBalance(1) + " 円");
			// withdraw()テスト（口座ID 1 から1000を出金する）
			accountDAO.withdraw(1, new BigDecimal("1000"));
			System.out.println("口座ID " + 1 + " の残高: " + accountDAO.getBalance(1) + " 円");
			// ユーザーID 1 の口座一覧を取得・表示
			accountDAO.getAccountsByUserId(1);
			System.out.println(accountDAO.getUserAccountDetails(1).toString());
			// 口座ID 1 の取引履歴を取得・表示
			List<Transaction> transactions = transactionDAO.getTransactionsByAccount(1);
			for(var transaction: transactions)
				System.out.println(transaction);
			// そのほか
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
}
