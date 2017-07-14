package com.chaglei.organizer.jtable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import util.ConfigData;

public class JTableEx extends JTable {

	private static final long serialVersionUID = -248144434053994443L;
	private boolean isColumnWidthChanged;
    private JTableEx tableObj = this;
    
    public JTableEx()
    {
    	super();
    	this.getColumnModel().addColumnModelListener(new TableColumnWidthListener());
    	this.getTableHeader().addMouseListener(new TableHeaderMouseListener());
    }
    
    public boolean getColumnWidthChanged() {
        return isColumnWidthChanged;
    }

    public void setColumnWidthChanged(boolean widthChanged) {
        isColumnWidthChanged = widthChanged;
    }
	
	private class TableColumnWidthListener implements TableColumnModelListener
	{
	    @Override
	    public void columnMarginChanged(ChangeEvent e)
	    {
	        /* columnMarginChanged is called continuously as the column width is changed
	           by dragging. Therefore, execute code below ONLY if we are not already
	           aware of the column width having changed */
	        if(!tableObj.getColumnWidthChanged())
	        {
	            /* the condition  below will NOT be true if
	               the column width is being changed by code. */
	            if(tableObj.getTableHeader().getResizingColumn() != null)
	            {
	                // User must have dragged column and changed width
	                tableObj.setColumnWidthChanged(true);
	            }
	        }
	    }

	    @Override
	    public void columnMoved(TableColumnModelEvent e) { }

	    @Override
	    public void columnAdded(TableColumnModelEvent e) { }

	    @Override
	    public void columnRemoved(TableColumnModelEvent e) { }

	    @Override
	    public void columnSelectionChanged(ListSelectionEvent e) { }
	}

	private class TableHeaderMouseListener extends MouseAdapter
	{
	    @Override
	    public void mouseReleased(MouseEvent e)
	    {
	        /* On mouse release, check if column width has changed */
	        if(tableObj.getColumnWidthChanged())
	        {
	        	/**
	        	 * this gets triggered when JTable column has it's width adjusted.
	        	 * 
	        	 * Save tha names of all visible columns and their widths to the config file
	        	 * 
	        	 */
	            ConfigData.setColumns(tableObj);
	            tableObj.setColumnWidthChanged(false);
	        }
	    }
	}
}
