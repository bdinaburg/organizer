package com.chaglei.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.ComboBoxModel;
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
import transientPojos.DocTypes;
import transientPojos.DocTypes.DocType;
import transientPojos.ExifData;
import util.ConfigData;
import util.ExifUtils;
import util.FileReadingUtils;
import util.FrameUtil;
import util.MongoDBUtils;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

public class UpdateDocument extends JFrame {
	private static final long serialVersionUID = 1L;
	protected String strDBSchema = null;
	MongoClient mongoClient = null;
	JButton btnFindDoc = new JButton("Find Doc..");
	JButton btnSaveDoc = new JButton("Save Doc");
	
	JLabel lblDocumentName = new JLabel("Document Name:");
	JLabel lblDocumentType = new JLabel("Document Type:");
	JLabel lblCreateDateb = new JLabel("Create Date:");
	JLabel lblDueDate = new JLabel("Due Date:");
	JLabel lblAmount = new JLabel("Amount:");
	JLabel lblDescription = new JLabel("Description:");
	JLabel lblCurrency = new JLabel("Currency:");
	JLabel lblPaidDate = new JLabel("Paid Date:");
	JLabel lblPDFText = new JLabel("PDF Text / Description:");
	JLabel lblIMGLocation = new JLabel("Location");
	
	JTextFieldEnhanced txtDocumentName = new JTextFieldEnhanced();
	JTextFieldEnhanced txtCreateDate = new JTextFieldEnhanced();
	JTextFieldEnhanced txtDueDate = new JTextFieldEnhanced();
	JTextFieldEnhanced txtAmount = new JTextFieldEnhanced();
	JTextFieldEnhanced txtPaidDate = new JTextFieldEnhanced();
	JTextFieldEnhanced txtCurrency = new JTextFieldEnhanced();
	JTextFieldEnhanced txtDescription = new JTextFieldEnhanced();
	JTextFieldEnhanced txtIMGLocation = new JTextFieldEnhanced();
	JComboBox<DocumentType> comboBoxDocumentTypes = new JComboBox<DocumentType>();
	JTextArea textAreaPDFText = new JTextArea();
	
