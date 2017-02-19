package com.chaglei.organizer.jtable;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DocumentCellRenderer implements TableCellRenderer
{
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	 {
		 JLabel componentJLabel = new JLabel();
		 table.getColumnModel().getColumn(column).getHeaderValue().toString();
		 return componentJLabel;
	 }	
}

