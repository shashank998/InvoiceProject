package com.altimetrik.config;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.altimetrik.serviceimplementation.ExtractPdf;



public class ReceiveEmailWithAttachment {
	public static String fromAddress = null;

	public void receiveEmail(String pop3Host, String userName, String password) {
		// Set properties

		Properties props = new Properties();
		props.put("mail.store.protocol", "pop3");
		props.put("mail.pop3.host", pop3Host);
		props.put("mail.pop3.port", "995");
		props.put("mail.pop3.starttls.enable", "true");

		// Get the Session object.
		Session session = Session.getInstance(props);
		MimeBodyPart part = null;
		PDDocument doc = null;

		try {
			// Create the POP3 store object and connect to the pop store.
			Store store = session.getStore("pop3s");
			store.connect(pop3Host, userName, password);

			// Create the folder object and open it in your mailbox.
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// Retrieve the messages from the folder object.
			Message[] messages = emailFolder.getMessages();
			System.out.println("Total Message" + messages.length);

			// Iterate the messages
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
				System.out.println("---------------------------------");
				System.out.println("Details of Email Message " + (i + 1) + " :");
				System.out.println("Subject: " + message.getSubject());
				fromAddress = message.getFrom()[0].toString();
				System.out.println("From: " + message.getFrom()[0]);

				// Iterate recipients
				System.out.println("To: ");
				for (int j = 0; j < toAddress.length; j++) {
					System.out.printf(toAddress[j].toString());
				}

				// Iterate multiparts
				Object object = message.getContent();
				if (object instanceof Multipart) {
					Multipart multipart = (Multipart) message.getContent();
					for (int k = 0; k < multipart.getCount(); k++) {
						part = (MimeBodyPart) multipart.getBodyPart(k);

						if (part.getDisposition() != null && part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
							System.out.println("file name " + part.getFileName());
							System.out.println("size " + part.getSize());

							doc = PDDocument.load(part.getInputStream());
							if (doc != null) {
								// ReadingText.receiveMail(doc);
								ExtractPdf.fileExtract(doc);
							}
						}

					}
				}

				else {
					System.out.println("Email does not contain any attachment");
				}

				// close the folder and store objects

			}

			emailFolder.close(false);

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
