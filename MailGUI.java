import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.swing.JFrame;
import java.awt.CardLayout;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;


public class MailGUI {

	private JFrame frame;
	private JTextField createMailSubjectText;
	private JTextField createMailRecipientText;
	private JTextArea createMailBodyText;
	private JTextField createMailCCText;
	private JTextField mainSearchTextField;
	private JTextField flagNameField;
	private JTextField flagFilterField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MailGUI window = new MailGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MailGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		final MailSession sess = new MailSession();
		sess.startIMAPSession();
		
		
		String username = sess.getName();
		String password = sess.getPassword();
		
		Session session = sess.getSession();
		
		final MailRecFun util = new MailRecFun(session,username,password);
		final MailSendFun sender = new MailSendFun(session, username, password);
		
		frame = new JFrame();
		frame.setBounds(300, 300, 650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		final JPanel panelMainMenu = new JPanel();
		frame.getContentPane().add(panelMainMenu, "name_17140779187303");
		panelMainMenu.setLayout(null);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 129, 21);
		panelMainMenu.add(menuBar);
		
		
		//================================================= TOP MENU ==========================================================//
		final Message[] msgs = util.getMesssagesFromFolder();
		
		
		JMenu mnCreate = new JMenu("Create");
		menuBar.add(mnCreate);
		
		final JPanel panelCreateEmail = new JPanel();
		frame.getContentPane().add(panelCreateEmail, "name_17143736665923");
		panelCreateEmail.setLayout(null);
		
		final JPanel panelSearchEmail = new JPanel();
		frame.getContentPane().add(panelSearchEmail, "name_17145127609263");
		panelSearchEmail.setLayout(null);
		
