package com.chaglei.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.mongodb.MongoClient;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import swingUtil.JTextFieldEnhanced;
import util.FileReadingUtils;
import util.FrameUtil;
import util.MongoDBUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InsertDocuments extends JFrame {
	private static final long serialVersionUID = 1L;
	protected String strDBSchema = null;
	MongoClient mongoClient = null;
	JTextFieldEnhanced txtDocumentName = new JTextFieldEnhanced();
	JTextFieldEnhanced txtCreateDate = new JTextFieldEnhanced();
	JTextFieldEnhanced txtDueDate = new JTextFieldEnhanced();
	JTextFieldEnhanced txtAmount = new JTextFieldEnhanced();
	JTextFieldEnhanced txtPaidDate = new JTextFieldEnhanced();
	JTextFieldEnhanced txtCurrency = new JTextFieldEnhanced();
	JTextFieldEnhanced txtDescription = new JTextFieldEnhanced();
	JComboBox<DocumentType> comboBoxDocumentTypes = new JComboBox<DocumentType>();
	JTextArea textAreaPDFText = new JTextArea();
	
	public InsertDocuments(MongoClient mongoClient, String schema) {
		this.strDBSchema = schema;
		this.mongoClient = mongoClient;
		buildGUI();
		populateDocTypes();
		populateDates();
		setSize((int)(1920/1.8), 1080/2);
		setPreferredSize(new Dimension((int)(1920/1.8), 1080/2));
		FrameUtil.centerWindow(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void buildGUI()
	{
		setTitle("Insert Document");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelMainPanel = new JPanel();
		getContentPane().add(panelMainPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblDocumentName = new JLabel("Document Name:");
		
		JLabel lblCreateDate = new JLabel("Create Date:");
		
		JLabel lblDueDate = new JLabel("Due Date:");
		
		JLabel lblAmount = new JLabel("Amount:");
		
		JLabel lblDescription = new JLabel("Description:");
		
		JLabel lblDocumentType = new JLabel("Document Type:");
		
		JLabel lblCurrency = new JLabel("Currency:");
		
		JLabel lblPaidDate = new JLabel("Paid Date:");
		
		txtAmount.setText("0");
		txtAmount.setColumns(10);
		
		txtDescription.setColumns(10);
		
		txtPaidDate.setText("mm/dd/yyyy");
		txtPaidDate.setColumns(10);
		
		txtDueDate.setText("mm/dd/yyyy");
		txtDueDate.setColumns(10);
		
		txtCreateDate.setText("mm/dd/yyyy");
		txtCreateDate.setColumns(10);
		
		txtDocumentName.setColumns(10);
		
		txtCurrency.setText("USD");
		txtCurrency.setEditable(false);
		txtCurrency.setColumns(10);
		
		JButton btnFindDoc = new JButton("Find Doc..");
		btnFindDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String strFile = chosenFile();
				txtDocumentName.setText(strFile);
				
			}
		});
		
		JLabel lblPDFText = new JLabel("PDF Text:");
		
		JButton button = new JButton("Save Doc");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				insertDocument();
			}
		});
		GroupLayout gl_panelMainPanel = new GroupLayout(panelMainPanel);
		gl_panelMainPanel.setHorizontalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDocumentName)
						.addComponent(lblCreateDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDueDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAmount, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPaidDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCurrency, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDocumentType, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtDescription, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
						.addComponent(txtAmount, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
						.addComponent(txtDocumentName, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
						.addComponent(txtCreateDate, 678, 678, Short.MAX_VALUE)
						.addComponent(txtDueDate, 678, 678, Short.MAX_VALUE)
						.addComponent(txtPaidDate, 678, 678, Short.MAX_VALUE)
						.addComponent(txtCurrency, 678, 678, Short.MAX_VALUE)
						.addComponent(comboBoxDocumentTypes, 0, 678, Short.MAX_VALUE))
					.addGap(44)
					.addComponent(btnFindDoc, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addGap(46))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPDFText, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(958, Short.MAX_VALUE))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap(907, Short.MAX_VALUE)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addGap(45))
		);
		gl_panelMainPanel.setVerticalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtDocumentName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnFindDoc))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(txtCreateDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtDueDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(txtAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtPaidDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPaidDate)))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addComponent(lblDocumentName)
							.addGap(24)
							.addComponent(lblCreateDate)
							.addGap(25)
							.addComponent(lblDueDate)
							.addGap(18)
							.addComponent(lblAmount)))
					.addGap(14)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtCurrency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCurrency))
					.addGap(18)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDescription))
					.addGap(13)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBoxDocumentTypes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDocumentType))
					.addGap(11)
					.addComponent(lblPDFText)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
					.addGap(28)
					.addComponent(button)
					.addContainerGap())
		);
		
		scrollPane.setViewportView(textAreaPDFText);
		panelMainPanel.setLayout(gl_panelMainPanel);		
	}
	
	private void populateDocTypes()
	{
		List<DocumentType> listDocTypes = MongoDBUtils.getDocTypes(mongoClient, strDBSchema);
		for(DocumentType docTypes : listDocTypes)
		{
			this.comboBoxDocumentTypes.addItem(docTypes);
		}

	}
	
	private void populateDates()
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		String strDate = dateFormat.format(cal.getTime());
		this.txtCreateDate.setText(strDate);
		this.txtDueDate.setText(strDate);
		this.txtPaidDate.setText(strDate);
	}
	
	private String chosenFile()
	{
		File selectedFile = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    selectedFile = fileChooser.getSelectedFile();
		}
		if(selectedFile == null)
		{
			return "";
		}
		else
		{
			return selectedFile.getAbsolutePath();
		}
	}
	
	private boolean insertDocument()
	{
		String strFile = this.txtDocumentName.getText();
		if(FileReadingUtils.doesFileExist(strFile) == false)
		{
			JOptionPane.showMessageDialog(this, "The file you specified " + strFile + " doesn't exist");
		}
		
		List<ScannedFiles> listScannedFiles = MongoDBUtils.doesScannedFileExist(mongoClient, strDBSchema, this.txtDocumentName.getText());
		
		
		
		if(listScannedFiles != null)
		{
			int dialogButton = JOptionPane.YES_NO_OPTION;
			dialogButton = JOptionPane.showConfirmDialog (null, "Document already exists, delete?","WARNING", dialogButton);
            if(dialogButton == JOptionPane.YES_OPTION) 
            {
            	MongoDBUtils.deleteScannedFiles(mongoClient, strDBSchema, listScannedFiles);
            	//listScannedFiles.
            }
            if(dialogButton == JOptionPane.NO_OPTION) 
            {
                  return false;
            }
		}
		
		DocumentType documentType;
		documentType = (DocumentType)(this.comboBoxDocumentTypes.getSelectedItem());
		Documents document = MongoDBUtils.saveDocument(	this.txtDocumentName.getText(), this.txtCreateDate.getText(), this.txtDueDate.getText(), 
									txtAmount.getText(), txtPaidDate.getText(), txtCurrency.getText(), textAreaPDFText.getText(), 
									documentType, mongoClient, strDBSchema);
		
		if(document == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static void main(String... args)
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.doLogin();
		new InsertDocuments(loginCredentials.getMongoClient(), loginCredentials.getDBSchema());
	}
}
