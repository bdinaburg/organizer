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
import javax.swing.ListCellRenderer;

import com.mongodb.MongoClient;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import swingUtil.JTextFieldEnhanced;
import util.FileReadingUtils;
import util.FileWritingUtils;
import util.FrameUtil;
import util.MongoDBUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
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

public class DatabaseDiagnostics extends JFrame {
	private static final long serialVersionUID = 1L;
	protected String strDBSchema = null;
	MongoClient mongoClient = null;
	JList<Object> jlistScannedFilesWithoutDocuments = new JList<Object>();
	List<ScannedFiles> listScannedFilesWithoutDocuments;
	JList<Object> jlistDocumentsWithoutScannedFiles = new JList<Object>();
	List<Documents> listDocumentsWithoutScannedFiles;
	JList<Object> jlistBrokenDocumentsReference = new JList<Object>();
	List<Documents> listBrokenDocumentsReference;
	private JTextField txtSchema;
	private JTextFieldEnhanced txtSaveLocationScans;
	private JTextFieldEnhanced txtSaveLocationDocs;
	
	public DatabaseDiagnostics(MongoClient mongoClient, String schema) {
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
		setTitle("Schema Integrity Check");
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelMainPanel = new JPanel();
		getContentPane().add(panelMainPanel);
		
		JScrollPane scrollPaneDocument = new JScrollPane();
		
		JLabel lblDatabase = new JLabel("Data Schema:");
		
		JButton btnCheckSchema = new JButton("Query Everything Above this Button");
		btnCheckSchema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listScannedFilesWithoutDocuments = MongoDBUtils.findScannedFilesWithoutADocument(mongoClient, strDBSchema);
				jlistScannedFilesWithoutDocuments.setListData(listScannedFilesWithoutDocuments.toArray());
			}
		});
		
		JButton btnDeleteScannedFiles = new JButton("Delete Scans");
		btnDeleteScannedFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//insertDocument();
			}
		});
		
		JScrollPane scrollPaneScannedFiles = new JScrollPane();
		
		JLabel lblBrokenLinks = new JLabel("Scanned Files that have a Document but don't have a reference to their Document");
		
		JScrollPane scrollPaneScannedFilesNoLinkBack = new JScrollPane();
		
		scrollPaneScannedFilesNoLinkBack.setViewportView(jlistBrokenDocumentsReference);
		jlistBrokenDocumentsReference.setCellRenderer(new MissingReferenceRenderer());
		
		
		scrollPaneScannedFiles.setViewportView(jlistScannedFilesWithoutDocuments);
		
		scrollPaneDocument.setViewportView(jlistDocumentsWithoutScannedFiles);
		
		JLabel lblDocuments = new JLabel("Documents without Scanned Files");
		
		JLabel lblScannedFiles = new JLabel("Scanned files that don't have a Document");
		
		JButton btnDeleteDocuments = new JButton("Delete Docs");
		
		JButton btnVerifyBrokenReferenceAndMissingScannedFiles = new JButton("Query Everything Above this Button");
		btnVerifyBrokenReferenceAndMissingScannedFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listBrokenDocumentsReference = MongoDBUtils.findDocumentsWithScannedFilesWithoutDocumentReference(mongoClient, strDBSchema);
				jlistBrokenDocumentsReference.setListData(listBrokenDocumentsReference.toArray());
				
				listDocumentsWithoutScannedFiles = MongoDBUtils.findDocumentsWithoutScannedFiles(mongoClient, strDBSchema);
				jlistDocumentsWithoutScannedFiles.setListData(listDocumentsWithoutScannedFiles.toArray());
			}
		});
		
		JButton buttonFixScannedFilesReferences = new JButton("Add References");
		buttonFixScannedFilesReferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MongoDBUtils.fixDocumentsWithScannedFilesWithoutDocumentReference(mongoClient, strDBSchema);
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
				int intCount = jlistDocumentsWithoutScannedFiles.getModel().getSize();
				String saveLocation = txtSaveLocationDocs.getText(); 
				if(intCount == 0)
				{
					return;
				}
				
				if(saveLocation.length() < 2)
				{
					return;
				}
				File fileSaveLocation = new File(saveLocation);
				if(fileSaveLocation.exists() == false || fileSaveLocation.isDirectory() == false)
				{
					fileSaveLocation.mkdir();
					return;
				}
				
				try 
				{
					saveLocation = fileSaveLocation.getCanonicalPath();
				} catch (IOException e1) 
				{
					return;
				}

				for(int i = 0; i < intCount; i++)
				{
					Object obj = jlistDocumentsWithoutScannedFiles.getModel().getElementAt(i);	
					if(obj instanceof Documents)
					{
						Documents document = (Documents)obj;
				    	String strFileName = saveLocation + "/";
				    	strFileName = strFileName + document.getID().toString() + "." + document.getStrName().toString() + ".txt";
					    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
					    String jsonStr = gson.toJson(document);
				    	if(FileReadingUtils.doesFileExist(strFileName) == false)
				    	{
				        	FileReadingUtils.writeTextFile(strFileName, jsonStr);
				    	}
					}

				}
				
			}
		});
		
		JButton btnSaveScannedFilesToDisk = new JButton("Save to Disk");
		btnSaveScannedFilesToDisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JButton btnViewScan = new JButton("View Scan");
		btnViewScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Object> selectedDocuments = jlistScannedFilesWithoutDocuments.getSelectedValuesList();
				if(selectedDocuments == null || selectedDocuments.size() == 0)
				{
					return;
				}
				
				Object obj = selectedDocuments.get(0);
				if(obj instanceof ScannedFiles)
				{
					FileWritingUtils.openScannedFile((ScannedFiles)obj);
				}
			}
		});
		
		JButton btnViewDocument = new JButton("View Doc");
		btnViewDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Object> selectedDocuments = jlistDocumentsWithoutScannedFiles.getSelectedValuesList();
				if(selectedDocuments == null || selectedDocuments.size() == 0)
				{
					return;
				}
				
				Object obj = selectedDocuments.get(0);
				if(obj instanceof Documents)
				{
					FileWritingUtils.openDocument((Documents)obj);
				}

			}
		});
		
		JButton btnViewScanNoReference = new JButton("View Scan");
		btnViewScanNoReference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Object> selectedDocuments = jlistBrokenDocumentsReference.getSelectedValuesList();
				if(selectedDocuments == null || selectedDocuments.size() == 0)
				{
					return;
				}
				
				Object obj = selectedDocuments.get(0);
				if(obj instanceof Documents)
				{
					FileWritingUtils.openScannedDocument((Documents)obj);
				}
			}
		});
		
		txtSaveLocationScans = new JTextFieldEnhanced();
		txtSaveLocationScans.setColumns(10);
		
		JLabel lblSaveLocationScans = new JLabel("Save Scans In:");
		
		txtSaveLocationDocs = new JTextFieldEnhanced();
		txtSaveLocationDocs.setColumns(10);
		
		JLabel lblSaveLocationdocs = new JLabel("Save Docs In:");
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
							.addComponent(btnDeleteDocuments, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveDocumentsToDisk, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnViewDocument, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(92))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnVerifyBrokenReferenceAndMissingScannedFiles, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(buttonFixScannedFilesReferences, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnViewScanNoReference, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(229)))
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGap(16)
							.addComponent(lblScannedFiles, GroupLayout.PREFERRED_SIZE, 528, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPaneScannedFiles, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
								.addComponent(btnCheckSchema, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
								.addGroup(gl_panelMainPanel.createSequentialGroup()
									.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnDeleteScannedFiles, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblSaveLocationScans)
										.addComponent(lblSaveLocationdocs, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panelMainPanel.createSequentialGroup()
											.addComponent(btnSaveScannedFilesToDisk, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnViewScan, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, 138, Short.MAX_VALUE))
										.addComponent(txtSaveLocationScans, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
										.addComponent(txtSaveLocationDocs, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))))))
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
						.addComponent(scrollPaneDocument, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPaneScannedFiles, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnDeleteScannedFiles)
							.addComponent(btnSaveScannedFilesToDisk)
							.addComponent(btnViewScan))
						.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnDeleteDocuments)
							.addComponent(btnSaveDocumentsToDisk)
							.addComponent(btnViewDocument)))
					.addGap(18)
					.addComponent(lblBrokenLinks)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPaneScannedFilesNoLinkBack, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelMainPanel.createSequentialGroup()
							.addComponent(txtSaveLocationScans, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(16)
							.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtSaveLocationDocs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSaveLocationdocs)))
						.addComponent(lblSaveLocationScans))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonFixScannedFilesReferences)
						.addComponent(btnViewScanNoReference))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnVerifyBrokenReferenceAndMissingScannedFiles)
						.addComponent(btnCheckSchema))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		panelMainPanel.setLayout(gl_panelMainPanel);
	}
	
	private void setFolderNames()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Calendar cal = Calendar.getInstance();
		String strDate = dateFormat.format(cal.getTime());
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		s = s + "\\"+ strDate;
		txtSaveLocationDocs.setText(s);
		txtSaveLocationScans.setText(s);
	}
	
	private void saveScannedFilesToDisk()
	{
		if(listScannedFilesWithoutDocuments != null && listScannedFilesWithoutDocuments.size() > 0)
		{
			String strPath = txtSaveLocationScans.getText();
			FileWritingUtils.saveScannedFilesToDisk(strPath, listScannedFilesWithoutDocuments);
		}
	}
	
	private void saveDocumentsToDisk()
	{
		if(listScannedFilesWithoutDocuments != null && listScannedFilesWithoutDocuments.size() > 0)
		{
			String strPath = txtSaveLocationScans.getText();
			FileWritingUtils.saveDocumentsToDisk(strPath, listDocumentsWithoutScannedFiles);
		}
	}
	
	public static void main(String... args)
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.doLogin();
		new DatabaseDiagnostics(loginCredentials.getMongoClient(), loginCredentials.getDBSchema());
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
