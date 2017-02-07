package com.chaglei.organizer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import transientPojos.DocTypes.DocType;

public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = -7041041701893926863L;

	public final int DOC_NAME = 0;
	public final int CREATE_DATE = 1;
	public final int INSERTION_DATE = 2;
	public final int DESCRIPTION = 3;
	public final int DOC_TYPE = 4;
	public final int DOLLAR_AMOUNT = 5;
	public final int DUE_DATE = 6;
	
	
	private Vector<TableRow> vector;
	TableColumnModel tableColumnModel;

	private String[] columnNames = { "Doc Name", "Create Date", "Insertion Date", "Description", "Doc Type", "Dollar Amount", "Due Date"};

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
		
		switch (columnIndex) {
		case DOC_NAME:
			return vector.elementAt(rowIndex).getDocumentName();
		case CREATE_DATE:
			return vector.elementAt(rowIndex).getDateCreateDate();
		case INSERTION_DATE:
			return vector.elementAt(rowIndex).getDateInsertionDate();
		case DESCRIPTION:
			return vector.elementAt(rowIndex).getDescription();
		case DOC_TYPE:
			return vector.elementAt(rowIndex).getDocumentType();
		case DOLLAR_AMOUNT:
			return vector.elementAt(rowIndex).getDollarAmount();
		case DUE_DATE:
			return vector.elementAt(rowIndex).getDateDueDate();
		}
		return null;
	}

	public String getColumnName(int index) {

		return columnNames[index];

	}

	public void addDataModelRow(String strDocumentName, Date dateCreateDate, Date insertionDate, String strDescription, DocType docType, BigDecimal dollarAmount, Date dueDate ) {
		vector.addElement(new TableRow(strDocumentName,  dateCreateDate,  insertionDate,  strDescription,  docType,  dollarAmount,  dueDate));
	}
}