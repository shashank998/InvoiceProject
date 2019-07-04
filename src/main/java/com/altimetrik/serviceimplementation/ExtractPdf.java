package com.altimetrik.serviceimplementation;

import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import java.io.IOException;

public class ExtractPdf {

	public static void fileExtract(PDDocument doc) throws IOException {

		try {
			doc.getClass();

			if (!doc.isEncrypted()) {

				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper tStripper = new PDFTextStripper();

				String pdfFileInText = tStripper.getText(doc);
				String lines[] = pdfFileInText.split("\n");
				String invoiceNo = "", invoiceDate = "", address = "", amount = "";
				String customerPO = "";
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].trim().equals("Invoice No")) {
						invoiceNo = lines[i + 1].trim();
					}
					if (lines[i].trim().equals("Invoice Date"))
						invoiceDate = lines[i + 1].trim();
					if (lines[i].trim().equals("Customer P.O."))
						customerPO = lines[i + 1].trim();
					if (lines[i].trim().equals("Sold To")) {
						while (!lines[++i].trim().startsWith("Ship To"))
							address += lines[i] + " ";
					}
				}
				outer: for (int i = 0; i < lines.length; i++) {
					if (lines[i].trim().equals("Total Invoice")) {
						for (int j = i + 1; j < lines.length; j++) {
							if (!lines[j + 1].trim().startsWith("$")) {
								amount = lines[j].trim();
								amount = amount.replace(",", "");
								amount = amount.replace("$", "");
								break outer;
							}
						}
					}
				}
				System.out.println("Invoice No :" + invoiceNo);
				System.out.println("Invoice Date :" + invoiceDate);
				System.out.println("Cust PO :" + customerPO);
				System.out.println("Addess :" + address);
				System.out.println("Total Invoice " + amount);
				Operations.saveRecord(invoiceNo, invoiceDate, customerPO, address, amount);
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				System.out.println("email does not contain attachement");
			}
		}
	}
}
