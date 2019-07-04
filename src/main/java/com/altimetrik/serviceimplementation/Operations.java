package com.altimetrik.serviceimplementation;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.altimetrik.config.DataBaseConnection;
import com.altimetrik.config.ReceiveEmailWithAttachment;
import com.altimetrik.config.SendEmail;

public class Operations {

	public static void saveRecord(String invoiceNo, String invoiceDate, String customerPO, String address,
			String amount) {
		Connection conn = null;
		try {
			conn = DataBaseConnection.dbConnection();
			PreparedStatement pre1 = conn.prepareStatement("SELECT * FROM InvoiceDetails where InvoiceNo=?");
			pre1.setString(1, invoiceNo);
			ResultSet re = pre1.executeQuery();
			if (re.next()) {
				System.out.println("Data record is already in the table");
			} else {

				PreparedStatement pre = conn.prepareStatement("INSERT INTO InvoiceDetails VALUES(?,?,?,?,?)");
				pre.setString(1, invoiceNo);
				pre.setString(2, invoiceDate);
				pre.setString(3, customerPO);
				pre.setString(4, address);
				pre.setString(5, amount);
				pre.executeUpdate();
				System.out.println("Data records are inserted!!");
			}

		} catch (SQLException e) {
			System.err.println("Failed to connect to database" + e);
		} finally {

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

	}

	public static void getDataByinvoiceNumber(String invoiceNo) {
		Connection conn = null;
		try {
			conn = DataBaseConnection.dbConnection();
			PreparedStatement pre1 = conn.prepareStatement("SELECT * FROM InvoiceDetails where InvoiceNo=?");
			PreparedStatement pre2 = conn
					.prepareStatement("update InvoiceDetails set APPROVESTATUS='true' where InvoiceNo=?");
			pre1.setString(1, invoiceNo);
			pre2.setString(1, invoiceNo);
			PreparedStatement pre3 = conn
					.prepareStatement("SELECT APPROVESTATUS FROM InvoiceDetails where InvoiceNo=?");
			pre3.setString(1, invoiceNo);
			ResultSet re = pre1.executeQuery();

			if (re.next()) {
				System.out.println("invoice:\tinvoiceDate\tcustomerPO\taddress\t\tamount");

				System.out.println(
						"invoice:re.getString(1)" + "\n" + "invoiceDate:" + re.getDate(2) + "\n" + "customerPO:"
								+ re.getString(3) + "\n" + "address" + re.getString(4) + "\t\t" + re.getString(5));
				// return "aproved";

				pre2.executeUpdate();
				System.out.println("Invoice number :" + invoiceNo + "Aproved");
				pre2.close();
				pre3.executeQuery();
				ResultSet result = pre1.executeQuery();
				while (result.next()) {
					if (result.getString(1) == "true") {
						SendEmail.sendingEmail(ReceiveEmailWithAttachment.fromAddress,re.getString(1));
						System.out.println("");
					}

					else {
						System.out.println("Invoice Data is not Approved!!!");
					}
				}

			} else {
				System.out.println("Data record is not there in the table!!");
				// return "not aproved";
			}

		} catch (SQLException e) {
			System.err.println("Failed to connect to database" + e);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}

			}
		}
	}
}