		final JPanel panelFlagShow = new JPanel();
		frame.getContentPane().add(panelFlagShow, "name_29067040545516");
		panelFlagShow.setLayout(null);
		
		
		JMenuItem mntmNewEmail = new JMenuItem("New Email");
		mntmNewEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelMainMenu.setVisible(false);
				panelCreateEmail.setVisible(true);
			}
		});
		mnCreate.add(mntmNewEmail);

		//=================================== SEARCH =========================================== //
		
		mainSearchTextField = new JTextField();
		mainSearchTextField.setBounds(153, 435, 143, 19);
		panelMainMenu.add(mainSearchTextField);
		mainSearchTextField.setColumns(10);

		
		JButton btnMailSearchEmail = new JButton("Search");
		btnMailSearchEmail.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String filter = mainSearchTextField.getText();
				Message[] fmsgs = util.filterEmails(msgs, filter);
				int subjectx = 49;
				int y = 29;
				int width = 391;
				int height = 15;
				int flagx = 49+391;
				int buttonx = 49+391+110;
				
				
				for(final Message msg : fmsgs){
					
					try {
						JLabel subjectLabel = new JLabel();
						JLabel flagLabel = new JLabel();
						JButton open = new JButton("Open");
						
						open.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								
								JFrame content = new JFrame();
								content.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								
								content.setTitle("Content");
								
								content.setBounds(100, 100, 450, 300);
								JPanel contentPane = new JPanel();
								contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
								content.setContentPane(contentPane);
								contentPane.setLayout(new BorderLayout(0, 0));
								
								JLabel lblSubject = new JLabel();
								contentPane.add(lblSubject, BorderLayout.NORTH);
								
								JTextArea textArea = new JTextArea();
								contentPane.add(textArea, BorderLayout.CENTER);
								textArea.setEditable(false);
							
								try {
									lblSubject.setText(msg.getSubject());
								} catch (MessagingException e1) {
									e1.printStackTrace();
								}
								try {
									textArea.setText(util.getMessageContents(msg));
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								
								content.setVisible(true);
								
							}
						});
						
						subjectLabel.setText(msg.getSubject());
						flagLabel.setText(util.getFlagSeen(msg));
						
						subjectLabel.setBounds(subjectx,y+27,width,height);
						flagLabel.setBounds(flagx,y+27, width/2,height);
						open.setBounds(buttonx, y+27, 80, height);
						
						panelSearchEmail.add(subjectLabel);
						panelSearchEmail.add(flagLabel);
						panelSearchEmail.add(open);
						y = y+27;
						
						
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
				
				panelMainMenu.setVisible(false);
				panelSearchEmail.setVisible(true);
				
			}
		});
		btnMailSearchEmail.setBounds(12, 432, 117, 25);
		panelMainMenu.add(btnMailSearchEmail);
		
	//=============================== SETTING FLAGS ================================//
		
		JButton btnAddFlag = new JButton("Add Flag");
		btnAddFlag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String filter = flagFilterField.getText();
				String flagName = flagNameField.getText();
				JLabel subject;
				JLabel flags;
				
				int subjectx = 49;
				int y = 29;
				int width = 391;
				int height = 15;
				int flagx = 49+391;
				int buttonx = 49+391+110;
				
				Message[] filteredMessages = util.filterEmails(msgs, filter);
				for(Message msg : filteredMessages){
					util.setUserFlag(msg, flagName, true);
				}
				
				for(final Message msg : msgs){
					try {
						JLabel subjectLabel = new JLabel();
						JLabel flagLabel = new JLabel();
						JButton open = new JButton("Open");
						
						open.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								
								JFrame content = new JFrame();
								content.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								
								content.setTitle("Content");
								
								content.setBounds(100, 100, 450, 300);
								JPanel contentPane = new JPanel();
								contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
								content.setContentPane(contentPane);
								contentPane.setLayout(new BorderLayout(0, 0));
								
								JLabel lblSubject = new JLabel();
								contentPane.add(lblSubject, BorderLayout.NORTH);
								
								JTextArea textArea = new JTextArea();
								contentPane.add(textArea, BorderLayout.CENTER);
								textArea.setEditable(false);
							
								try {
									lblSubject.setText(msg.getSubject());
								} catch (MessagingException e1) {
									e1.printStackTrace();
								}
								try {
									textArea.setText(util.getMessageContents(msg));
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								
								content.setVisible(true);
								
							}
						});
						
						subjectLabel.setText(msg.getSubject());
						flagLabel.setText(Arrays.toString(msg.getFlags().getUserFlags()));
						
						subjectLabel.setBounds(subjectx,y+27,width,height);
						flagLabel.setBounds(flagx,y+27, width/2,height);
						open.setBounds(buttonx, y+27, 80, height);
						
						panelFlagShow.add(subjectLabel);
						panelFlagShow.add(flagLabel);
						panelFlagShow.add(open);
						y = y+27;
						
						
				}catch (MessagingException e1) {
					e1.printStackTrace();
				}
			}
				panelMainMenu.setVisible(false);
				panelFlagShow.setVisible(true);
			}
		});
		btnAddFlag.setBounds(320, 432, 117, 25);
		panelMainMenu.add(btnAddFlag);
		
		flagNameField = new JTextField();
		flagNameField.setBounds(483, 394, 114, 19);
		panelMainMenu.add(flagNameField);
		flagNameField.setColumns(10);
		
		flagFilterField = new JTextField();
		flagFilterField.setBounds(483, 435, 114, 19);
		panelMainMenu.add(flagFilterField);
		flagFilterField.setColumns(10);
		
		JLabel lblFlagName = new JLabel("Flag Name");
		lblFlagName.setBounds(374, 396, 84, 15);
		panelMainMenu.add(lblFlagName);
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(506, 419, 70, 15);
		panelMainMenu.add(lblFilter);
		
				
		

		//======================================== INBOX =====================================//
		
		int subjectx = 49;
		int y = 29;
		int width = 391;
		int height = 15;
		int flagx = 49+391;
		int buttonx = 49+391+110;
		
		for(final Message msg : msgs){
			try {
				JLabel subjectLabel = new JLabel();
				JLabel flagLabel = new JLabel();
				JButton open = new JButton("Open");
				
				open.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						util.setSeenFlag(msg);
						JFrame content = new JFrame();
						content.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						
						content.setTitle("Content");
						
						content.setBounds(100, 100, 450, 300);
						JPanel contentPane = new JPanel();
						contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
						content.setContentPane(contentPane);
						contentPane.setLayout(new BorderLayout(0, 0));
						
						JLabel lblSubject = new JLabel();
						contentPane.add(lblSubject, BorderLayout.NORTH);
						
						JTextArea textArea = new JTextArea();
						contentPane.add(textArea, BorderLayout.CENTER);
						textArea.setEditable(false);
					
						try {
							lblSubject.setText(msg.getSubject());
						} catch (MessagingException e1) {
							e1.printStackTrace();
						}
						try {
							textArea.setText(util.getMessageContents(msg));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						content.setVisible(true);
						
					}
				});
				
				subjectLabel.setText(msg.getSubject());
				flagLabel.setText(util.getFlagSeen(msg));
				
				subjectLabel.setBounds(subjectx,y+27,width,height);
				flagLabel.setBounds(flagx,y+27, width/2,height);
				open.setBounds(buttonx, y+27, 80, height);
				
				panelMainMenu.add(subjectLabel);
				panelMainMenu.add(flagLabel);
				panelMainMenu.add(open);
				y = y+27;
				
				
			} catch (MessagingException e1) {
				e1.printStackTrace();
			}
		}
		
		
		
		
		
		
		//===================================================== CREATING ====================================================//
		
		createMailSubjectText = new JTextField();
		createMailSubjectText.setBounds(211, 12, 170, 19);
		panelCreateEmail.add(createMailSubjectText);
		createMailSubjectText.setColumns(10);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(91, 14, 70, 15);
		panelCreateEmail.add(lblSubject);
		
		createMailRecipientText = new JTextField();
		createMailRecipientText.setBounds(196, 45, 213, 19);
		panelCreateEmail.add(createMailRecipientText);
		createMailRecipientText.setColumns(10);
		
		JLabel lblRecipient = new JLabel("Recipient");
		lblRecipient.setBounds(91, 47, 70, 15);
		panelCreateEmail.add(lblRecipient);
		
		createMailBodyText = new JTextArea();
		createMailBodyText.setBounds(172, 76, 273, 131);
		panelCreateEmail.add(createMailBodyText);
		createMailBodyText.setColumns(10);
		
		createMailCCText = new JTextField();
		createMailCCText.setBounds(172, 219, 273, 19);
		panelCreateEmail.add(createMailCCText);
		createMailCCText.setColumns(10);
		
		JLabel lblAddCc = new JLabel("Add CC");
		lblAddCc.setBounds(84, 221, 70, 15);
		panelCreateEmail.add(lblAddCc);
		
		JButton btnCreateBackButton = new JButton("Back");
		btnCreateBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelCreateEmail.setVisible(false);
				panelMainMenu.setVisible(true);
			}
		});
		btnCreateBackButton.setBounds(116, 386, 117, 60);
		panelCreateEmail.add(btnCreateBackButton);
		
		
		final JRadioButton rdbtnCreateEmailAttach = new JRadioButton("Attach");
		rdbtnCreateEmailAttach.setBounds(84, 271, 149, 23);
		panelCreateEmail.add(rdbtnCreateEmailAttach);
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnCreateEmailAttach.isSelected()){
					
					JButton open = new JButton("");
					JFileChooser fo = new JFileChooser();
					fo.setCurrentDirectory(new java.io.File("."));
					
					fo.setDialogTitle("Choose a File to attach");
					fo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					
					if(fo.showOpenDialog(open) == JFileChooser.APPROVE_OPTION){
						
					}
				
					sess.startSMTPSession();
					String subject = createMailSubjectText.getText();
					String recipient = createMailRecipientText.getText();
					String body = createMailBodyText.getText();
					String filePath = fo.getSelectedFile().getAbsolutePath();
					String cc = createMailCCText.getText();
					
					Message msg = sender.createEmailWithAtt(recipient, subject, body, filePath);
					
					sender.addCC(msg, cc);
					sender.sendEmail(msg);
					createMailBodyText.setText("");
					createMailRecipientText.setText("");
					createMailSubjectText.setText("");
					createMailCCText.setText("");
					rdbtnCreateEmailAttach.setSelected(false);
				}
				else{
					sess.startSMTPSession();
					String subject = createMailSubjectText.getText();
					String recipient = createMailRecipientText.getText();
					String body = createMailBodyText.getText();
					String cc = createMailCCText.getText();
					
					Message msg = sender.createEmail(recipient, subject , body);
					sender.addCC(msg, cc);
					sender.sendEmail(msg);
					createMailBodyText.setText("");
					createMailRecipientText.setText("");
					createMailSubjectText.setText("");
					createMailCCText.setText("");
				}	
			}
		});
		btnSend.setBounds(399, 386, 117, 60);
		panelCreateEmail.add(btnSend);
		
		//========================================= END CREATE =========================================//
		
		
		//========================================= SEARCH EMAIL ========================================//
		
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelSearchEmail.setVisible(false);
				panelMainMenu.setVisible(true);
			}
		});
		btnBack.setBounds(12, 432, 117, 25);
		panelSearchEmail.add(btnBack);
		
		JLabel lblSearchResults = new JLabel("Search Results :");
		lblSearchResults.setBounds(26, 12, 143, 15);
		panelSearchEmail.add(lblSearchResults);
		
		
		//========================================== USER FLAGS PANEL =====================================//
		
		
		JButton btnFlagBack = new JButton("Back");
		btnFlagBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelFlagShow.setVisible(false);
				panelMainMenu.setVisible(true);
			}
		});
		btnFlagBack.setBounds(30, 432, 117, 25);
		panelFlagShow.add(btnFlagBack);
	}
}
