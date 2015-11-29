import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.imap.IMAPFolder;

/**
 * This class demonstrates some pieces of functionality in the console 
 * There is a class that does everything in the GUI (MailGUI)
 * Uncomment whatever needed to check the functionality as said in the comments :
 * @author jetmaxz
 */
public class MailRunner {

		public static void main(String[] args) throws MessagingException, IOException{
			
			MailSession sess = new MailSession();
			sess.startIMAPSession();
			
			
			String username = sess.getName();
			String password = sess.getPassword();
			
			Session session = sess.getSession();
			
			MailRecFun util = new MailRecFun(session,username,password);
			MailSendFun sender = new MailSendFun(session, username, password);
			
		
			//Get all the messages in an array.
			Message msgs2[] =util.getMesssagesFromFolder();
			
			
			//Uncomment the following to :
			//======= CHECK ALL THE MESSAGE TITLES AND CONTENTS AND IF A MESSAGE HAS BEEN SEEN OR NOT==========//
			
			/*
			for(int i = 0; i < msgs2.length ; i++){
				System.out.println(msgs2[i].getSubject());
				System.out.println(util.getMessageContents(msgs2[i]));
				System.out.println(util.getFlagSeen(msgs2[i]));
				System.out.println("========================== END EMAIL =====================");
			}
			*/
			
			//Uncomment the following to :
			//====== GET (SEATCH) THE MESSAGES THAT CONTAIN A SPECIFIC STRING ======//
			/*
			Message filteredMessages [] = util.filterEmails(msgs2, "test");
			for(int i =0; i < filteredMessages.length; i++){
				System.out.println(filteredMessages[i].getSubject());
				System.out.println(util.getMessageContents(filteredMessages[i]));	
				System.out.println("==============END FILTER MESSAGE=========================");
			}
			*/
			
			//Uncomment the following to :
		    //============= SET A FLAG TO AN EMAIL ===================================/
			
			/*
			for(int i =0 ; i < msgs2.length; i++){
				util.setFlag(msgs2[i], "spam" , true); // gives every email a flag spam
				System.out.println(Arrays.toString(msgs2[i].getFlags().getUserFlags()));
			}
			*/
			
			
			
			// In order to check sending , attaching , cc functionality , run the GUI //
			
		}
}


