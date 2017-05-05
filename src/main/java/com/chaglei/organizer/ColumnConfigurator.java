package com.chaglei.organizer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import util.FrameUtil;

public class ColumnConfigurator extends JFrame 
{
	private static final long serialVersionUID = 1L;

	JTable jTableGlobalVar = null;
	HashMap<String, TableColumn> hashMapRemovedColumns = new HashMap<String, TableColumn>();
	ActionListener actionListener;
	public ColumnConfigurator(JTable jTable) 
	{
		jTableGlobalVar = jTable;
		buildGUIDynamic(jTable);
		this.setVisible(true);
		Dimension dim = this.determineWidthOfFrame();
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Select Visible Columns");
		FrameUtil.centerWindow(this);		
	}

	public static void main (String... args)
	{
		JTable jTable = new JTable();
		TableColumn tableColumnToRemove = null;
		//Set<TableColumn> setOfTableColumns = new HashSet<TableColumn>();
		for(int i = 0; i < 12; i ++)
		{
			TableColumn tableColumn = new TableColumn();
			if(i == 6)
			{
				tableColumnToRemove = tableColumn;
			}
			tableColumn.setHeaderValue(new Integer(i).toString());
			//setOfTableColumns.add(tableColumn);
			jTable.getColumnModel().addColumn(tableColumn);
		}
		
		jTable.removeColumn(tableColumnToRemove);
		//Enumeration<TableColumn> enumerationTableColumn = Collections.enumeration(setOfTableColumns);
		
		ColumnConfigurator columnConfigurator = new ColumnConfigurator(jTable);
		
		
	}
	
	private Dimension determineWidthOfFrame()
	{
		int intComponentWidth = 0;
		int intComponentHeight = 0;
		double dblBiggestLeft = 0;
		double dblBiggestTop = 0;
		for (Component component : this.getContentPane().getComponents()) {
		    if (component instanceof JCheckBox) { 
		    	JCheckBox checkbox = (JCheckBox) component;
		    	double dblUpperLeftPoint = checkbox.getLocation().getX();
		    	if(dblUpperLeftPoint > dblBiggestLeft)
		    	{
		    		dblBiggestLeft = dblUpperLeftPoint;
		    		intComponentWidth = checkbox.getWidth();
		    	}
		    	
		    	double dblTopPoint = checkbox.getLocation().getY();
		    	if(dblTopPoint > dblBiggestTop)
		    	{
		    		dblBiggestTop = dblTopPoint;
		    		intComponentHeight = checkbox.getHeight();
		    	}
		    }
		}
		
		Double dbl = intComponentWidth + dblBiggestLeft + 20;
		Integer intWidth = dbl.intValue();
		
		dbl = dblBiggestTop + intComponentHeight + 75; //this takes into account the border of the title window at the top.
		Integer intHeight = dbl.intValue();
		Dimension dim = new Dimension(intWidth, intHeight);
		return dim;
	}
	
