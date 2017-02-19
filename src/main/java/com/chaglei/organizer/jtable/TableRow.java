package com.chaglei.organizer.jtable;

import pojos.Documents;

//	private String[] columnNames = { "Doc Name", "Create Date", "Insertion Date", "Description", "Doc Type", "Dollar Amount", "Due Date"};
public class TableRow 
{

	Documents document;
	public TableRow(Documents document) 
	{
		this.document = document;
	}
	
	public Documents getDocument() { return document; }
	public void setDocument(Documents document) { this.document = document; }
	
}
