import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.sun.mail.imap.IMAPFolder;

/**
 * This class handles the sessions.
 * IMAP and SMTP.
 * @author jetmaxz
 *
 */
public class MailSession {

	
	IMAPFolder folder;
	Store store;
	
	String username ;
	String password;	     
	Session session;
	
	
	
	/**
	 * Constructor , just asks for the password.
	 */
	public MailSession(){
		
		this.username = "ioannis.a.angelakos@gmail.com";
		this.folder = null;
		
		JPasswordField pwd = new JPasswordField(10);  
		int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);  
		if(action < 0) {
			JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
			System.exit(0); 
		}
		else{ 
			this.password = new String(pwd.getPassword());  
		}
	}
	
	
	public String getName(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public Session getSession(){
		return this.session;
	}
	
	public void setName(String name){
		this.username = name;
	}
	
	public void setPassword(String pass){
		this.password = pass;
	}

	
	/**
	 * Starts an IMAP session , used for inbox functionality
	 */
	public void startIMAPSession(){
		
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		
		
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		//Step 1.2: Establish a mail session (java.mail.Session)
		this.session = Session.getDefaultInstance(props);
		
		}
	
	
	/**
	 * Starts SMTP session , used for sending functionality.
	 */
		public void startSMTPSession(){
		
			String smtphost = "smtp.gmail.com";
			
			Properties props = System.getProperties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", smtphost);
			props.put("mail.smtp.port", "587");
			
			
			// Set Property with username and password for authentication  
			props.setProperty("mail.user", username);
			props.setProperty("mail.password", password);


			//Step 2: Establish a mail session (java.mail.Session)
			this.session = Session.getDefaultInstance(props);

		}
	}


