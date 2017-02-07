package com.chaglei.organizer;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

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
import java.awt.Component;

public class LoginCredentials extends JFrame {
	static LoginCredentials loginCredentials;
	protected JFrame jFrameParentToClose = null;
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
		txtUserName.setText("admin");
		txtUserName.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		
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
		txtHostname.setText("chaglei.com");
		txtHostname.setColumns(10);
		
		JLabel lblPort = new JLabel("port:");
		
		txtPort = new JTextField();
		txtPort.setText("27017");
		txtPort.setColumns(10);
		
		JLabel lblSchema = new JLabel("schema:");
		
		txtDBSchema = new JTextField();
		txtDBSchema.setText("admin");
		txtDBSchema.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPort, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHostname, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(txtHostname, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(69)
					.addComponent(btnQuit, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
					.addComponent(btnLogin)
					.addGap(63))
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSchema, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txtDBSchema, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(txtUserName, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
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
					.addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin)
						.addComponent(btnQuit))
					.addGap(26))
		);
		gl_panel.linkSize(SwingConstants.HORIZONTAL, new Component[] {txtUserName, txtPassword, txtHostname, txtPort, txtDBSchema});
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}
	protected void doClose()
	{
		if(this.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE || this.getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE)
		{
			this.dispose();
		}
		else
		{
			this.setVisible(false);
		}
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JTextField txtHostname;
	private JTextField txtPort;
	private JTextField txtDBSchema;

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
