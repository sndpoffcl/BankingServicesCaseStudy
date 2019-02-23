package com.cg.banking.daoservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cg.banking.beans.Account;
import com.cg.banking.util.BankingDBUtil;


public class AccountDAOImpl implements AccountDAO {

	private static Connection con  = BankingDBUtil.getDBConnection();
	
	
	@Override
	public Account save(Account account) {
		try {
			con.setAutoCommit(false);
			
			PreparedStatement pstmt1 = con.prepareStatement("insert into Account(AccountNumber , PinNumber, AccountType , AccountStatus , AccountBalance)values(ACCOUNT_NO_SEQ.NEXTVAL,?,?,?,?)");
			pstmt1.setInt(1,  account.getPinNumber());
			pstmt1.setString(2, account.getAccountType());
			pstmt1.setString(3, account.getAccountStatus());
			pstmt1.setFloat(4, account.getAccountBalance());
			pstmt1.executeUpdate();
			
			PreparedStatement pstmt2 = con.prepareStatement("select max(accountNumber)from Account");
			ResultSet rs = pstmt2.executeQuery();
			rs.next();
			int accountNo = rs.getInt(1);
			
			account.setAccountNo(accountNo);
			
			
			con.commit();
		}catch(SQLException e ) {
			e.printStackTrace();
		}
		try {
			con.rollback();
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return account;
	}

	@Override
	public boolean update(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account findOne(long accountNo) {
		try {
			PreparedStatement pstmt1 = con.prepareStatement("select * from Account where accountNumber="+accountNo);
			ResultSet accountRs=pstmt1.executeQuery();
			if(accountRs.next()) {
				int pinNumber=accountRs.getInt("pinNumber"); 
				String accountType=accountRs.getString("accountType");
				String accountStatus=accountRs.getString("accountStatus");
				float accountBalance=accountRs.getFloat("accountBalance");
				
				Account account = new Account(accountNo, pinNumber, accountType, accountStatus, accountBalance);
				
				return account;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Account> findAll() {
		ArrayList<Account> accounts = new ArrayList<Account>();
		try {
			PreparedStatement pstmt1 = con.prepareStatement("select * from Account ");
			ResultSet accountRs=pstmt1.executeQuery();
			while(accountRs.next()) {
				int accountNo=accountRs.getInt("accountNo");
				
				Account account = new Account(accountNo, accountRs.getInt("accountBalance"), accountRs.getString("AccountType"), accountRs.getString("Accountstatus"), accountRs.getInt("pinNumber"));
				accounts.add(account);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	
}
