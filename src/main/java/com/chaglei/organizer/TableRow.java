package com.chaglei.organizer;

import java.math.BigDecimal;
import java.util.Date;

import pojos.DocumentType;
import transientPojos.DocTypes.DocType;

//	private String[] columnNames = { "Doc Name", "Create Date", "Insertion Date", "Description", "Doc Type", "Dollar Amount", "Due Date"};
public class TableRow 
{
	String strDocumentName;
	Date dateCreateDate;
	Date dateInsertionDate;
	String strDescription;
	DocType docTypeDocType;
	BigDecimal bigDecDollarAmount;
	Date dateDueDate;
	public TableRow(String strDocumentName, Date dateCreateDate, Date dateInsertionDate, String strDescription,
			DocType docTypeDocType, BigDecimal bigDecDollarAmount, Date dateDueDate) 
	{
		this.strDocumentName = strDocumentName; 
		this.dateCreateDate = dateCreateDate;
		this.dateInsertionDate = dateInsertionDate;
		this.strDescription = strDescription;
		this.docTypeDocType = docTypeDocType;
		this.bigDecDollarAmount = bigDecDollarAmount;
		this.dateDueDate = dateDueDate;
	}
	
	
	public String getDocumentName() {
		return strDocumentName;
	}
	public void setDocumentName(String strDocumentName) {
		this.strDocumentName = strDocumentName;
	}
	public Date getDateCreateDate() {
		return dateCreateDate;
	}
	public void setDateCreateDate(Date dateCreateDate) {
		this.dateCreateDate = dateCreateDate;
	}
	public Date getDateInsertionDate() {
		return dateInsertionDate;
	}
	public void setDateInsertionDate(Date dateInsertionDate) {
		this.dateInsertionDate = dateInsertionDate;
	}
	public String getDescription() {
		return strDescription;
	}
	public void setDescription(String strDescription) {
		this.strDescription = strDescription;
	}
	public DocType getDocumentType() {
		return docTypeDocType;
	}
	public void setDocumentType(DocType docTypeDocType) {
		this.docTypeDocType = docTypeDocType;
	}
	public BigDecimal getDollarAmount() {
		return bigDecDollarAmount;
	}
	public void setDollarAmount(BigDecimal bigDecDollarAmount) {
		this.bigDecDollarAmount = bigDecDollarAmount;
	}
	public Date getDateDueDate() {
		return dateDueDate;
	}
	public void setDateDueDate(Date dateDueDate) {
		this.dateDueDate = dateDueDate;
	}
}
