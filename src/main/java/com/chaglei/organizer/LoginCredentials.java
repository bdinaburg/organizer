package com.chaglei.organizer;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import util.ConfigData;
import util.FrameUtil;
import util.MongoDBUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.mongodb.MongoClient;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Component;

public class LoginCredentials extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtHostname;
	private JTextField txtPort;
	private JTextField txtDBSchema;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JFrame jFrameParentToClose = null;
	
	static LoginCredentials loginCredentials;
	private JTextField txtAuthSchema;

	public LoginCredentials() {
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
		txtUserName.setText(ConfigData.getUserName());
		txtUserName.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setText(ConfigData.getPassword());
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MongoClient mongoClient = MongoDBUtils.connectToMongoDB(txtUserName.getText(), txtPassword.getPassword(), txtDBSchema.getText(), txtHostname.getText(), txtPort.getText());
				if(MongoDBUtils.checkConnection(mongoClient) == true)
				{
					getLoginCredentials().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					getLoginCredentials().setVisible(false);
					if(jFrameParentToClose != null)
					{
						jFrameParentToClose.dispose();
					}
					jFrameParentToClose = (JFrame) new DocumentOrganizer();
				}
				else
				{
					JOptionPane.showMessageDialog(getLoginCredentials(), "Couldn't retrieve data, either login is wrong or didn't connect.", "Could not retrieve data", JOptionPane.ERROR_MESSAGE);
				}
				if(mongoClient != null)
				{
					mongoClient.close();
				}
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
		txtHostname.setText(ConfigData.getHostName());
		txtHostname.setColumns(10);
		
		JLabel lblPort = new JLabel("port:");
		
		txtPort = new JTextField();
		txtPort.setText(ConfigData.getPort());
		txtPort.setColumns(10);
		
		JLabel lblSchema = new JLabel("data schema:");

		txtAuthSchema = new JTextField();
		txtAuthSchema.setText((String) null);
		txtAuthSchema.setColumns(10);
		
		txtDBSchema = new JTextField();
		txtDBSchema.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				txtAuthSchema.setText(txtDBSchema.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				txtAuthSchema.setText(txtDBSchema.getText());
			}

			public void insertUpdate(DocumentEvent e) {
				txtAuthSchema.setText(txtDBSchema.getText());
			}
		});
		
		txtDBSchema.setText(ConfigData.getSchema());
		txtDBSchema.setColumns(10);
		
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
						.addComponent(txtDBSchema, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(txtDBSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
		txtAuthSchema.setText(ConfigData.getAuthSchema());
	}
	
	protected void doClose() {
		if (this.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE
				|| this.getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE) {
			this.dispose();
		} else {
			this.setVisible(false);
		}
	}
	
	public String getHostName() { return txtHostname.getText(); }
	public String getPort() { return txtPort.getText(); } 
	public String getDBSchema() { return txtDBSchema.getText(); }
	public String getUserName() { return txtUserName.getText(); }
	public char[] getPassword() { return txtPassword.getPassword(); }
	
	
	public static void main(String[] args) {
		loginCredentials = new LoginCredentials();
		loginCredentials.setVisible(true);
		FrameUtil.setSize(loginCredentials, 498, 270);
		FrameUtil.centerWindow(loginCredentials);
		loginCredentials.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
	
	public static LoginCredentials getLoginCredentials()
	{
		return LoginCredentials.loginCredentials;
	}
}
