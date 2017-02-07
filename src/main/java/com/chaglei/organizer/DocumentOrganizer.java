package com.chaglei.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;

import swingUtil.JTextFieldEnhanced;
import transientPojos.DocTypes.DocType;
import util.FrameUtil;

public class DocumentOrganizer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextFieldEnhanced jtextFieldSearch;
	private JScrollPane scrollPaneForJTable;
	private JTable jTable;
	private JTextFieldEnhanced txtDocumentName;
	private JTextFieldEnhanced txtCreateDate;
	private JTextFieldEnhanced txtDueDate;
	private JTextFieldEnhanced txtAmount;
	private JTextFieldEnhanced txtPaidDate;
	private JTextFieldEnhanced txtUsd;
	private JTextFieldEnhanced txtDescription;
	private JComboBox<?> jcomboDocumentType;
	private JTextArea textAreaPDFText = new JTextArea();
	private JButton jbtnSearch = new JButton("Search");
	public DocumentOrganizer() {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel jpanelAddDocuments = new JPanel();
		JPanel jpanelBrowseDocuments = new JPanel();

		tabbedPane.addTab("Browse Documents", jpanelBrowseDocuments);
		tabbedPane.addTab("Add Documents", jpanelAddDocuments);
		
		JLabel lblDocumentName = new JLabel("Document Name:");
		
		txtDocumentName = new JTextFieldEnhanced();
		txtDocumentName.setColumns(10);
		
		JButton btnFind = new JButton("Find Doc..");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JLabel lblCreateDate = new JLabel("Create Date:");
		
		txtCreateDate = new JTextFieldEnhanced();
		txtCreateDate.setText("mm/dd/yyyy");
		txtCreateDate.setColumns(10);
		
		JLabel lblDueDate = new JLabel("Due Date:");
		
		txtDueDate = new JTextFieldEnhanced();
		txtDueDate.setText("mm/dd/yyyy");
		txtDueDate.setColumns(10);
		
		JLabel lblDollarAmount = new JLabel("Amount:");
		
		txtAmount = new JTextFieldEnhanced();
		txtAmount.setText("0");
		txtAmount.setColumns(10);
		
		JLabel lblPaidDate = new JLabel("Paid Date:");
		
		txtPaidDate = new JTextFieldEnhanced();
		txtPaidDate.setText("mm/dd/yyyy");
		txtPaidDate.setColumns(10);
		
		JLabel lblCurrency = new JLabel("Currency:");
		
		txtUsd = new JTextFieldEnhanced();
		txtUsd.setEditable(false);
		txtUsd.setText("USD");
		txtUsd.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		
		txtDescription = new JTextFieldEnhanced();
		txtDescription.setColumns(10);
		
		JLabel lblDocumentType = new JLabel("Document Type:");
		
		jcomboDocumentType = new JComboBox();
		jcomboDocumentType.setModel(new DefaultComboBoxModel(DocType.values()));
		
		JLabel lblTextContent = new JLabel("PDF Text:");
		
		JScrollPane scrollPanePDFText = new JScrollPane();
		
		JButton btnSave = new JButton("Save Doc");
		GroupLayout gl_jpanelAddDocuments = new GroupLayout(jpanelAddDocuments);
		gl_jpanelAddDocuments.setHorizontalGroup(
			gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPanePDFText, GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
								.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
									.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
										.addComponent(lblDocumentName)
										.addComponent(lblCreateDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDueDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDollarAmount, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDocumentType, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCurrency, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblPaidDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
									.addGap(45)
									.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
										.addComponent(txtAmount)
										.addComponent(txtDescription)
										.addComponent(txtPaidDate)
										.addComponent(txtDueDate)
										.addComponent(txtCreateDate)
										.addComponent(txtDocumentName, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
										.addComponent(jcomboDocumentType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtUsd))
									.addGap(18)
									.addComponent(btnFind, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
							.addGap(8))
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addComponent(lblTextContent, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(914, Short.MAX_VALUE))))
		);
		gl_jpanelAddDocuments.setVerticalGroup(
			gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDocumentName)
						.addComponent(txtDocumentName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnFind))
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGap(14)
							.addComponent(lblCreateDate))
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(txtCreateDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGap(21)
							.addComponent(lblDueDate))
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGap(18)
							.addComponent(txtDueDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(20)
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDollarAmount)
						.addComponent(txtAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtPaidDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPaidDate))
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGap(14)
							.addComponent(txtUsd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGap(18)
							.addComponent(lblCurrency)))
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addGap(14)
							.addComponent(lblDescription))
						.addGroup(gl_jpanelAddDocuments.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(20)
					.addGroup(gl_jpanelAddDocuments.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDocumentType)
						.addComponent(jcomboDocumentType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblTextContent)
					.addGap(12)
					.addComponent(scrollPanePDFText, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
					.addGap(47)
					.addComponent(btnSave)
					.addContainerGap())
		);
		
		
		textAreaPDFText.setColumns(10);
		scrollPanePDFText.setViewportView(textAreaPDFText);
		jpanelAddDocuments.setLayout(gl_jpanelAddDocuments);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JLabel lblSearch = new JLabel("Search: ");
		
		jtextFieldSearch = new JTextFieldEnhanced();
		jtextFieldSearch.setColumns(10);
		
		
		jbtnSearch.setToolTipText("Search");
		
		JPanel jpanelContainerForScrollPane = new JPanel();
		scrollPaneForJTable = new JScrollPane();
		jTable = new JTable();
		jTable.setModel(new TableModel());
		jTable.setFillsViewportHeight(true);
		
		JScrollPane scrollPaneDocDescription = new JScrollPane();
		
		GroupLayout gl_jpanelBrowseDocuments = new GroupLayout(jpanelBrowseDocuments);
		gl_jpanelBrowseDocuments.setHorizontalGroup(
			gl_jpanelBrowseDocuments.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpanelBrowseDocuments.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSearch)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jtextFieldSearch, GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
					.addGap(72)
					.addComponent(jbtnSearch)
					.addContainerGap())
				.addComponent(jpanelContainerForScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
				.addGroup(gl_jpanelBrowseDocuments.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneDocDescription, GroupLayout.DEFAULT_SIZE, 919, Short.MAX_VALUE)
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
					.addComponent(jpanelContainerForScrollPane, GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPaneDocDescription, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
		);
		
		JTextArea txtAreaDescription = new JTextArea();
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
		((TableModel)jTable.getModel()).addDataModelRow("doc name", new Date(), new Date(), "sample data", DocType.BILL_CAR, new BigDecimal(100.00), new Date());
		
		jpanelContainerForScrollPane.setLayout(gl_jpanelContainerForScrollPane);
		jpanelBrowseDocuments.setLayout(gl_jpanelBrowseDocuments);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuOrganizer = new JMenu("Organizer");
		menuOrganizer.setMnemonic('o');
		menuBar.add(menuOrganizer);
		
		JMenuItem menuItemUserLogin = new JMenuItem("User Login");
		menuItemUserLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginCredentials.getLoginCredentials().setVisible(true);
			}
		});
		menuOrganizer.add(menuItemUserLogin);
		
		JMenuItem menuItemUserManagement = new JMenuItem("Manage Users");
		menuOrganizer.add(menuItemUserManagement);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1920/2, 1080/2);
		this.setPreferredSize(new Dimension(1920/2, 1080/2));
		FrameUtil.centerWindow(this);
		this.setVisible(true);

	}

	public static void main(String[] args) {
		new DocumentOrganizer();
	}

}
