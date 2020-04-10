package wallet.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import wallet.util.DBUtil;

/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import wallet.model.Account;*/

public class AccountDaoImp implements AccountDao {

	/*
	 * @Autowired JdbcTemplate jdbcTemplate;
	 * 
	 * 
	 * public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	 * this.jdbcTemplate = jdbcTemplate; }
	 */

	public int createAccount(String accountName, String pin, int accountId)
			throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();

		String sql = "INSERT INTO wallet VALUES(?,?,?,?)";
		// String sql = "INSERT INTO wallet VALUES(?,?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, accountId);
		statement.setString(2, accountName);
		statement.setDouble(3, 0);
		statement.setString(4, pin);
		statement.executeUpdate();
		connection.close();

		return accountId;

		// return jdbcTemplate.update(sql,accountId,accountName,pin);
	}

	public double getBalance(int accountId, int pin) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		String sql = "SELECT account_Balance FROM wallet where Account_Id = ? AND Account_Pin = ?";
		java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, accountId);
		statement.setInt(2, pin);
		java.sql.ResultSet result = statement.executeQuery();
		if (result.next()) {
			double bal = result.getDouble("account_Balance");
			System.out.println("Account with ID: " + accountId + " has Balance " + bal);
		}

		return 0;
	}

	public double depositAmount(int accountId, int pin, double amount) throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();
		String sql = "UPDATE wallet SET Account_Balance = Account_Balance+? WHERE Account_Id = ? AND Account_Pin = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDouble(1, amount);
		statement.setInt(2, accountId);
		statement.setInt(3, pin);
		statement.executeUpdate();

		sql = "SELECT Account_Balance FROM wallet where Account_Id = ? AND Account_Pin = ?";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, accountId);
		statement.setInt(2, pin);
		java.sql.ResultSet result = statement.executeQuery();
		if (result.next()) {
			double bal = result.getDouble("Account_Balance");
			System.out.println("Amount deposited. Account with ID: " + accountId + " has Balance: " + bal);
			System.out.println("\n");
		}

		String transtype = "Deposit";
		java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(new java.util.Date().getTime());
		String sql1 = "INSERT INTO transaction VALUES(?,?,?,?)";
		statement = connection.prepareStatement(sql1);
		statement.setInt(1, accountId);
		statement.setDouble(2, amount);
		statement.setString(3, transtype);
		statement.setTimestamp(4, sqlTimeStamp);
		statement.executeUpdate();

		connection.close();

		return 0;
	}

	public double withdrawAmount(int accountId, double amount) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		String sql = "UPDATE wallet SET Account_Balance = Account_Balance-? WHERE Account_Id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDouble(1, amount);
		statement.setInt(2, accountId);
		statement.executeUpdate();

		sql = "SELECT Account_Balance FROM wallet where Account_Id = ?";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, accountId);
		java.sql.ResultSet result = statement.executeQuery();
		if (result.next()) {
			double bal = result.getDouble("Account_Balance");
			System.out.println("Amount Withdrawn. Account with ID: " + accountId + " has Balance: " + bal);
		}

		String transtype = "Withdraw";
		java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(new java.util.Date().getTime());
		String sql1 = "INSERT INTO transaction VALUES(?,?,?,?)";
		statement = connection.prepareStatement(sql1);
		statement.setInt(1, accountId);
		statement.setDouble(2, amount);
		statement.setString(3, transtype);
		statement.setTimestamp(4, sqlTimeStamp);
		statement.executeUpdate();

		connection.close();

		return 0;
	}

	public double fundTransfer(int accountId, int toAccountId, double amount)
			throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();
		String sql = "UPDATE wallet SET account_Balance = account_Balance+? WHERE Account_Id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDouble(1, amount);
		statement.setInt(2, toAccountId);
		statement.executeUpdate();

		sql = "UPDATE wallet SET account_Balance = account_Balance-? WHERE Account_Id = ?";
		statement = connection.prepareStatement(sql);
		statement.setDouble(1, amount);
		statement.setInt(2, accountId);
		statement.executeUpdate();

		sql = "SELECT account_Balance FROM wallet where Account_Id = ?";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, accountId);
		java.sql.ResultSet result = statement.executeQuery();
		if (result.next()) {
			double bal = result.getDouble("account_Balance");
			System.out.println("Amount Transferred. Your Account Balance: " + bal);
		}
		connection.close();
		return 0;
	}

	public int pinCheck(int accountId, String pin) throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();
		String sql = "SELECT Account_Pin FROM wallet where Account_Id = ?";
		java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, accountId);
		java.sql.ResultSet result = statement.executeQuery();
		if (result.next()) {
			String dbpin = result.getString("Account_Pin");
			if (dbpin.equalsIgnoreCase(pin)) {
				return 1;
			}
		}
		return 0;
	}

	public void transaction() throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM transaction");
		if (rs.next()) {
			do {
				System.out.println("Account Id	Amount	Transaction Type	Created At");
				System.out.println(rs.getString(1) + " 		" + rs.getString(2) + " 	" + rs.getString(3)
						+ " 		" + rs.getString(4) + "\n");
			} while (rs.next());
		} else {
			System.out.println("Record Not Found...");
		}

	}

}
