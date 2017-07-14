package com.chaglei.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
import javax.swing.ListCellRenderer;

import com.mongodb.MongoClient;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import swingUtil.JTextFieldEnhanced;
import util.ConfigData;
import util.FileReadingUtils;
import util.FileWritingUtils;
import util.FrameUtil;
import util.MongoDBUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;

public class DatabaseRestore extends JFrame {
	private static final long serialVersionUID = 1L;
	protected String strDBSchema = null;
	MongoClient mongoClient = null;
	JList<ScannedFiles> jlistScannedFiles = new JList<ScannedFiles>();
	List<ScannedFiles> listScannedFiles;
	JList<Object> jlistDocuments = new JList<Object>();
	List<Documents> listDocuments;
	JList<Object> jlistDocTypes = new JList<Object>();
	List<DocumentType> listDocTypes;
	private JTextField txtSchema;
	private JTextFieldEnhanced txtSaveLocation;
	private JTextFieldEnhanced txtTempLocationSave;
	DatabaseRestore thiz = this;
	
	public DatabaseRestore(MongoClient mongoClient, String schema) {
		this.strDBSchema = schema;
		this.mongoClient = mongoClient;
		buildGUI();
		setFolderNames();
		//populateDocTypes();
		//populateDates();
		setSize(new Dimension(1066, 1215));
		setPreferredSize(new Dimension((int)(1920/1.8), 1080/2));
		FrameUtil.centerWindow(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void buildGUI()
	{
		setTitle("Restore From Disk to the Database");
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelMainPanel = new JPanel();
		getContentPane().add(panelMainPanel);
		
		JScrollPane scrollPaneDocument = new JScrollPane();
		
		JLabel lblDatabase = new JLabel("Data Schema:");
		
		
		
		JButton btnPopulateDocs = new JButton("Populate Docs");
		btnPopulateDocs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listDocuments = FileReadingUtils.readDocuments(txtSaveLocation.getText() + "/documents/");
				jlistDocuments.setListData(listDocuments.toArray());

//				listScannedFiles = MongoDBUtils.getItems(ScannedFiles.class, mongoClient, strDBSchema);
//				jlistScannedFiles.setListData(listScannedFiles.toArray());
			}
		});
		
		JScrollPane scrollPaneScannedFiles = new JScrollPane();
		
		JLabel lblBrokenLinks = new JLabel("Document Types");
		
		JScrollPane scrollPaneDocumentTypes = new JScrollPane();
		
		scrollPaneDocumentTypes.setViewportView(jlistDocTypes);
		//jlistBrokenDocumentsReference.setCellRenderer(new MissingReferenceRenderer());
		
		
		scrollPaneScannedFiles.setViewportView(jlistScannedFiles);
		
		scrollPaneDocument.setViewportView(jlistDocuments);
		
		JLabel lblDocuments = new JLabel("Documents");
		
		JLabel lblScannedFiles = new JLabel("Scanned Files");
		
