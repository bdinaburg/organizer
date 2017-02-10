package com.chaglei.organizer;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;

import util.FrameUtil;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

public class ManageUsers extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtCurrentlyLoggedInAs;
	private JTextField txtHostname;
	private JTextField txtPort;
	private JTextField txtDataSchema;
	private JTextField txtAdminSchema;
	private JTextField txtUserName;
	private JTextField txtPassword;
	private JTextField txtDataSchemaNewUser;
	private JTextField txtAdminSchemaNewUser;
	public ManageUsers(LoginCredentials loginCredentials) {
		
		JPanel panelNewUser = new JPanel();
		panelNewUser.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panelExistingUsers = new JPanel();
		panelExistingUsers.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panelExistingConnectionInformation = new JPanel();
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
		
		JLabel lblCurrentlyLoggedInAs = new JLabel("Currently Login:");
		
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
		
		JCheckBox chckbxRead = new JCheckBox("Read");
		chckbxRead.setEnabled(false);
		
		JCheckBox chckbxWrite = new JCheckBox("Write");
		chckbxWrite.setEnabled(false);
		
		JCheckBox chckbxSchemaAdmin = new JCheckBox("Data Schema Admin");
		chckbxSchemaAdmin.setEnabled(false);
		
		JCheckBox chckbxGlobalAdmin = new JCheckBox("Admin ALL Dbs");
		chckbxGlobalAdmin.setEnabled(false);
		
		JLabel lblAdminSchema = new JLabel("Admin Schema:");
		
		txtAdminSchema = new JTextField();
		txtAdminSchema.setEditable(false);
		txtAdminSchema.setText("admin");
		txtAdminSchema.setColumns(10);
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
							.addComponent(chckbxSchemaAdmin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addComponent(lblDataSchema, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtDataSchema, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(chckbxGlobalAdmin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addComponent(lblAdminSchema, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtAdminSchema, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
								.addComponent(lblHostname, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCurrentlyLoggedInAs))
							.addGap(18)
							.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
									.addComponent(txtCurrentlyLoggedInAs, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(chckbxRead, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
									.addComponent(txtHostname, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(chckbxWrite, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
					.addContainerGap(482, Short.MAX_VALUE))
		);
		gl_panelExistingConnectionInformation.setVerticalGroup(
			gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCurrentlyLoggedInAs)
						.addComponent(txtCurrentlyLoggedInAs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxRead))
					.addGap(1)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
									.addGap(3)
									.addComponent(lblHostname))
								.addComponent(txtHostname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addComponent(chckbxWrite))
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGap(3)
							.addComponent(lblPort))
						.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
							.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(chckbxSchemaAdmin)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGap(3)
							.addComponent(lblDataSchema))
						.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.BASELINE)
							.addComponent(txtDataSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(chckbxGlobalAdmin)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelExistingConnectionInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelExistingConnectionInformation.createSequentialGroup()
							.addGap(2)
							.addComponent(lblAdminSchema))
						.addComponent(txtAdminSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(137)
					.addComponent(label)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelExistingConnectionInformation.setLayout(gl_panelExistingConnectionInformation);
		
		JLabel lblNewUsers = new JLabel("New User");
		
		JLabel lblUserName = new JLabel("User Name:");
		
		txtUserName = new JTextField();
		txtUserName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		txtPassword = new JTextField();
		//txtPassword.setText(new String(loginCredentials.getPassword()));
		txtPassword.setColumns(10);
		
		JLabel lblDataSchemaNewUser = new JLabel("Data Schema:");
		
		txtDataSchemaNewUser = new JTextField();
		txtDataSchemaNewUser.setColumns(10);
		
		JLabel lblAdminSchema_1 = new JLabel("Admin Schema:");
		
		txtAdminSchemaNewUser = new JTextField();
		txtAdminSchemaNewUser.setEditable(false);
		txtAdminSchemaNewUser.setColumns(10);
		
		JCheckBox checkBoxReadNewUser = new JCheckBox("Read");
		
		JCheckBox checkBoxWriteNewUser = new JCheckBox("Write");
		
		JCheckBox checkBoxDataSchemaAdminNewUser = new JCheckBox("Data Schema Admin");
		
		JCheckBox checkBoxAdminALLDbsNewUser = new JCheckBox("Admin ALL Dbs");
		checkBoxAdminALLDbsNewUser.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                {
                	txtAdminSchemaNewUser.setText(txtAdminSchema.getText());
                }
                else
                {
                	txtAdminSchemaNewUser.setText("");
                }
                    
            }
        });
		
		JButton btnCreateUser = new JButton("Create User");
		GroupLayout gl_panelNewUser = new GroupLayout(panelNewUser);
		gl_panelNewUser.setHorizontalGroup(
			gl_panelNewUser.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNewUser.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnCreateUser, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_panelNewUser.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(checkBoxReadNewUser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGroup(gl_panelNewUser.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblAdminSchema_1, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtAdminSchemaNewUser))
							.addGroup(gl_panelNewUser.createSequentialGroup()
								.addGap(124)
								.addComponent(lblNewUsers))
							.addGroup(gl_panelNewUser.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblDataSchemaNewUser, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtDataSchemaNewUser))
							.addGroup(gl_panelNewUser.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
									.addComponent(lblUserName)
									.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
								.addGap(45)
								.addGroup(gl_panelNewUser.createParallelGroup(Alignment.TRAILING)
									.addComponent(txtUserName, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
									.addComponent(txtPassword)))
							.addGroup(gl_panelNewUser.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
									.addComponent(checkBoxAdminALLDbsNewUser, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
									.addComponent(checkBoxDataSchemaAdminNewUser, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)))))
					.addContainerGap())
				.addGroup(gl_panelNewUser.createSequentialGroup()
					.addContainerGap()
					.addComponent(checkBoxWriteNewUser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(32))
		);
		gl_panelNewUser.setVerticalGroup(
			gl_panelNewUser.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNewUser.createSequentialGroup()
					.addComponent(lblNewUsers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserName)
						.addComponent(txtUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDataSchemaNewUser)
						.addComponent(txtDataSchemaNewUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panelNewUser.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addGap(9)
							.addComponent(lblAdminSchema_1))
						.addGroup(gl_panelNewUser.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtAdminSchemaNewUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(checkBoxReadNewUser)
					.addGap(3)
					.addComponent(checkBoxWriteNewUser)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(checkBoxDataSchemaAdminNewUser)
					.addGap(3)
					.addComponent(checkBoxAdminALLDbsNewUser)
					.addPreferredGap(ComponentPlacement.RELATED, 327, Short.MAX_VALUE)
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
		
		JList listExistingUsers = new JList();
		scrollPaneExistingUsers.setViewportView(listExistingUsers);
		
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
		
		JList lstRoles = new JList();
		scrollPaneRoles.setViewportView(lstRoles);
		lstRoles.setModel(new AbstractListModel() {
			String[] values = new String[] {"ADMIN_ALL_DATABASES", "LOCAL_DB_ADMIN", "WRITE", "READ"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JList lstSchemas = new JList();
		scrollPaneSchemas.setViewportView(lstSchemas);
		panelExistingUsers.setLayout(gl_panelExistingUsers);
		getContentPane().setLayout(groupLayout);
	}
	
	public static void main (String args[]) {
		LoginCredentials loginCredentials = new LoginCredentials();
		ManageUsers manageUsers = new ManageUsers(loginCredentials);
		FrameUtil.setSize(manageUsers, 1200, 800);
		FrameUtil.centerWindow(manageUsers);
		manageUsers.setVisible(true);
		
	}
}
