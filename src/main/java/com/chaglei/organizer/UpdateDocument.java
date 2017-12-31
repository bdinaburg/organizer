package com.chaglei.organizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.mongodb.MongoClient;

import pojos.DocumentType;
import pojos.Documents;
import swingUtil.JTextFieldEnhanced;
import transientPojos.DocTypes.DocType;
import util.ConfigData;
import util.DateUtil;
import util.FrameUtil;
import util.MongoDBUtils;

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
	JTextFieldEnhanced txtDescription = new JTextFieldEnhanced(); //unused
	JTextFieldEnhanced txtIMGLocation = new JTextFieldEnhanced();
	JComboBox<DocumentType> comboBoxDocumentTypes = new JComboBox<DocumentType>();
	JTextArea textAreaPDFText = new JTextArea();
	
	public UpdateDocument(MongoClient mongoClient, String schema, Documents document) {
		this.strDBSchema = schema;
		this.mongoClient = mongoClient;
		buildGUI("Update Document", document);
		populateDocTypes(document);
		populateFields(document);
		setSize((int)(1920/1.8), 1080/2);
		setPreferredSize(new Dimension((int)(1920/1.8), 1080/2));
		FrameUtil.centerWindow(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void buildGUI(String strTitle, Documents document)
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
	
		btnFindDoc.setEnabled(false);


		

		btnSaveDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateDocument(document);
			}
		});
		
		/**
		 * ItemChangeListener is an internal class. Listens for changes and adjusts the GUI 
		 * accordingly as not all input fields make sense for every type of document
		 */
		comboBoxDocumentTypes.addItemListener(new ItemChangeListener(document));
		
		
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
		
	}
	
	private void populateDocTypes(Documents document)
	{
		List<DocumentType> listDocTypes = MongoDBUtils.getDocTypes(mongoClient, strDBSchema);
		for(DocumentType docTypes : listDocTypes)
		{
			this.comboBoxDocumentTypes.addItem(docTypes);
		}
	}
	
	private void populateFields(Documents document)
	{
		String theFormat = ConfigData.getInsertDateFormat();
		DateFormat dateFormat = new SimpleDateFormat(theFormat);
		
		this.txtDocumentName.setText(document.getStrName());
		try{ this.txtCreateDate.setText(dateFormat.format(document.getDate_document_creation()));  } catch (Exception anyExc) { this.txtCreateDate.setText("");}		
		try{ this.txtDueDate.setText(dateFormat.format(document.getDate_due_date())); } catch (Exception anyExc) { this.txtDueDate.setText("");}
		try{ this.txtIMGLocation.setText(document.getStrAddress()); } catch (Exception anyExc) { this.txtIMGLocation.setText("");}
		
		DecimalFormat df = new DecimalFormat("0.00");
		try{ this.txtAmount.setText(df.format(Double.parseDouble(document.getBigDecimalAmount().toString()))); } catch (Exception anyExc) { this.txtAmount.setText("");}
		if(Double.parseDouble(document.getBigDecimalAmount().toString()) == 0) { this.txtAmount.setText(""); }
		try{ this.txtPaidDate.setText(dateFormat.format(document.getDate_paid_date())); } catch (Exception anyExc) { this.txtPaidDate.setText("");} 
		//try{ this.txtCurrency.setText(document.getCurrency().toString()); } catch (Exception anyExc) { this.txtCurrency.setText("USD");}
		this.textAreaPDFText.setText(document.getStrDocumentDescription());
		this.txtDueDate.setEnabled(true);
		this.txtPaidDate.setEnabled(true);
		
		for(int i = 0; i < comboBoxDocumentTypes.getItemCount(); i++)
		{
			DocumentType dt1 = comboBoxDocumentTypes.getItemAt(i);
			if(dt1.toString().equalsIgnoreCase(document.getDocumentType().toString()))
			{
				this.comboBoxDocumentTypes.setSelectedIndex(i);
			}
		}
		
		/*		String txtName, 
		String strDocumentCreateDate, 
		String strDueDate, 
		String strLocation,
		String strAmount, 
		String strPaidDate, 
		String strCurrency,
		String strDescription,*/ 
	}
	
	private boolean updateDocument(Documents document)
	{
		document.setStrName(this.txtDocumentName.getText());
		document.setDate_document_creation(DateUtil.getDateObject(txtCreateDate.getText()));
		document.setDate_due_date(DateUtil.getDateObject(txtDueDate.getText()));
		document.setStrAddress(txtIMGLocation.getText());
		try {document.setBigDecimalAmount(new BigDecimal(txtAmount.getText())); } catch ( Exception anyExc ) {document.setBigDecimalAmount( new BigDecimal(0)); }
		document.setDate_paid_date(DateUtil.getDateObject(txtPaidDate.getText()));
		document.setStrDocumentDescription(textAreaPDFText.getText());
		DocumentType documentType;
		documentType = (DocumentType)(this.comboBoxDocumentTypes.getSelectedItem());
		document.setDocumentType(documentType);
		
		Documents doc = MongoDBUtils.saveDocument( document, mongoClient, strDBSchema);
		
		if (doc == null) { return false; }  else { return true; }
	}

	public static void main(String... args)
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.doLogin();
		Documents document = new Documents();
		document.setStrName("2009 dodge charger repair bill 1823.49");
		document.setDate_document_creation(DateUtil.getDateObject("09/25/2015"));
		document.setDate_due_date(null);
		document.setBigDecimalAmount(new BigDecimal("1823.49"));
		document.setDate_paid_date(DateUtil.getDateObject("09/30/2015"));
		document.setCurrency(Currency.getInstance("USD"));
		document.setStrDocumentDescription("random stuff");
		document.setDocumentType(new DocumentType(DocType.BILL_CAR));
		new UpdateDocument(loginCredentials.getMongoClient(), loginCredentials.getDBSchema(), document);
	}
	
	class ItemChangeListener implements ItemListener 
	{
		Documents document;
		ItemChangeListener(Documents document)
		{
			this.document = document;
		}
		@Override
		public void itemStateChanged(ItemEvent event) 
		{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				DocumentType documentType = (DocumentType) event.getItem();
				txtDueDate.setEnabled(true);
				txtPaidDate.setEnabled(true);
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