		JButton btnPopulateScannedFiles = new JButton("Populate Scans");
		btnPopulateScannedFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				populateScannedFiles();
			}
		});
		
		JButton btnQueryDocTypes = new JButton("Populate Types");
		btnQueryDocTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String saveLocation = txtSaveLocation.getText();
				saveLocation = saveLocation + "/docTypes/";
				listDocTypes = FileReadingUtils.readDocumentTypes(saveLocation);
				jlistDocTypes.setListData(listDocTypes.toArray());

			}
		});
		
		txtSchema = new JTextField();
		txtSchema.setEditable(false);
		txtSchema.setBackground(Color.GREEN);
		txtSchema.setColumns(10);
		txtSchema.setText(this.strDBSchema);
		
		JButton btnRestoreOneScannedFile = new JButton("Restore One");
		btnRestoreOneScannedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnRestoreOneDocument = new JButton("Restore One");
		btnRestoreOneDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Object> selectedDocuments = jlistDocuments.getSelectedValuesList();
				if(selectedDocuments == null || selectedDocuments.size() == 0)
				{
					return;
				}
				
				Object obj = selectedDocuments.get(0);
				if(obj instanceof Documents)
				{
					restoreOneDocument((Documents) obj);
				}
			}
		});
		
		JButton btnRestoreAllDocuments = new JButton("Restore All");
		btnRestoreAllDocuments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				restoreAllDocuments();
			}
		});
		
		JButton btnRestoreAllScannedFiles = new JButton("Restore All");
		btnRestoreAllScannedFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnRestoreAllDocumentTypes = new JButton("Restore All");
		btnRestoreAllDocumentTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//txtSaveLocationDocs
				int intCount = jlistDocTypes.getModel().getSize();
				if(intCount == 0)
				{
					JOptionPane.showMessageDialog(thiz, "Nothing to save, try loading doctypes first");
					return;
				}
				List<DocumentType> listDocType = new Vector<DocumentType>(10);
				for(int i = 0; i < intCount; i++)
				{
					Object obj = jlistDocTypes.getModel().getElementAt(i);	
					if(obj instanceof DocumentType)
					{
						DocumentType documentType = (DocumentType)obj;
						listDocType.add(documentType);
					}
				}
				
				MongoDBUtils.saveDocumentType(listDocType, mongoClient, strDBSchema);
			}
		});
		
		txtSaveLocation = new JTextFieldEnhanced();
		txtSaveLocation.setColumns(10);
		
		JLabel lblBackupFolder = new JLabel("Backup Folder:");
		
		txtTempLocationSave = new JTextFieldEnhanced();
		txtTempLocationSave.setColumns(10);
		
		JLabel lblTempFolder = new JLabel("Temp Folder:");
		
		JButton btnViewDoc = new JButton("View Doc");
		btnViewDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Object> selectedDocuments = jlistDocuments.getSelectedValuesList();
				if(selectedDocuments == null || selectedDocuments.size() == 0)
				{
					return;
				}
				
				Object obj = selectedDocuments.get(0);
				if(obj instanceof Documents)
				{
					Documents document = (Documents)obj;
					Iterator<ScannedFiles> iter = document.getScannedFiles().iterator();
					while(iter.hasNext())
					{
						ScannedFiles scannedFile = iter.next();
						try {
							scannedFile.setDocument_inbytearray(null);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
					String strFileName = FileReadingUtils.getCurrentPath();
			    	strFileName = strFileName + "/" + ConfigData.getTempFolder() + "/";

					
					FileWritingUtils.openDocument(document, strFileName);
				}


			}
		});
		
		JButton btnViewScan = new JButton("View Scan");
		btnViewScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewScannedFile();
			}
			
		});
		
		JButton btnBrowse = new JButton("Browse");
		
		JButton btnViewScanFromDocument = new JButton("View Scan");
		btnViewScanFromDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewScannedFileFromDocument();
			}
		});
		GroupLayout gl_panelMainPanel = new GroupLayout(panelMainPanel);
		gl_panelMainPanel.setHorizontalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDatabase)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtSchema, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addGap(10)
					.addComponent(lblDocuments, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
					.addGap(549))
				.addGroup(Alignment.LEADING, gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnPopulateDocs, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRestoreOneDocument, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRestoreAllDocuments, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnViewDoc, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnViewScanFromDocument, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(376, Short.MAX_VALUE))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addGap(16)
					.addComponent(lblScannedFiles, GroupLayout.PREFERRED_SIZE, 528, GroupLayout.PREFERRED_SIZE)
					.addGap(506))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnPopulateScannedFiles, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRestoreOneScannedFile, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRestoreAllScannedFiles, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnViewScan, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(507, Short.MAX_VALUE))
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneDocument, GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneScannedFiles, GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneDocumentTypes, GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblBrokenLinks, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
								.addGroup(gl_panelMainPanel.createSequentialGroup()
									.addComponent(btnQueryDocTypes, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnRestoreAllDocumentTypes, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
									.addGap(229)))
							.addGap(519))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblBackupFolder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTempFolder, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtTempLocationSave, GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)
								.addComponent(txtSaveLocation, GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panelMainPanel.setVerticalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDatabase)
						.addComponent(txtSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addComponent(lblDocuments)
					.addGap(11)
					.addComponent(scrollPaneDocument, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPopulateDocs)
						.addComponent(btnRestoreOneDocument)
						.addComponent(btnRestoreAllDocuments)
						.addComponent(btnViewDoc)
						.addComponent(btnViewScanFromDocument))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblScannedFiles)
					.addGap(11)
					.addComponent(scrollPaneScannedFiles, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPopulateScannedFiles)
						.addComponent(btnRestoreOneScannedFile)
						.addComponent(btnRestoreAllScannedFiles)
						.addComponent(btnViewScan))
					.addGap(18)
					.addComponent(lblBrokenLinks)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPaneDocumentTypes, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnQueryDocTypes)
						.addComponent(btnRestoreAllDocumentTypes))
					.addGap(40)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBackupFolder)
						.addComponent(txtSaveLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse))
					.addGap(18)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTempFolder)
						.addComponent(txtTempLocationSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(132))
		);
		panelMainPanel.setLayout(gl_panelMainPanel);
		
		btnPopulateScannedFiles.setVisible(true);
		btnRestoreOneScannedFile.setVisible(false);
		btnRestoreAllScannedFiles.setVisible(false);
	}
	
	private void setFolderNames()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Calendar cal = Calendar.getInstance();
		String strDate = dateFormat.format(cal.getTime());
		String tempFolder = "\\temp\\" + strDate;
		String backupFolder = "\\backup\\" + strDate;
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		tempFolder = s + tempFolder;
		backupFolder = s + backupFolder;

		txtTempLocationSave.setText(tempFolder);
		txtSaveLocation.setText(backupFolder);
	}
	
	/**
	 * This depends on Documents being populated first.
	 * 
	 * Documents contain a link to their scanned files. We will at the document to figure out
	 * which scaned files we need to load.
	 */
	private void populateScannedFiles()
	{
		DefaultListModel<ScannedFiles> listModel = new DefaultListModel<ScannedFiles>();
		
		if(jlistDocuments == null || jlistDocuments.getModel() == null || jlistDocuments.getModel().getSize() < 1)
		{
			return;
		}
	
		for (int i = 0; i < jlistDocuments.getModel().getSize(); i++) {
			Object obj = jlistDocuments.getModel().getElementAt(i);
			if(obj instanceof Documents)
			{
				Documents document = (Documents)obj;
				Iterator<ScannedFiles> iter = document.getScannedFiles().iterator();
				while(iter.hasNext())
				{
					ScannedFiles scannedFile = iter.next();
					try {
						File file = new File(txtSaveLocation.getText() + "\\scannedFiles\\" + scannedFile.getSHA256HashTotal() + "." + scannedFile.getFileType());
						if(file.exists() == true)
						{
							listModel.addElement(scannedFile);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		
		jlistScannedFiles.setModel(listModel);
	}
	
	private void viewScannedFile()
	{
		List<ScannedFiles> selectedScannedFiles = jlistScannedFiles.getSelectedValuesList();
		
		if(selectedScannedFiles == null || selectedScannedFiles.size() == 0)
		{
			return;
		}
		
		Object obj = selectedScannedFiles.get(0);
		if(obj instanceof ScannedFiles)
		{
			ScannedFiles scannedFile = (ScannedFiles)obj;
			File myFile = new File(txtSaveLocation.getText() + "\\scannedFiles\\" + scannedFile.getSHA256HashTotal() + "." + scannedFile.getFileType());
			
	    	if (Desktop.isDesktopSupported()) {
	    	    try {
	    	        String strFileName = myFile.getAbsolutePath();
	    	        if(strFileName.endsWith("pdf") == true)
	    	        {
	    	        	Desktop.getDesktop().open(myFile);	
	    	        }
	    	        else
	    	        {
	    	        	Desktop.getDesktop().edit(myFile);
	    	        }
	    	        
	    	    } catch (IOException ex) {
	    	        // no application registered for PDFs
	    	    }
	    	}

		}

	}
	/**
	 * When they see the documents in a list and they want to view an associated scanned file with the document
	 * use this function. Right now doesn't work with merged files I don't think.
	 */
	private void viewScannedFileFromDocument()
	{
		List<Object> selectedDocuments = jlistDocuments.getSelectedValuesList();
		if(selectedDocuments == null || selectedDocuments.size() == 0)
		{
			return;
		}
		
		Object obj = selectedDocuments.get(0);
		if(obj instanceof Documents)
		{
			Documents document = (Documents)obj;
			String fileName = getScannedFileNameFromDocument(document);
			File myFile = new File(txtSaveLocation.getText() + "\\scannedFiles\\" + fileName);
			
	    	if (Desktop.isDesktopSupported()) {
	    	    try {
	    	        String strFileName = myFile.getAbsolutePath();
	    	        if(strFileName.endsWith("pdf") == true)
	    	        {
	    	        	Desktop.getDesktop().open(myFile);	
	    	        }
	    	        else
	    	        {
	    	        	Desktop.getDesktop().edit(myFile);
	    	        }
	    	        
	    	    } catch (IOException ex) {
	    	        // no application registered for PDFs
	    	    }
	    	}
		}
	}
	
	private boolean restoreOneDocument(Documents document)
	{
		String strFile = this.getScannedFileNameFromDocument(document);
		strFile = txtSaveLocation.getText() + "\\scannedFiles\\" + strFile;
		if(FileReadingUtils.doesFileExist(strFile) == false)
		{
			System.out.println("file not found: " + strFile);
			System.out.println("Document: " + document);
			return false;
		}
		
		List<ScannedFiles> listScannedFiles = MongoDBUtils.doesScannedFileExist(mongoClient, strDBSchema, strFile);
		if(listScannedFiles != null)
		{
			System.out.println("file was already in the database: " + strFile);
			System.out.println("Document: " + document);
		}
		
		DocumentType documentType;
		documentType = document.getDocumentType();
		Documents savedDocument = MongoDBUtils.saveDocument(strFile, document, documentType, mongoClient, strDBSchema);
		
		if(savedDocument == null)
		{
			return false;
		}
		else
		{
			return true;
		}

	}
	
	private void restoreAllDocuments()
	{
		if(jlistDocuments == null || jlistDocuments.getModel() == null || jlistDocuments.getModel().getSize() < 1)
		{
			return;
		}
	
		for (int i = 0; i < jlistDocuments.getModel().getSize(); i++) {
			Object obj = jlistDocuments.getModel().getElementAt(i);
			if(obj instanceof Documents)
			{
				Documents document = (Documents)obj;
				restoreOneDocument(document);
			}
		}
	}
	
	private String getScannedFileNameFromDocument(Documents document)
	{
		Iterator<ScannedFiles> iter = document.getScannedFiles().iterator();
		String fileName = "";			
		/**
		 * fix this later, for now it will only be one file
		 */
		/**
		 * TODO: Fix this to support multiple files
		 */
		while(iter.hasNext())
		{
			ScannedFiles scannedFile = iter.next();
			fileName = scannedFile.getSHA256HashTotal();
			fileName += "." + scannedFile.getFileType();
		}
		return fileName;
	}
	
	public static void main(String... args)
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.setVisible(false);
		loginCredentials.doLogin();
		
		new DatabaseRestore(loginCredentials.getMongoClient(), loginCredentials.getDBSchema());
	}
	
	/**
	 * custom cell renderer required because we get the Documents objects back but the list shows ScannedFiles 
	 * but since Documents to ScannedFiles is a one to many relationship we have to flaten multiple scannedFiles
	 * into a single cell.
	 * @author Boris
	 *
	 */
	@SuppressWarnings("rawtypes")
	class MissingReferenceRenderer implements ListCellRenderer {
		  protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			String strText = "";
			try {

				Documents document;

				if (value instanceof Documents) 
				{
					document = (Documents) value;
					List<ScannedFiles> scannedFiles = document.getScannedFiles();
					strText = "DocID: " + document.getID().toString() + " missing in: ";
					for (ScannedFiles scannedFile : scannedFiles) 
					{
						if (scannedFile.getDocumentsID() == null) 
						{
							strText += scannedFile.getId().toString() + ", ";
						}
					}
				}
				strText = strText.substring(0, strText.length() - 2);
			} 
			catch (Exception anyExc) 
			{
				strText = "";
			}
			renderer.setText(strText);
			return renderer;
		}
	}
}