	public UpdateDocument(MongoClient mongoClient, String schema) {
		this.strDBSchema = schema;
		this.mongoClient = mongoClient;
		buildGUI("Update Document");
		populateDocTypes();
		populateFields();
		setSize((int)(1920/1.8), 1080/2);
		setPreferredSize(new Dimension((int)(1920/1.8), 1080/2));
		FrameUtil.centerWindow(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@SuppressWarnings("serial")
	private void buildGUI(String strTitle)
	{
		setTitle(strTitle);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelMainPanel = new JPanel();
		getContentPane().add(panelMainPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		

		
		/**
		 * These are currently not being used as the textAreaPDFText has replaced these
		 * may will do something with these later
		 */
		lblDescription.setVisible(false);
		txtDescription.setVisible(false);
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
	
		btnFindDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String strFile = chosenFile();
				txtDocumentName.setText(strFile);
				setComboBoxValue();
			}
		});
		

		

		btnSaveDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				insertDocument();
			}
		});
		
		/**
		 * ItemChangeListener is an internal class. Listens for changes and adjusts the GUI 
		 * accordingly as not all input fields make sense for every type of document
		 */
		comboBoxDocumentTypes.addItemListener(new ItemChangeListener());
		
		
		GroupLayout gl_panelMainPanel = new GroupLayout(panelMainPanel);
		gl_panelMainPanel.setHorizontalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap(909, Short.MAX_VALUE)
					.addComponent(btnSaveDoc, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addGap(45))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPDFText, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(853, Short.MAX_VALUE))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGap(8)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMainPanel.createSequentialGroup()
									.addComponent(lblCreateDateb, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
									.addGap(92)
									.addComponent(txtCreateDate, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
								.addGroup(gl_panelMainPanel.createSequentialGroup()
									.addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
									.addGap(92)
									.addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, 679, GroupLayout.PREFERRED_SIZE)))
							.addGap(12)))
					.addGap(175))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDocumentName)
						.addComponent(lblDueDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblIMGLocation, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPaidDate, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCurrency, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDocumentType, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAmount, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtAmount, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
						.addComponent(txtDocumentName, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
						.addComponent(txtDueDate, 678, 678, Short.MAX_VALUE)
						.addComponent(txtIMGLocation, 678, 678, Short.MAX_VALUE)
						.addComponent(txtPaidDate, 678, 678, Short.MAX_VALUE)
						.addComponent(txtCurrency, 678, 678, Short.MAX_VALUE)
						.addComponent(comboBoxDocumentTypes, 678, 678, Short.MAX_VALUE))
					.addGap(44)
					.addComponent(btnFindDoc, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addGap(46))
		);
		gl_panelMainPanel
				.setVerticalGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMainPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panelMainPanel.createSequentialGroup()
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblDocumentName)
														.addComponent(txtDocumentName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(btnFindDoc))
												.addGap(20)
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblDocumentType)
														.addComponent(comboBoxDocumentTypes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGap(20)
												/**
												 * this magic stuff here makes due date and image location be placed on top of each other
												 * since they will never both be visible at the same time. Designed so that when user
												 * selects a different type of document he gets proper entry fields.
												 */
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblDueDate)
														.addComponent(txtDueDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblIMGLocation)
														.addComponent(txtIMGLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
												.addGap(20)
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblAmount)
														.addComponent(txtAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGap(20)
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblPaidDate)
														.addComponent(txtPaidDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGap(20)
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblCurrency)
														.addComponent(txtCurrency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGap(20)
												.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblCreateDateb)
														.addComponent(txtCreateDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))												
														
														))
								.addGap(10)
								.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(txtDescription, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDescription))
								.addGap(20).addComponent(lblPDFText).addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE).addGap(28)
								.addComponent(btnSaveDoc).addContainerGap()));
		
		scrollPane.setViewportView(textAreaPDFText);
		panelMainPanel.setLayout(gl_panelMainPanel);
		
		txtDocumentName.setDropTarget(new DropTarget() {
		    public synchronized void drop(DropTargetDropEvent evt) {
		        try {
		            evt.acceptDrop(DnDConstants.ACTION_COPY);
		            Object obj = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
		            if(obj instanceof List<?>)
		            {
			            List<?> droppedFiles = (List<?>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			            for (Object potentialFile : droppedFiles) 
			            {
			            	if(potentialFile instanceof File)
			            	{
			            		File theFile = (File)potentialFile;
			            		if(theFile.isDirectory() == true)
			            		{
			            			return; //idiot proofing
			            		}
			            		
			            		ConfigData.setLastFolder((theFile).getParentFile().getAbsolutePath());
			            		txtDocumentName.setText((theFile).getAbsolutePath());
			            		setComboBoxValue();
			            	}
			                
			            }
		            }

		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		});
	}
	
	private void populateDocTypes()
	{
		List<DocumentType> listDocTypes = MongoDBUtils.getDocTypes(mongoClient, strDBSchema);
		for(DocumentType docTypes : listDocTypes)
		{
			this.comboBoxDocumentTypes.addItem(docTypes);
		}

	}
	
	private void populateFields()
	{
		String theFormat = ConfigData.getInsertDateFormat();
		DateFormat dateFormat = new SimpleDateFormat(theFormat);
		Calendar cal = Calendar.getInstance();
		String strDate = dateFormat.format(cal.getTime());
		this.txtCreateDate.setText(strDate);
		this.txtDueDate.setText(strDate);
		this.txtPaidDate.setText(strDate);
		this.txtDueDate.setEnabled(true);
		this.txtPaidDate.setEnabled(true);
	}
	
	private String chosenFile()
	{
		File selectedFile = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(ConfigData.getLastFolder()));
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
			ConfigData.setLastFolder(selectedFile.getParentFile().getAbsolutePath());
			return selectedFile.getAbsolutePath();
		}
	}
	
	private void setComboBoxValue()
	{
		if(txtDocumentName == null || txtDocumentName.getText().length() < 4)
		{
			return;  //there is no valid value in the text box;
		}
		String strExtention = txtDocumentName.getText().substring(txtDocumentName.getText().length() - 3, txtDocumentName.getText().length());
		ComboBoxModel<DocumentType> comboBoxModel = comboBoxDocumentTypes.getModel();
		int size = comboBoxModel.getSize();
		for(int i = 0; i < size; i++)
		{
			if(strExtention.equalsIgnoreCase("jpg") || strExtention.equalsIgnoreCase("png") || strExtention.equalsIgnoreCase("gif"))
			{
				DocumentType documentType = comboBoxModel.getElementAt(i);
				if(documentType.toString().indexOf("PERSONAL") >= 0 && documentType.toString().indexOf("PHOTO") >= 0)
				{
					comboBoxDocumentTypes.setSelectedIndex(i);
					ExifData exifData = ExifUtils.getExifData(txtDocumentName.getText());
					txtIMGLocation.setText(exifData.getStrAddress());
					SimpleDateFormat dt1 = new SimpleDateFormat(ConfigData.getInsertDateFormat());
					txtCreateDate.setText(dt1.format(exifData.getCreateDate()));
					textAreaPDFText.setText(exifData.toString());
				}
			}

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
		Documents document = MongoDBUtils.saveDocument(	this.txtDocumentName.getText(), this.txtCreateDate.getText(),  this.txtDueDate.getText(), this.txtIMGLocation.getText(), 
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
		new UpdateDocument(loginCredentials.getMongoClient(), loginCredentials.getDBSchema());
	}
	
	class ItemChangeListener implements ItemListener 
	{
		@Override
		public void itemStateChanged(ItemEvent event) 
		{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				DocumentType documentType = (DocumentType) event.getItem();
				populateFields();
				String strDocSelected = documentType.toString();
				final String PERSONAL_PHOTO = DocumentType.docTypeToString(DocType.PHOTO_PERSONAL);
				if (strDocSelected.equalsIgnoreCase(PERSONAL_PHOTO) == true) {
					txtDueDate.setText("");
					txtPaidDate.setText("");
					txtDueDate.setEnabled(false);
					txtDueDate.setVisible(false);
					txtPaidDate.setEnabled(false);
					lblDueDate.setVisible(false);
					lblIMGLocation.setVisible(true);
					txtIMGLocation.setVisible(true);
				}
				else
				{
					txtDueDate.setEnabled(true);
					txtDueDate.setVisible(true);
					txtPaidDate.setEnabled(true);
					lblIMGLocation.setVisible(false);
					txtIMGLocation.setVisible(false);
					lblDueDate.setVisible(true);
				}
				// do something with object
			}
		}
	}
}
