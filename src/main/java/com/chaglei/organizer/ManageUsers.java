package com.chaglei.organizer;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mongodb.MongoClient;

import transientPojos.UserDBRoles;
import util.FrameUtil;
import util.MongoDBUtils;

public class ManageUsers extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtCurrentlyLoggedInAs;
	private JTextField txtHostname;
	private JTextField txtPort;
	private JTextField txtDataSchema;
	private JTextField txtAuthSchema;
	private JTextField txtNewUserUserName;
	private JTextField txtNewUserPassword;
	private JComboBox<String> jComboBoxNewUserSchema;
	protected LoginCredentials loginCredentials;
	private JPanel panelExistingUsers;
	private JPanel panelExistingConnectionInformation;
	private JPanel panelNewUser;
	UserDBRoles userDBRolesForLoggedInUser;
	JList<String> lstExistingUsers;
	JList<String> lstSchemas;
	JList<String> lstRoles;
	public ManageUsers(LoginCredentials loginCredentials) {
		this.loginCredentials = loginCredentials;
		buildGUI();
		UserDBRoles userDBRoles = populateCurrentUserFields(panelExistingConnectionInformation);
		populateNewUserFields();
		populateExistingUsers(userDBRoles);
	}
	
	
	
	
	public static void main (String args[]) {
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.doLogin();
		ManageUsers manageUsers = new ManageUsers(loginCredentials);
		FrameUtil.setSize(manageUsers, 1200, 800);
		FrameUtil.centerWindow(manageUsers);
		manageUsers.setVisible(true);
		
	}
	
	protected void createNewUser()
	{
		String strUserName = txtNewUserUserName.getText();
		String strPassword = txtNewUserPassword.getText();
		String strSchema =  String.valueOf(jComboBoxNewUserSchema.getSelectedItem());
		MongoClient mongoClient = loginCredentials.getMongoClient();

		HashSet<String> hashSetOfRoles = new HashSet<String>(10);
		for (Component component : panelNewUser.getComponents()) {
		    if (component instanceof JCheckBox) { 
		    	JCheckBox checkbox = (JCheckBox) component;
		    	if(checkbox.isSelected() == true)
		    	{
		    		hashSetOfRoles.add(checkbox.getText());
		    	}
		    }
		}
		MongoDBUtils.createUser(mongoClient, strSchema, strUserName, strPassword, hashSetOfRoles);
	}

	protected void populateExistingUsers(UserDBRoles userDBRoles)
	{
		((DefaultListModel<String>)lstExistingUsers.getModel()).clear();
		if(userDBRoles.getIsGlobalAdmin() == true);
		{
			Vector<UserDBRoles> vectorUserDBRoles = MongoDBUtils.getRolesForAllUsers(loginCredentials.getMongoClient(), loginCredentials.getAuthSchema());
			/**
			 * populated the list with user names, wow, I can't believe how verbose that is. Seems way overcomplicated
			 */
			HashMap<String, UserDBRoles> hashMapUserNameToUserDBRolesObject = new HashMap<String, UserDBRoles>();
			((DefaultListModel<String>)lstExistingUsers.getModel()).clear();
			
			for(int i = 0; i < vectorUserDBRoles.size(); i++)
			{
				UserDBRoles userDBRolesObject = vectorUserDBRoles.get(i);
				((DefaultListModel<String>)lstExistingUsers.getModel()).addElement(userDBRolesObject.getUser());
				hashMapUserNameToUserDBRolesObject.put(userDBRolesObject.getUser() + (new Integer(i)).toString(), userDBRolesObject);
			}
			
			lstExistingUsers.addListSelectionListener(new ListSelectionListener() {

	            @Override
	            public void valueChanged(ListSelectionEvent arg0) {
	                if (!arg0.getValueIsAdjusting()) {
	                	((DefaultListModel<String>)lstSchemas.getModel()).clear();
	                	((DefaultListModel<String>)lstRoles.getModel()).clear();
	                	UserDBRoles userDBRolesRetrievedFromHashMap = hashMapUserNameToUserDBRolesObject.get(lstExistingUsers.getSelectedValue() + lstExistingUsers.getSelectedIndex());
	                	populateDBsWhenGivenAUser(userDBRolesRetrievedFromHashMap);
	                }
	            }
	        });
		
		}
	}
	
	private void populateDBsWhenGivenAUser(UserDBRoles userDBRoles)
	{
		Set<String> setUserDBs = userDBRoles.getDBs();
		for(String strDB : setUserDBs)
		{
			((DefaultListModel<String>)lstSchemas.getModel()).addElement(strDB);
		}
		
		lstSchemas.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                	populateRolesWhenGivenADB(userDBRoles, lstSchemas.getSelectedValue());
                }
            }
        });
	}
	
	private void populateRolesWhenGivenADB(UserDBRoles userDBRoles, String strDB)
	{
		Set<String> setUserRoles = userDBRoles.getRoles(strDB);
		//((DefaultListModel<String>)lstSchemas.getModel()).clear();
		if(setUserRoles == null)
		{
			return;
		}
    	((DefaultListModel<String>)lstRoles.getModel()).clear();
		for(String strRole : setUserRoles)
		{
			((DefaultListModel<String>)lstRoles.getModel()).addElement(strRole);
		}
	}
	protected void populateNewUserFields()
	{
		jComboBoxNewUserSchema.addItem(txtDataSchema.getText());
		jComboBoxNewUserSchema.addItem(txtAuthSchema.getText());
	}
	
	protected UserDBRoles populateCurrentUserFields(final JPanel userJPanel)
	{
		this.txtCurrentlyLoggedInAs.setText(loginCredentials.getUserName());
		this.txtHostname.setText(loginCredentials.getHostName());
		this.txtPort.setText(loginCredentials.getPort());
		this.txtDataSchema.setText(loginCredentials.getDBSchema());
		this.txtAuthSchema.setText(loginCredentials.getAuthSchema());

		Vector<JCheckBox> vectorOfCheckBoxes = new Vector<JCheckBox>(10);
		for (Component component : userJPanel.getComponents()) {
		    if (component instanceof JCheckBox) { 
		    	vectorOfCheckBoxes.add((JCheckBox)component);
		    }
		}
		
		MongoClient mongoClient = loginCredentials.getMongoClient();
		userDBRolesForLoggedInUser = MongoDBUtils.getRolesForUser(mongoClient, txtAuthSchema.getText(), txtDataSchema.getText(),  txtCurrentlyLoggedInAs.getText());
		String strRolesForWhichSchema = txtDataSchema.getText();
		if(userDBRolesForLoggedInUser != null)
		{
			txtDataSchema.setBackground(Color.GREEN);
		}
		if(userDBRolesForLoggedInUser == null)
		{
			userDBRolesForLoggedInUser = MongoDBUtils.getRolesForUser(mongoClient, txtAuthSchema.getText(), txtAuthSchema.getText(),  txtCurrentlyLoggedInAs.getText());
			strRolesForWhichSchema = txtAuthSchema.getText();
			userDBRolesForLoggedInUser.setIsGlobalAdmin(true);
			txtAuthSchema.setBackground(Color.GREEN);
		}
		
		Set<String> rolesSet = userDBRolesForLoggedInUser.getRoles(strRolesForWhichSchema);
		
		for(String str : rolesSet)
		{
			for(JCheckBox checkbox : vectorOfCheckBoxes)
			{
				if(str.equalsIgnoreCase(checkbox.getText()) == true)
				{
					checkbox.setSelected(true);
				}
			}
		}
		
		return userDBRolesForLoggedInUser;
	}
	
	protected void buildGUI()
	{
		panelNewUser = new JPanel();
		panelNewUser.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		panelExistingUsers = new JPanel();
		panelExistingUsers.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		panelExistingConnectionInformation = new JPanel();
		panelExistingConnectionInformation.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelNewUser, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelExistingUsers, GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE))
				.addComponent(panelExistingConnectionInformation, GroupLayout.DEFAULT_SIZE, 1190, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(panelExistingConnectionInformation, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelExistingUsers, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addComponent(panelNewUser, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE))
					.addGap(0))
		);
		
		txtCurrentlyLoggedInAs = new JTextField();
		txtCurrentlyLoggedInAs.setEditable(false);
		txtCurrentlyLoggedInAs.setText(loginCredentials.getUserName());
		txtCurrentlyLoggedInAs.setColumns(10);
		
		JLabel lblCurrentlyLoggedInAs = new JLabel("Current Login:");
		
		JLabel lblHostname = new JLabel("Hostname:");
		
		txtHostname = new JTextField();
		txtHostname.setEditable(false);
		txtHostname.setText(loginCredentials.getHostName());
		txtHostname.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		
		txtPort = new JTextField();
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setText(loginCredentials.getPort());
		
		JLabel label = new JLabel("Port:");
		
		JLabel lblDataSchema = new JLabel("Data Schema:");
		
		txtDataSchema = new JTextField();
		txtDataSchema.setEditable(false);
		txtDataSchema.setColumns(10);
		txtDataSchema.setText(loginCredentials.getDBSchema());
		
		JCheckBox chkRead = new JCheckBox("read");
		chkRead.setEnabled(false);
		
		JCheckBox chkReadWrite = new JCheckBox("readWrite");
		chkReadWrite.setEnabled(false);
		
		JCheckBox chkUserAdminAnyDatabase = new JCheckBox("userAdminAnyDatabase");
		chkUserAdminAnyDatabase.setEnabled(false);
		
		JCheckBox chkRoot = new JCheckBox("root");
		chkRoot.setEnabled(false);
		
		JLabel lblAuthSchema = new JLabel("Auth Schema:");
		
		txtAuthSchema = new JTextField();
		txtAuthSchema.setEditable(false);
		txtAuthSchema.setText("admin");
		txtAuthSchema.setColumns(10);
		
		JCheckBox chckbxUserAdmin = new JCheckBox("userAdmin");
		chckbxUserAdmin.setEnabled(false);
		
		JCheckBox chckbxRoot = new JCheckBox("dbAdmin");
		chckbxRoot.setEnabled(false);
		
		JCheckBox chckbxDbowner = new JCheckBox("dbOwner");
		chckbxDbowner.setEnabled(false);
		GroupLayout gl_panelExistingConnectionInformation = new GroupLayout(panelExistingConnectionInformation);
		gl_panelExistingConnectionInformation.setHorizontalGroup(
			gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addComponent(lblPort, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(chkUserAdminAnyDatabase, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addComponent(lblDataSchema, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtDataSchema, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(chkRoot, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addComponent(lblAuthSchema, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtAuthSchema, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(chckbxUserAdmin, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
								.addComponent(lblHostname, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCurrentlyLoggedInAs))
							.addGap(18)
							.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
									.addComponent(txtCurrentlyLoggedInAs, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(chkRead, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
									.addComponent(txtHostname, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(chkReadWrite, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
					.addGap(18)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxRoot, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxDbowner, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(263, Short.MAX_VALUE))
		);
		gl_panelExistingConnectionInformation.setVerticalGroup(
			gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCurrentlyLoggedInAs)
						.addComponent(txtCurrentlyLoggedInAs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chkRead)
						.addComponent(chckbxRoot))
					.addGap(1)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
									.addGap(3)
									.addComponent(lblHostname))
								.addComponent(txtHostname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
							.addComponent(chkReadWrite)
							.addComponent(chckbxDbowner)))
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGap(3)
							.addComponent(lblPort))
						.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
							.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(chkUserAdminAnyDatabase)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGap(3)
							.addComponent(lblDataSchema))
						.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
							.addComponent(txtDataSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(chkRoot)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
								.addGap(2)
								.addComponent(lblAuthSchema))
							.addComponent(txtAuthSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(chckbxUserAdmin))
					.addGap(137)
					.addComponent(label)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelExistingConnectionInformation.setLayout(gl_panelExistingConnectionInformation);
		
		JLabel lblNewUsers = new JLabel("Add New User");
		
		JLabel lblUserName = new JLabel("User Name:");
		
		txtNewUserUserName = new JTextField();
		txtNewUserUserName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		txtNewUserPassword = new JTextField();
		//txtPassword.setText(new String(loginCredentials.getPassword()));
		txtNewUserPassword.setColumns(10);
		
		JLabel lblNewUserSchema = new JLabel("Schema:");
		
		jComboBoxNewUserSchema = new JComboBox<String>();
		
		JCheckBox chkNewUserRead = new JCheckBox("read");
		
		JCheckBox chkNewUserUserAdminAnyDatabase = new JCheckBox("userAdminAnyDatabase");
		
		JCheckBox chkNewUserRoot = new JCheckBox("root");
		
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createNewUser();
			}
		});
		
		JCheckBox chkNewUserReadWrite = new JCheckBox("readWrite");
		
		JCheckBox chkUserAdmin = new JCheckBox("userAdmin");
		
		JCheckBox chkNewUserDBAdmin = new JCheckBox("dbAdmin");
		
		JCheckBox chkNewUserDBOwner = new JCheckBox("dbOwner");
		GroupLayout gl_panelNewUser = new GroupLayout(panelNewUser);
		gl_panelNewUser.setHorizontalGroup(
			gl_panelNewUser.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNewUser.createSequentialGroup()
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_panelNewUser.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnCreateUser, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panelNewUser.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(chkNewUserRead, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
									.addGroup(gl_panelNewUser.createSequentialGroup()
										.addGap(124)
										.addComponent(lblNewUsers))
									.addGroup(gl_panelNewUser.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
											.addComponent(chkNewUserRoot, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
											.addComponent(chkNewUserUserAdminAnyDatabase, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)))
									.addGroup(gl_panelNewUser.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
											.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
											.addComponent(lblUserName)
											.addComponent(lblNewUserSchema, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
												.addComponent(txtNewUserUserName, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
												.addComponent(jComboBoxNewUserSchema, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
											.addComponent(txtNewUserPassword, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))))))
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addContainerGap()
							.addComponent(chkNewUserReadWrite, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addContainerGap()
							.addComponent(chkUserAdmin, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addContainerGap()
							.addComponent(chkNewUserDBAdmin, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addContainerGap()
							.addComponent(chkNewUserDBOwner, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)))
					.addGap(26))
		);
		gl_panelNewUser.setVerticalGroup(
			gl_panelNewUser.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNewUser.createSequentialGroup()
					.addComponent(lblNewUsers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserName)
						.addComponent(txtNewUserUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(txtNewUserPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewUserSchema)
						.addComponent(jComboBoxNewUserSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addComponent(chkNewUserRead)
					.addGap(3)
					.addComponent(chkNewUserReadWrite)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chkNewUserUserAdminAnyDatabase)
					.addGap(3)
					.addComponent(chkNewUserRoot)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkUserAdmin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkNewUserDBAdmin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkNewUserDBOwner)
					.addPreferredGap(ComponentPlacement.RELATED, 261, Short.MAX_VALUE)
					.addComponent(btnCreateUser)
					.addContainerGap())
		);
		panelNewUser.setLayout(gl_panelNewUser);
		
		JLabel lblUsers = new JLabel("USERS:");
		
		JLabel lblSchemas = new JLabel("SCHEMAS:");
		
		JLabel lblRoles = new JLabel("ROLES:");
		
		JLabel lblExistingUsers = new JLabel("Existing Users");
		lblExistingUsers.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnUpdateUser = new JButton("Update User");
		
		JButton btnDeleteUser = new JButton("Delete User");
		
		JScrollPane scrollPaneExistingUsers = new JScrollPane();
		
		lstExistingUsers = new JList<String>(new DefaultListModel<String>());
		scrollPaneExistingUsers.setViewportView(lstExistingUsers);
		
		JPanel panelBottomBuffer = new JPanel();
		
		JScrollPane scrollPaneSchemas = new JScrollPane();
		
		JScrollPane scrollPaneRoles = new JScrollPane();
		GroupLayout gl_panelExistingUsers = new GroupLayout(panelExistingUsers);
		gl_panelExistingUsers.setHorizontalGroup(
			gl_panelExistingUsers.createParallelGroup(Alignment.TRAILING)
				.addComponent(panelBottomBuffer, GroupLayout.PREFERRED_SIZE, 856, GroupLayout.PREFERRED_SIZE)
				.addGroup(Alignment.LEADING, gl_panelExistingUsers.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblUsers, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
					.addGap(785))
				.addGroup(gl_panelExistingUsers.createSequentialGroup()
					.addGroup(gl_panelExistingUsers.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExistingUsers.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblExistingUsers, GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panelExistingUsers.createSequentialGroup()
							.addGap(10)
							.addComponent(scrollPaneExistingUsers, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
							.addGap(6)
							.addGroup(gl_panelExistingUsers.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelExistingUsers.createSequentialGroup()
									.addComponent(scrollPaneSchemas, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
									.addGap(6))
								.addGroup(gl_panelExistingUsers.createSequentialGroup()
									.addComponent(lblSchemas, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
									.addGap(327)))
							.addGroup(gl_panelExistingUsers.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRoles, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelExistingUsers.createParallelGroup(Alignment.LEADING, false)
									.addGroup(gl_panelExistingUsers.createSequentialGroup()
										.addGap(15)
										.addComponent(btnUpdateUser, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_panelExistingUsers.createSequentialGroup()
										.addGap(15)
										.addComponent(btnDeleteUser, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
									.addComponent(scrollPaneRoles, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))))
					.addGap(16))
		);
		gl_panelExistingUsers.setVerticalGroup(
			gl_panelExistingUsers.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelExistingUsers.createSequentialGroup()
					.addComponent(lblExistingUsers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelExistingUsers.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsers)
						.addComponent(lblSchemas)
						.addComponent(lblRoles))
					.addGap(11)
					.addGroup(gl_panelExistingUsers.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPaneExistingUsers, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
						.addComponent(scrollPaneSchemas, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
						.addGroup(gl_panelExistingUsers.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPaneRoles, GroupLayout.PREFERRED_SIZE, 435, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
							.addComponent(btnUpdateUser)
							.addGap(6)
							.addComponent(btnDeleteUser)))
					.addGap(6)
					.addComponent(panelBottomBuffer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		lstRoles = new JList<String>(new DefaultListModel<String>());
		scrollPaneRoles.setViewportView(lstRoles);
			
		lstSchemas = new JList<String>(new DefaultListModel<String>());
		scrollPaneSchemas.setViewportView(lstSchemas);
		panelExistingUsers.setLayout(gl_panelExistingUsers);
		getContentPane().setLayout(groupLayout);
	}
}
