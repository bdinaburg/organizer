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
import javax.swing.JTextField;

public class DatabaseBackup extends JFrame {
	private static final long serialVersionUID = 1L;
	protected String strDBSchema = null;
	MongoClient mongoClient = null;
	JList<Object> jlistScannedFiles = new JList<Object>();
	List<ScannedFiles> listScannedFilesWithoutDocuments;
	JList<Object> jlistDocuments = new JList<Object>();
	List<Documents> listDocumentsWithoutScannedFiles;
	JList<Object> jlistDocTypes = new JList<Object>();
	List<DocumentType> listBrokenDocumentsReference;
	private JTextField txtSchema;
	private JTextFieldEnhanced txtSaveLocation;
	private JTextFieldEnhanced txtTempLocationSave;
	DatabaseBackup thiz = this;
	
	public DatabaseBackup(MongoClient mongoClient, String schema) {
		this.strDBSchema = schema;
		this.mongoClient = mongoClient;
		buildGUI();
		setFolderNames();
		//populateDocTypes();
		//populateDates();
		setSize(new Dimension((int)(1920/1.8), 1080/2));
		setPreferredSize(new Dimension((int)(1920/1.8), 1080/2));
		FrameUtil.centerWindow(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@SuppressWarnings("unchecked")
	private void buildGUI()
	{
		setTitle("Backup Database Schema to Disk");
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelMainPanel = new JPanel();
		getContentPane().add(panelMainPanel);
		
		JScrollPane scrollPaneDocument = new JScrollPane();
		
		JLabel lblDatabase = new JLabel("Data Schema:");
		
		JButton btnDeleteScannedFiles = new JButton("Query Scans");
		btnDeleteScannedFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listScannedFilesWithoutDocuments = MongoDBUtils.getItems(ScannedFiles.class, mongoClient, strDBSchema);
				jlistScannedFiles.setListData(listScannedFilesWithoutDocuments.toArray());
			}
		});
		
		JScrollPane scrollPaneScannedFiles = new JScrollPane();
		
		JLabel lblBrokenLinks = new JLabel("Document Types");
		
		JScrollPane scrollPaneScannedFilesNoLinkBack = new JScrollPane();
		
		scrollPaneScannedFilesNoLinkBack.setViewportView(jlistDocTypes);
		//jlistBrokenDocumentsReference.setCellRenderer(new MissingReferenceRenderer());
		
		
		scrollPaneScannedFiles.setViewportView(jlistScannedFiles);
		
		scrollPaneDocument.setViewportView(jlistDocuments);
		
		JLabel lblDocuments = new JLabel("Documents");
		
		JLabel lblScannedFiles = new JLabel("Scanned Files");
		
