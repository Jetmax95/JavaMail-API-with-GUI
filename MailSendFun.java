import java.net.Socket;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.plaf.multi.MultiButtonUI;

import com.sun.mail.imap.IMAPFolder;

/**
 * This class handles all the functionality realated to
 * creating, sending e-mails , attaching things and adding cc to your email.
 * @author jetmaxz
 *
 */

public class MailSendFun {

	Store store;	   
	Session session;   
	String username;
	String password;
	String smtphost;
	
	
	public MailSendFun(Session session , String username , String password){
		
		this.session = session;
		this.username = username;
		this.password = password;
		
		this.store = null;
		this.smtphost = "smtp.gmail.com";
		
	}
	
	
	/**
	 * Creates a new e-mail with no attachment
	 * @param recipient The person to send the e-mail to
	 * @param subject The subject of the e-mail
	 * @param body The body of the e-mail
	 * @return Returns the message formed.
	 */
	public Message createEmail(String recipient , String subject , String body){
		
		MimeMessage message = new MimeMessage(session);
		
		try{
			
			message.setFrom(new InternetAddress(recipient));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(body);

			message.saveChanges();
			
			System.out.println("Email Created");
			
		}catch(MessagingException e){
			e.printStackTrace();
		}
		return message;
		
	}
	
	
	/**
	 * Creates an e-mail with an attachment
	 * @param recipient The recipient
	 * @param subject The subject of the e-mail
	 * @param body The body of the e-mail
	 * @param path The path that the attachment has
	 * @return Returns the message with the attachment
	 */
	public Message createEmailWithAtt(String recipient , String subject , String body , String path){
	
		MimeMessage message = new MimeMessage(session);
		
		try{
			
			message.setFrom(new InternetAddress(recipient));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient));
			message.setSubject(subject);
			
			BodyPart messageBodyPart = new MimeBodyPart();
			
			messageBodyPart.setText(body);
			
			Multipart multipart = new MimeMultipart();
			
			multipart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(path);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(path);
			multipart.addBodyPart(messageBodyPart);
			
			message.setContent(multipart);
			message.saveChanges();
			
			System.out.println("Email Created");
			
		}catch(MessagingException e){
			e.printStackTrace();
		}
		return message;
		
	}
	
	
	/**
	 * Handles sending whatever message is passed in it
	 * @param msg The message passed in.
	 */
	public void sendEmail(Message msg){
		
		try {

			Transport tr = session.getTransport("smtp");	// Get Transport object from session		
			tr.connect(smtphost, username, password); // We need to connect
			tr.sendMessage(msg, msg.getAllRecipients()); // Send message

			System.out.println("Message sent");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Adds a cc to an e-mail
	 * @param msg The message you want to add a cc to
	 * @param email The e-mail that of the cc
	 */
	public void addCC(Message msg, String email){
		try {
			msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(email));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
}
