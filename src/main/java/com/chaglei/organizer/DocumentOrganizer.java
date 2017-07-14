package com.chaglei.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.chaglei.organizer.jtable.JTableEx;
import com.chaglei.organizer.jtable.TableModel;

import pojos.Documents;
import swingUtil.JTextFieldEnhanced;
import util.ConfigData;
import util.FileReadingUtils;
import util.FileWritingUtils;
import util.FrameUtil;
import util.MongoDBUtils;

public class DocumentOrganizer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextFieldEnhanced jtextFieldSearch;
	private JScrollPane scrollPaneForJTable;
	private JTableEx jTable;
	private JButton jbtnSearch = new JButton("Search");
	JTextArea txtAreaDescription;
	public DocumentOrganizer() {
		
		JPanel jpanelBrowseDocuments = new JPanel();

		
		getContentPane().add(jpanelBrowseDocuments, BorderLayout.CENTER);
		
		JLabel lblSearch = new JLabel("Search: ");
		
		jtextFieldSearch = new JTextFieldEnhanced();
		jtextFieldSearch.setColumns(10);
		jbtnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((TableModel)jTable.getModel()).clearDataModel();
				List<Documents> listOfDocuments = MongoDBUtils.getDocuments(LoginCredentials.getLoginCredentials().getMongoClient(), LoginCredentials.getLoginCredentials().getDBSchema(), jtextFieldSearch.getText());
				((TableModel)jTable.getModel()).populateDataModel(listOfDocuments);
				((TableModel)jTable.getModel()).fireTableDataChanged();

			}
		});
		
		
		jbtnSearch.setToolTipText("Search");
		
		JPanel jpanelContainerForScrollPane = new JPanel();
		scrollPaneForJTable = new JScrollPane();
		configureJTable();
		JScrollPane scrollPaneDocDescription = new JScrollPane();
		
		GroupLayout gl_jpanelBrowseDocuments = new GroupLayout(jpanelBrowseDocuments);
		gl_jpanelBrowseDocuments.setHorizontalGroup(
			gl_jpanelBrowseDocuments.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelBrowseDocuments.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSearch)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jtextFieldSearch, GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
					.addGap(72)
					.addComponent(jbtnSearch)
					.addContainerGap())
				.addComponent(jpanelContainerForScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 944, Short.MAX_VALUE)
				.addGroup(gl_jpanelBrowseDocuments.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneDocDescription, GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_jpanelBrowseDocuments.setVerticalGroup(
			gl_jpanelBrowseDocuments.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelBrowseDocuments.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_jpanelBrowseDocuments.createParallelGroup(Alignment.BASELINE)
						.addComponent(jtextFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSearch)
						.addComponent(jbtnSearch))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jpanelContainerForScrollPane, GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPaneDocDescription, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
		);
		
		txtAreaDescription = new JTextArea();
		scrollPaneDocDescription.setViewportView(txtAreaDescription);
		
		GroupLayout gl_jpanelContainerForScrollPane = new GroupLayout(jpanelContainerForScrollPane);
		gl_jpanelContainerForScrollPane.setHorizontalGroup(
			gl_jpanelContainerForScrollPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelContainerForScrollPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneForJTable, GroupLayout.DEFAULT_SIZE, 919, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_jpanelContainerForScrollPane.setVerticalGroup(
			gl_jpanelContainerForScrollPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelContainerForScrollPane.createSequentialGroup()
					.addGap(5)
					.addComponent(scrollPaneForJTable, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE))
		);
		

		scrollPaneForJTable.add(jTable.getTableHeader());
		scrollPaneForJTable.setViewportView(jTable);

		
		jpanelContainerForScrollPane.setLayout(gl_jpanelContainerForScrollPane);
		jpanelBrowseDocuments.setLayout(gl_jpanelBrowseDocuments);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuOrganizer = new JMenu("Organizer");
		menuOrganizer.setMnemonic('o');
		menuBar.add(menuOrganizer);
		
		JMenuItem menuItemAddDocument = new JMenuItem("Add Document");
		menuItemAddDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new InsertDocuments(LoginCredentials.getLoginCredentials().getMongoClient(), LoginCredentials.getLoginCredentials().getDBSchema());
			}
		});
		
		JMenuItem menuItemUserLogin = new JMenuItem("User Login");
		menuOrganizer.add(menuItemUserLogin);
		menuItemUserLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginCredentials.getLoginCredentials().setVisible(true);
			}
		});
		
		
		menuOrganizer.add(menuItemAddDocument);
		
		JMenuItem menuItemHideShowColumns = new JMenuItem("Hide / Show Columns");
		
		menuItemHideShowColumns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ColumnConfigurator(jTable);
			}
		});
		menuOrganizer.add(menuItemHideShowColumns);
		
		JMenu menuDatabase = new JMenu("Database");
		menuBar.add(menuDatabase);
		
		JMenuItem menuItemUserManagement = new JMenuItem("Manage Users");
		menuDatabase.add(menuItemUserManagement);
		menuItemUserManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ManageUsers(LoginCredentials.getLoginCredentials());
			}
		});
		
		JMenuItem menuItemDatabaseIntegrity = new JMenuItem("Database Integrity Check");
		menuDatabase.add(menuItemDatabaseIntegrity);
		menuItemDatabaseIntegrity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DatabaseDiagnostics(LoginCredentials.getLoginCredentials().getMongoClient(), LoginCredentials.getLoginCredentials().getDBSchema());
			}
		});
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1920/2, 1080/2);
		this.setPreferredSize(new Dimension(1920/2, 1080/2));
		FrameUtil.centerWindow(this);
		
		ColumnConfigurator temp = new ColumnConfigurator(jTable);
		temp.setVisible(false);
		temp = null;
		this.setVisible(true);

	}
	
	private void configureJTable()
	{
		jTable = new JTableEx();
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	int row = jTable.getSelectedRow();
	        	Documents document = ((TableModel)jTable.getModel()).getDocumentAtRow(row);
	        	if(document != null)
	        	{
	        		txtAreaDescription.setText(document.getStrDocumentDescription());	
	        	}
	        	
	        }
	    });
		
		jTable.addMouseListener(new MouseAdapter() {

		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(e.getButton() >= MouseEvent.BUTTON2) //it is middle or beyond button
		    	{
			    	JTable table =(JTable) e.getSource();
			        Point p = e.getPoint();
			        int row = table.rowAtPoint(p);
			        jTable.setRowSelectionInterval(row, row);
			    	OrganizerPopupMenu.getIntance(e.getPoint(), table, LoginCredentials.getLoginCredentials().getMongoClient(), LoginCredentials.getLoginCredentials().getDBSchema());
		    		
		    	}
		    }

			public void mousePressed(MouseEvent me) {
				OrganizerPopupMenu.removeFromView();
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		        	Documents document = ((TableModel)jTable.getModel()).getDocumentAtRow(row);
		        	String strFileName = FileReadingUtils.getCurrentPath();
		        	strFileName = strFileName + "/" + ConfigData.getTempFolder() + "/";
		        	
		        	FileWritingUtils.openScannedDocument(document, strFileName);
		        }
		    }
		});
		jTable.setModel(new TableModel());
		jTable.setFillsViewportHeight(true);

	}

	public static void main(String[] args) 
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		DocumentOrganizer documentOrganizer = loginCredentials.doLogin();
		
		List<Documents> listOfDocuments = MongoDBUtils.getDocuments(loginCredentials.getMongoClient(), loginCredentials.getDBSchema());
		((TableModel)documentOrganizer.jTable.getModel()).populateDataModel(listOfDocuments);
	}

}
