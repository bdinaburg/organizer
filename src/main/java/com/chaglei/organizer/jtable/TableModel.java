package com.chaglei.organizer.jtable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import pojos.Documents;
import transientPojos.DocTypes.DocType;

public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = -7041041701893926863L;

	public final String DOC_NAME = "Doc Name";
	public final String CREATE_DATE = "Create Date";
	public final String INSERTION_DATE = "Insertion Date";
	public final String DESCRIPTION = "Description";
	public final String DOC_TYPE =  "Doc Type";
	public final String DOLLAR_AMOUNT = "Dollar Amount";
	public final String DUE_DATE = "Due Date";
	public final String DOCUMENT_OBJECT = "DocumentObject";
	
	
	private Vector<TableRow> vector;
	TableColumnModel tableColumnModel;

	private String[] columnNames = { DOC_NAME, CREATE_DATE, INSERTION_DATE, DESCRIPTION, DOC_TYPE, DOLLAR_AMOUNT, DUE_DATE, DOCUMENT_OBJECT};

	public TableModel() {
		vector = new Vector<TableRow>(25);
	}

	public int getRowCount() {
		return vector.size();
	}

	public int getColumnCount() {

		return columnNames.length;

	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(vector == null || vector.elementAt(rowIndex) == null)
		{
			return null;
		}
		
			

		
		switch (this.getColumnName(columnIndex)) {
		case DOC_NAME:
			return vector.elementAt(rowIndex).getDocument().getStrName();
		case CREATE_DATE:
			return vector.elementAt(rowIndex).getDocument().getDate_document_creation().toString();
		case INSERTION_DATE:
			return vector.elementAt(rowIndex).getDocument().getDate_document_database_insertion();
		case DESCRIPTION:
			return vector.elementAt(rowIndex).getDocument().getStrDocumentDescription();
		case DOC_TYPE:
			return vector.elementAt(rowIndex).getDocument().getDocumentType().toString();
		case DOLLAR_AMOUNT:
			return vector.elementAt(rowIndex).getDocument().getBigDecimalAmount().toString();
		case DUE_DATE:
			if(vector.elementAt(rowIndex).getDocument().getDate_due_date() == null)
			{
				return "";
			}
			return vector.elementAt(rowIndex).getDocument().getDate_due_date().toString();
		case DOCUMENT_OBJECT:
			return vector.elementAt(rowIndex).getDocument().toString();
		}
		return null;
	}

	public String getColumnName(int index) {

		return columnNames[index];

	}

	public void addDataModelRow(Documents document) {
		TableRow tableRow = new TableRow(document);
		vector.addElement(tableRow);
	}
	
	public void populateDataModel(List<Documents> listOfDocuments) 
	{
		for(Documents document : listOfDocuments)
		{
			TableRow tableRow = new TableRow(document);
			vector.addElement(tableRow);
		}
	}
}