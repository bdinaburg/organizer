package com.chaglei.organizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mongodb.MongoClient;

import util.ConfigData;
import util.FrameUtil;
import util.MongoDBUtils;

public class LoginCredentials extends JFrame {

	final String COLLECTION_TO_COUNT = "document_type";
	private static final long serialVersionUID = 1L;
	private JTextField txtHostname;
	private JTextField txtPort;
	private JTextField txtDBDataSchema;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JFrame jFrameParentToClose = null;
	protected boolean isLoggedIn = false;
	protected MongoClient mongoClient = null;
	
	static LoginCredentials loginCredentials;
	private JTextField txtAuthSchema;

	public LoginCredentials() {
		buildGUI();
		addCloseListener();
		populateTextFields();
		setVisible(true);
		FrameUtil.setSize(this, Integer.parseInt(ConfigData.getLoginCredentialsWidth()), Integer.parseInt(ConfigData.getLoginCredentialsHeight()));
		FrameUtil.centerWindow(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	protected void doClose() {
		if (this.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE || this.getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE) 
		{
			this.dispose();
		} 
		else 
		{
			this.setVisible(false);
		}
	}
	
	public DocumentOrganizer doLogin()
	{
		mongoClient = MongoDBUtils.connectToMongoDB(txtUserName.getText(), txtPassword.getPassword(), txtAuthSchema.getText(), txtHostname.getText(), txtPort.getText());
		if(MongoDBUtils.checkConnection(mongoClient, this.getDBSchema(), COLLECTION_TO_COUNT) == true)
		{
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setVisible(false);
			if(jFrameParentToClose != null)
			{
				jFrameParentToClose.dispose();
			}
			jFrameParentToClose = (JFrame) new DocumentOrganizer();
		}
		else
		{
			JOptionPane.showMessageDialog(getLoginCredentials(), "Warning: Couldn't retrieve data, new schema?", "Could not retrieve data", JOptionPane.ERROR_MESSAGE);
		}
		return (DocumentOrganizer)jFrameParentToClose;
	}
	
	
	protected void populateTextFields()
	{
		txtPassword.setText(ConfigData.getPassword());
		txtUserName.setText(ConfigData.getUserName());
		txtHostname.setText(ConfigData.getHostName());
		txtPort.setText(ConfigData.getPort());
		txtDBDataSchema.setText(ConfigData.getSchema());
		txtAuthSchema.setText(ConfigData.getAuthSchema());
		
	}
	
	public String getHostName() { return txtHostname.getText(); }
	public String getPort() { return txtPort.getText(); } 
	public String getDBSchema() { return txtDBDataSchema.getText(); }
	public String getUserName() { return txtUserName.getText(); }
	public char[] getPassword() { return txtPassword.getPassword(); }
	public String getAuthSchema() { return this.txtAuthSchema.getText(); }
	public MongoClient getMongoClient() { return mongoClient; } //need to invoke doLogin() before this works
	
	public static void main(String[] args) 
	{
		loginCredentials = new LoginCredentials();
	}
	
	public static LoginCredentials getLoginCredentials()
	{
		return LoginCredentials.loginCredentials;
	}
	
	private void addCloseListener()
	{
		this.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent we) {
				if(mongoClient != null)
				{
					mongoClient.close();
				}
	         }
	     }
	);
	}
	
	protected void buildGUI()
	{
		setTitle("Database Login Credentials");
		setResizable(false);
		setAlwaysOnTop(true);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
		);
		
		JLabel lblUsername = new JLabel("username:");
		
		JLabel lblPassword = new JLabel("password:");
		
		txtUserName = new JTextField();
		txtUserName.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doLogin();
			}
		});
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doClose();
			}
		});
		
		JLabel lblHostname = new JLabel("hostname:");
		
		txtHostname = new JTextField();
		txtHostname.setColumns(10);
		
		JLabel lblPort = new JLabel("port:");
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		
		JLabel lblSchema = new JLabel("data schema:");

		txtAuthSchema = new JTextField();
		txtAuthSchema.setText((String) null);
		txtAuthSchema.setColumns(10);
		
		txtDBDataSchema = new JTextField();
		txtDBDataSchema.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				txtAuthSchema.setText(txtDBDataSchema.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				txtAuthSchema.setText(txtDBDataSchema.getText());
			}

			public void insertUpdate(DocumentEvent e) {
				txtAuthSchema.setText(txtDBDataSchema.getText());
			}
		});
		
		txtDBDataSchema.setColumns(10);
		
		JLabel lblAuthenticationSchema = new JLabel("authentication schema:");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(69)
					.addComponent(btnQuit, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 364, Short.MAX_VALUE)
					.addComponent(btnLogin)
					.addGap(63))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblAuthenticationSchema, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
									.addGap(35))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
									.addGap(132)))
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(lblSchema, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
								.addGap(124)))
						.addComponent(lblPort, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHostname, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtPort, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
						.addComponent(txtDBDataSchema, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtUserName, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
						.addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
						.addComponent(txtAuthSchema, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtHostname, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtHostname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHostname))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSchema)
						.addComponent(txtDBDataSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(txtUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
							.addComponent(txtAuthSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(16)
							.addComponent(lblAuthenticationSchema)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin)
						.addComponent(btnQuit))
					.addGap(26))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}
}