	private void buildGUIDynamic(JTable jTable)
	{
		final int intColumnCount = 5;
		Vector<String> vectorOfTableColumnNames = new Vector<String>(20);

		for(int i = 0; i < jTable.getModel().getColumnCount(); i++)
		{
			vectorOfTableColumnNames.addElement(jTable.getModel().getColumnName(i));
		}

/*
		for(TableColumn tb : vectorOfTableColumn)
		{
			System.out.println(tb.getHeaderValue().toString());
		}
*/
		int intRowCount = (vectorOfTableColumnNames.size() / intColumnCount) + 1;
		Vector<ColumnSpec> vectorOfColumnSpecs = new Vector<ColumnSpec>(22);
		Vector<RowSpec> vectorOfRowSpecs = new Vector<RowSpec>(intRowCount);
		
		for(int i = 0; i < (intColumnCount * 4) + 1; i++)
		{
			vectorOfColumnSpecs.addElement(FormSpecs.RELATED_GAP_COLSPEC);
			vectorOfColumnSpecs.addElement(FormSpecs.DEFAULT_COLSPEC);
		}


		for(int i = 0; i < intRowCount * 2; i++)
		{
			vectorOfRowSpecs.addElement(FormSpecs.RELATED_GAP_ROWSPEC);
			vectorOfRowSpecs.addElement(FormSpecs.DEFAULT_ROWSPEC);
		}
		ColumnSpec[] columnSpecArray = vectorOfColumnSpecs.toArray(new ColumnSpec[vectorOfColumnSpecs.size()]);  
		RowSpec[] rowSpecArray = vectorOfRowSpecs.toArray(new RowSpec[vectorOfRowSpecs.size()]);
		FormLayout formLayout = new FormLayout(columnSpecArray, rowSpecArray);
		getContentPane().setLayout(formLayout);

		int intExtractionCounter=0; 
		for(int j = 2; j < (intRowCount * 2 + 1); j = j+2)
		{
			for(int i = 2; i < (intColumnCount * 4) + 2; i = i + 4)
			{
				if(intExtractionCounter < vectorOfTableColumnNames.size())
				{
					String strTableColumn = vectorOfTableColumnNames.elementAt(intExtractionCounter);
					JCheckBox chckbxNewCheckBox = new JCheckBox(strTableColumn);
					chckbxNewCheckBox.addActionListener(getActionListener());
					if(isColumnVisible( jTable, strTableColumn) == true)
					{
						chckbxNewCheckBox.setSelected(true);
					}
					else
					{
						TableColumn tableCol = new TableColumn();
						tableCol.setHeaderValue(strTableColumn);
						hashMapRemovedColumns.put(strTableColumn, tableCol);
						chckbxNewCheckBox.setSelected(false);
					}
					intExtractionCounter++;
					String strPositioning = (new Integer(i).toString()) + ", " + (new Integer(j).toString());
					//System.out.println(strPositioning);
					getContentPane().add(chckbxNewCheckBox, strPositioning);
				}
			}
		}
	}
	
	boolean isColumnVisible(JTable jTable, String columnName)
	{
		for(int i = 0; i < jTable.getColumnModel().getColumnCount(); i++)
		{
			if (jTable.getColumnModel().getColumn(i).getHeaderValue().toString().equalsIgnoreCase(columnName) == true)
			{
					return true;
			}

		}
		
		return false;
	}
	
	private ActionListener getActionListener()
	{
		if (actionListener == null) {
			actionListener = new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
					boolean selected = abstractButton.getModel().isSelected();
					String strButtonText = abstractButton.getText();
					if (isColumnVisible(jTableGlobalVar, strButtonText) == true) {
						if (selected == false) {
							/**
							 * button text should be the name of the column.
							 */
							int intColumnToRemove = findColumnInColumnModelByName(jTableGlobalVar, strButtonText);
							TableColumn tableColumnToRemove = jTableGlobalVar.getColumnModel().getColumn(intColumnToRemove);
							hashMapRemovedColumns.put(strButtonText, tableColumnToRemove);
							jTableGlobalVar.removeColumn(tableColumnToRemove);
						}
					} else {
						// so column is not visible, so let's check to see if
						// user wants it visible
						if (selected == true) 
						{
							TableColumn tableColumnToAdd = hashMapRemovedColumns.remove(strButtonText);
							int intIndexInModel = findColumnInDataModelByName(jTableGlobalVar, strButtonText);
							tableColumnToAdd.setModelIndex(intIndexInModel);
							jTableGlobalVar.addColumn(tableColumnToAdd);
						}

					}
					System.out.println(selected);
					// abstractButton.setText(newLabel);
				}
			};
		}
		return actionListener;
	}
	
	private int findColumnInColumnModelByName(JTable jTable, String strColumnName)
	{
		Enumeration<TableColumn> enumerationTableColumn = jTable.getColumnModel().getColumns();
		int i = 0;
		while(enumerationTableColumn.hasMoreElements())
		{

			TableColumn tableColumn = enumerationTableColumn.nextElement();
			if(tableColumn.getHeaderValue().toString() == strColumnName)
			{
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private int findColumnInDataModelByName(JTable jTable, String strColumnNameIn)
	{
		int intColumnCount = jTable.getModel().getColumnCount();
		
		for(int i = 0; i < intColumnCount; i++)
		{

			String strColumName = jTable.getModel().getColumnName(i);
			if(strColumnNameIn.equalsIgnoreCase(strColumName) == true){
				return i;
			}
			
		}
		return -1;
	}
	
}
