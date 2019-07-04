package com.altimetrik.controller;

import java.util.Scanner;

import com.altimetrik.config.ReceiveEmailWithAttachment;
import com.altimetrik.serviceimplementation.Operations;

public class SaasOperation {

	public static void main(String[] args) {

		ReceiveEmailWithAttachment emial = new ReceiveEmailWithAttachment();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String pop3Host = "pop.gmail.com";
		// change accordingly
		// final String userName = "jyots987@gmail.com";// change accordingly
		// final String password = "Mummy@78";// change accordingly
		final String userName = "shashankcrg@gmail.com";// change accordingly
		final String password = "******";// change accordingly

		int choice;
		String invoiceNo = null;
		while (true) {
			System.out.println(
					"Enter the choice "+"\n"
					+ "1:Email Attachment and database store"+"\n"+
					"2:Get Data By invoice number"+"\n"
					+ "3:exit");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				emial.receiveEmail(pop3Host, userName, password);
				// System.out.println("Data is stored in the DataBase");
				break;
			case 2:
				System.out.println("Eneter the invoice number to approve");
				invoiceNo = sc.next();
				Operations.getDataByinvoiceNumber(invoiceNo);

				break;
			default:
				System.exit(0);

			}

			// call receiveEmail

		}
	}

}
