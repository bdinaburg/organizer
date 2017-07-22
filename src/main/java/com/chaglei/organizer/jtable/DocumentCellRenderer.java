package com.chaglei.organizer.jtable;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.chaglei.organizer.ColumnFormatter;

public class DocumentCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 669088245248395996L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	 {
		 if(value == null)
		 {
			 super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
			 return this;
		 }
		 if(value instanceof Date)
		 {
			 SimpleDateFormat simpleDateFormat = ColumnFormatter.getInstance(false).getDateFormat();
			 String formattedDate = simpleDateFormat.format((Date)value);
			 super.getTableCellRendererComponent(table, formattedDate, isSelected, hasFocus, row, column);
			 return this;
		 }
		 /**
		  * TODO hard coded the name of the column, try to get rid of this
		  */
		 if(table.getColumnModel().getColumn(column).getHeaderValue().toString().equalsIgnoreCase("Doc Name"))
		 {
			 if(ColumnFormatter.getInstance(false).getHidePath() == true)
			 {
				 String strFullFileName = value.toString();
				 int intSlashLocation = strFullFileName.lastIndexOf("\\");
				 String strJustFileName = strFullFileName.substring(intSlashLocation + 1, strFullFileName.length());
				 super.getTableCellRendererComponent(table, strJustFileName, isSelected, hasFocus, row, column);
				 return this;
			 }
		 }
		 
		if (value != null && value instanceof BigDecimal == true) {

			
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
			String formattedValue = numberFormat.format((BigDecimal)value);
			super.getTableCellRendererComponent(table, formattedValue, isSelected, hasFocus, row, column);
			return this;
		}

		 super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		 return this;
	 }	
}

