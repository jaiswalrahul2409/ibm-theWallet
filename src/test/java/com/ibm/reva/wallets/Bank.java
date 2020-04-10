package com.ibm.reva.wallets;

import java.sql.SQLException;

public class Bank {

	static java.util.Scanner in = new java.util.Scanner(System.in);

	static wallet.dao.AccountDao accountDao = new wallet.dao.AccountDaoImp();

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		int choice = 0;
		while (true) {
			System.out.println("* ------------ Welcome to THE WALLET ------------ *");
			System.out.print("\t	1. Create Account\n");
			System.out.print("\t	2. Get Account Balance\n");
			System.out.print("\t	3. Deposit Amount\n");
			System.out.print("\t	4. Withdraw Amount\n");
			System.out.print("\t	5. Fund Transfer\n");
			System.out.print("\t	6. Print Transactions\n");
			System.out.print("\t	0. Exit\n");
			choice = in.nextInt();
			switch (choice) {
			case 1:
				createAccount();
				break;
			case 2:
				getBalance();
				break;
			case 3:
				depositAmount();
				break;
			case 4:
				withdrawAmount();
				break;
			case 5:
				fundTransfer();
				break;
			case 6:
				transaction();
				break;
			case 0:
				System.exit(0);
			}
		}

	}

	private static void transaction() throws ClassNotFoundException, SQLException {
		accountDao.transaction();
		
	}

	private static void createAccount() {
		System.out.println("Account Name? ");
		in.nextLine();
		String accountName = in.nextLine();
		System.out.println(accountName);
		System.out.println("Account Pin? ");
		String accountPin = in.nextLine();
		System.out.println(accountPin);
		
		
		System.out.println("Account ID?");
		int accountId = in.nextInt();
		System.out.println(accountId);
		try {
			accountDao.createAccount(accountName, accountPin, accountId);
			System.out.println("Account Created with Account ID :" + accountId);

		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found Exception");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL Exception");
			e.printStackTrace();
		}
	}

	private static void getBalance() {
		System.out.println("Account ID?");
		int accountId = in.nextInt();
		System.out.println("Account Pin?");
		int pin = in.nextInt();
		try {
			accountDao.getBalance(accountId, pin);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void depositAmount() {

		System.out.println("Enter Account ID");
		int accountId = in.nextInt();
		System.out.println("Enter Account Pin");
		int pin = in.nextInt();
		System.out.println("Enter Amount");
		double amount = in.nextDouble();
		try {
			accountDao.depositAmount(accountId, pin, amount);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void withdrawAmount() throws ClassNotFoundException, SQLException {
		System.out.println("Enter Account ID");
		int accountId = in.nextInt();
		System.out.println("Enter Account Pin");
		in.nextLine();
		String pin = in.nextLine();
		int pincheck = accountDao.pinCheck(accountId, pin);
		if (pincheck == 1) {
			System.out.println("Enter Amount");
			double amount = in.nextDouble();
			try {
				accountDao.withdrawAmount(accountId, amount);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Pin Incorrect");
		}
	}

	private static void fundTransfer() throws ClassNotFoundException, SQLException {

		System.out.println("Enter Account ID");
		int accountId = in.nextInt();
		System.out.println("Enter Account Pin");
		in.nextLine();
		String pin = in.nextLine();
		int pincheck = accountDao.pinCheck(accountId, pin);
		if (pincheck == 1) {
			System.out.println("Enter Amount");
			double amount = in.nextDouble();
			System.out.println("Enter the recepient account Id");
			int toAccountId = in.nextInt();

			accountDao.fundTransfer(accountId, toAccountId, amount);
		}
	}

}