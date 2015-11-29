import java.io.IOException;
import java.util.ArrayList;

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
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder;

/**
 * 
 * @author jetmaxz
 * All the functionality related to receiving , flagging and searching
 * is implemented in this class
 * 
 */
public class MailRecFun {

	IMAPFolder folder; 
	Store store;	   
	Session session;   
	String username;
	String password;
	
	
	/**
	 * Constructor  
	 * @param session The session to work on
	 * @param username The user name to work on
	 * @param password The password of the user name
	 */
	public MailRecFun(Session session , String username , String password){
		
		this.session = session;
		this.username = username;
	
		this.password = password;
		this.folder = null;
		this.store = null;
	}
	

	/**
	 * Has the code that usually goes in the finally block
	 * Wrote in a method so I can call that instead of 
	 * writing the same code every time.
	 */
	private void fileClose() {
	
		if (folder != null && folder.isOpen()) { 
			try {
				folder.close(true);
			} catch (MessagingException e) {
				e.printStackTrace();
			} 
		}
		
		if (store != null) { 
			try {
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			} 
		}
		
	}
	
	/**
	 * Contains The code that is needed to open a file.
	 * I need to write this many times so instead I put that
	 * code in a method which I call when needed
	 * 
	 * @throws MessagingException
	 */
	
	
	private void fileOpen() throws MessagingException{
		
		this.store = session.getStore("imaps");
		this.store.connect("imap.googlemail.com",username, password);

		this.folder = (IMAPFolder) store.getFolder("inbox"); 
		
		if(!this.folder.isOpen())
			this.folder.open(Folder.READ_WRITE);
		
	}

	
	/**
	 * Gets the messages from the inbox folder.
	 * @return An Array of Messages.
	 */
	
	public Message[] getMesssagesFromFolder() {
		Message[] toReturn = null;
		try{
			this.fileOpen();
				Message messages [] = folder.getMessages();
				toReturn = messages;
		}catch(MessagingException e){
			e.printStackTrace();
		}
		return toReturn;
	}
	
	/**
	 * Filters an array of messages according to a keyword
	 * @param msgs The array passed in
	 * @param filter The filter used
	 * @return the filtered array of messages
	 */
	
	
	public Message[] filterEmails(Message[] msgs , final String filter){
		
		try{
			SearchTerm searchCondition = new SearchTerm() {
				@Override
				public boolean match(Message msg) {
					try {
							if(msg.getSubject().contains(filter) || getMessageContents(msg).contains(filter)){
								return true;
							}
						} catch (MessagingException | IOException e) {
							e.printStackTrace();
						}
						return false;
					}
				
				};
				System.out.println("ADDED MESSAGE");
				msgs = folder.search(searchCondition);
		}catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return msgs;
		
	}
	
	
	
	/**
	 * Gets the content of a message if it's a TEXT/PLAIN
	 * If it is not just TEXT/PLAIN it returns whatever text it can find 
	 * and displays a message that there is more to that e-mail.
	 * @param msg The message passed in
	 * @return The content of the message body
	 * @throws IOException
	 */
	
	public String getMessageContents(Message msg) throws IOException {
		
		String content = "<Content Cannot be Displayed Fully>";
		try {		
					if(msg.getContentType().contains("TEXT/PLAIN")) {
						content = msg.getContent().toString();
					}
					else{
						Multipart multipart = (Multipart) msg.getContent();
						for (int x = 0; x < multipart.getCount(); x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);
							if(bodyPart.getContentType().contains("TEXT/PLAIN")){
								content = content + "\n" + bodyPart.getContentType();
								content = content + "\n" +bodyPart.getContent().toString();
								}
							}
					}
						
			} catch (MessagingException e) {
				e.printStackTrace();
			}		
		return content;
	}// End of getMessageContents()
	
	
	/**
	 * Checks if an email has been read or not.
	 * @param msg The message to check
	 * @return The String that describes if the message is seen or not.
	 */
	public String getFlagSeen(Message msg){
		String flag = "NOT SEEN";
		try {
			if(msg.getFlags().contains(Flags.Flag.SEEN)){
					flag = "SEEN";
			}
		} catch (MessagingException e) {
				e.printStackTrace();
			}	
		return flag;
	}
	
	
	
	/**
	 * Adds a user flag to a message
	 * @param msg The message 
	 * @param name The name that we want our flag to have
	 * @param trigger Turns the flag on / off
	 */
	
	public void setUserFlag(Message msg , String name , boolean trigger){
		try{
			Flags flag = new Flags(name);
			msg.setFlags(flag, trigger);	
		}catch (MessagingException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Sets the seen flag to true
	 * @param msg The message to run the method on.
	 */
	public void setSeenFlag(Message msg){
		try{
			msg.setFlag(Flags.Flag.SEEN , true);
		}catch(MessagingException e){
			e.printStackTrace();
		}
	}
	

} // End of Class