		JButton btnQueryDocuments = new JButton("Query Docs");
		btnQueryDocuments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listDocumentsWithoutScannedFiles = MongoDBUtils.getDocuments(mongoClient, strDBSchema);
				jlistDocuments.setListData(listDocumentsWithoutScannedFiles.toArray());
			}
		});
		
		JButton btnQueryDocTypes = new JButton("Query Types");
		btnQueryDocTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listBrokenDocumentsReference = MongoDBUtils.getDocTypes(mongoClient, strDBSchema);
				jlistDocTypes.setListData(listBrokenDocumentsReference.toArray());

			}
		});
		
		txtSchema = new JTextField();
		txtSchema.setEditable(false);
		txtSchema.setBackground(Color.GREEN);
		txtSchema.setColumns(10);
		txtSchema.setText(this.strDBSchema);
		
		JButton btnSaveDocumentsToDisk = new JButton("Save to Disk");
		btnSaveDocumentsToDisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//txtSaveLocationDocs
				int intCount = jlistDocuments.getModel().getSize();
				String saveLocation = txtSaveLocation.getText(); 
				if(intCount == 0)
				{
					JOptionPane.showMessageDialog(thiz, "Nothing to save, try quering documents first");
					return;
				}
				
				if(saveLocation.length() < 2)
				{
					JOptionPane.showMessageDialog(thiz, "Invalid save location.");
					return;
				}
				File fileSaveLocation = new File(saveLocation);
				if(fileSaveLocation.exists() == false || fileSaveLocation.isDirectory() == false)
				{
					if(fileSaveLocation.mkdirs() ==  false)
					{
						JOptionPane.showMessageDialog(thiz, "Invalid save location.");
						return;
					}
				}
				
				try 
				{
					saveLocation = fileSaveLocation.getCanonicalPath();
				} catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(thiz, "Invalid save location.");
					return;
				}

				for(int i = 0; i < intCount; i++)
				{
					Object obj = jlistDocuments.getModel().getElementAt(i);	
					if(obj instanceof Documents)
					{
						Documents document = (Documents)obj;
				    	String strFileName = saveLocation + "/documents/";
				    	String strDocName = document.getStrName().toString().replace("\\", ".");
				    	strDocName = strDocName.replace(":", ".");
				    	strDocName = strDocName.replace("/", ".");
				    	strFileName = strFileName + document.getID().toString() + "." + strDocName + ".txt";
					    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
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
					    String jsonStr = gson.toJson(document);
				    	if(FileReadingUtils.doesFileExist(strFileName) == false)
				    	{
				        	FileReadingUtils.writeTextFile(strFileName, jsonStr);
				    	}
					}

				}
				
				
		    	if (Desktop.isDesktopSupported()) 
		    	{
		    	        
		   	        	try {
							Desktop.getDesktop().open(fileSaveLocation);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(thiz, "Saved " + intCount + " documents.");
		    	}
			}
		});
		
		JButton btnSaveScannedFilesToDisk = new JButton("Save to Disk");
		btnSaveScannedFilesToDisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//txtSaveLocationDocs
				int intCount = jlistScannedFiles.getModel().getSize();
				String saveLocation = txtSaveLocation.getText(); 
				saveLocation = saveLocation + "/scannedFiles/";
				if(intCount == 0)
				{
					JOptionPane.showMessageDialog(thiz, "Nothing to save, try quering scanned files first");
					return;
				}
				
				if(saveLocation.length() < 2)
				{
					JOptionPane.showMessageDialog(thiz, "Invalid save location.");
					return;
				}
				File fileSaveLocation = new File(saveLocation);
				if(fileSaveLocation.exists() == false || fileSaveLocation.isDirectory() == false)
				{
					if(fileSaveLocation.mkdir() ==  false)
					{
						JOptionPane.showMessageDialog(thiz, "Invalid save location.");
						return;
					}
				}
				
				try 
				{
					saveLocation = fileSaveLocation.getCanonicalPath();
				} catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(thiz, "Invalid save location.");
					return;
				}
				
				List<ScannedFiles> scannedFilesVector = new Vector<ScannedFiles>(20);
				for(int i = 0; i < intCount; i++)
				{
					Object obj = jlistScannedFiles.getModel().getElementAt(i);	
					if(obj instanceof ScannedFiles)
					{
						ScannedFiles scannedFile = (ScannedFiles)obj;
						scannedFilesVector.add(scannedFile);

					}
				}
				
				FileWritingUtils.saveScannedFilesToDisk(saveLocation, scannedFilesVector);
				
				
		    	if (Desktop.isDesktopSupported()) 
		    	{
		    	        
		   	        	try {
							Desktop.getDesktop().open(fileSaveLocation);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(thiz, "Saved " + intCount + " scanned files.");
		    	}
			}
		});
		
		JButton btnViewScan = new JButton("View Scan");
		btnViewScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Object> selectedDocuments = jlistScannedFiles.getSelectedValuesList();
				if(selectedDocuments == null || selectedDocuments.size() == 0)
				{
					return;
				}
				
				Object obj = selectedDocuments.get(0);
				if(obj instanceof ScannedFiles)
				{
					String strFileName = FileReadingUtils.getCurrentPath();
			    	strFileName = strFileName + "/";
			    	
			    	
					FileWritingUtils.openScannedFile((ScannedFiles)obj, txtTempLocationSave.getText() + "\\");
					
				}
			}
		});
		
		JButton btnViewDocument = new JButton("View Doc");
		btnViewDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
					FileWritingUtils.openDocument(document, txtTempLocationSave.getText() + "\\");
				}

			}
		});
		
		JButton btnSaveDocTypes = new JButton("Save to Disk");
		btnSaveDocTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//txtSaveLocationDocs
				int intCount = jlistDocTypes.getModel().getSize();
				String saveLocation = txtSaveLocation.getText();
				saveLocation = saveLocation + "/docTypes/";   
				if(intCount == 0)
				{
					JOptionPane.showMessageDialog(thiz, "Nothing to save, try quering documents first");
					return;
				}
				
				if(saveLocation.length() < 2)
				{
					JOptionPane.showMessageDialog(thiz, "Invalid save location.");
					return;
				}
				File fileSaveLocation = new File(saveLocation);
				if(fileSaveLocation.exists() == false || fileSaveLocation.isDirectory() == false)
				{
					if(fileSaveLocation.mkdirs() ==  false)
					{
						JOptionPane.showMessageDialog(thiz, "Invalid save location.");
						return;
					}
				}
				
				try 
				{
					saveLocation = fileSaveLocation.getCanonicalPath();
				} catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(thiz, "Invalid save location.");
					return;
				}

				for(int i = 0; i < intCount; i++)
				{
					Object obj = jlistDocTypes.getModel().getElementAt(i);	
					if(obj instanceof DocumentType)
					{
						DocumentType documentType = (DocumentType)obj;
				    	String strFileName = saveLocation + "/";
				    	String strDocName = documentType.getStrDocument_type().toString();
				    	strFileName = strFileName + documentType.getId().toString() + "." + strDocName + ".txt";
					    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
					    String jsonStr = gson.toJson(documentType);
				    	if(FileReadingUtils.doesFileExist(strFileName) == false)
				    	{
				        	FileReadingUtils.writeTextFile(strFileName, jsonStr);
				    	}
					}

				}
				
				
		    	if (Desktop.isDesktopSupported()) 
		    	{
		    	        
		   	        	try {
							Desktop.getDesktop().open(fileSaveLocation);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(thiz, "Saved " + intCount + " documents.");
		    	}
			}
		});
		
		txtSaveLocation = new JTextFieldEnhanced();
		txtSaveLocation.setColumns(10);
		
		JLabel lblBackupFolder = new JLabel("Backup Folder:");
		
		txtTempLocationSave = new JTextFieldEnhanced();
		txtTempLocationSave.setColumns(10);
		
		JLabel lblTempFolder = new JLabel("Temp Folder:");
		GroupLayout gl_panelMainPanel = new GroupLayout(panelMainPanel);
		gl_panelMainPanel.setHorizontalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDatabase)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtSchema, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPaneScannedFilesNoLinkBack, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblBrokenLinks, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblDocuments, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
								.addComponent(scrollPaneDocument, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnQueryDocuments, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveDocumentsToDisk, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnViewDocument, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(92))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnQueryDocTypes, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveDocTypes, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(229)))
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGap(16)
							.addComponent(lblScannedFiles, GroupLayout.PREFERRED_SIZE, 528, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPaneScannedFiles, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
								.addGroup(gl_panelMainPanel.createSequentialGroup()
									.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnDeleteScannedFiles, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblBackupFolder)
										.addComponent(lblTempFolder, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panelMainPanel.createSequentialGroup()
											.addComponent(btnSaveScannedFilesToDisk, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnViewScan, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, 138, Short.MAX_VALUE))
										.addComponent(txtSaveLocation, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
										.addComponent(txtTempLocationSave, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))))))
					.addGap(5))
		);
		gl_panelMainPanel.setVerticalGroup(
			gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDatabase)
						.addComponent(txtSchema, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblScannedFiles)
						.addComponent(lblDocuments))
					.addGap(11)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneDocument, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
						.addComponent(scrollPaneScannedFiles, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnDeleteScannedFiles)
							.addComponent(btnSaveScannedFilesToDisk)
							.addComponent(btnViewScan))
						.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnQueryDocuments)
							.addComponent(btnSaveDocumentsToDisk)
							.addComponent(btnViewDocument)))
					.addGap(18)
					.addComponent(lblBrokenLinks)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPaneScannedFilesNoLinkBack, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addComponent(txtSaveLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(16)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtTempLocationSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTempFolder)))
						.addComponent(lblBackupFolder))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnQueryDocTypes)
						.addComponent(btnSaveDocTypes))
					.addGap(66))
		);
		panelMainPanel.setLayout(gl_panelMainPanel);
	}
	
	private void setFolderNames()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Calendar cal = Calendar.getInstance();
		String strDate = dateFormat.format(cal.getTime());
		String tempFolder = ConfigData.getTempFolder();
		String backupFolder = "\\backup\\" + strDate;
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		tempFolder = s + "\\" + tempFolder;
		backupFolder = s + backupFolder;

		txtTempLocationSave.setText(tempFolder);
		txtSaveLocation.setText(backupFolder);
	}
	
	private void saveScannedFilesToDisk()
	{
		if(listScannedFilesWithoutDocuments != null && listScannedFilesWithoutDocuments.size() > 0)
		{
			String strPath = txtSaveLocation.getText();
			FileWritingUtils.saveScannedFilesToDisk(strPath, listScannedFilesWithoutDocuments);
		}
	}
	
	private void saveDocumentsToDisk()
	{
		if(listScannedFilesWithoutDocuments != null && listScannedFilesWithoutDocuments.size() > 0)
		{
			String strPath = txtSaveLocation.getText();
			FileWritingUtils.saveDocumentsToDisk(strPath, listDocumentsWithoutScannedFiles);
		}
	}
	
	public static void main(String... args)
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.doLogin();
		new DatabaseBackup(loginCredentials.getMongoClient(), loginCredentials.getDBSchema());
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
