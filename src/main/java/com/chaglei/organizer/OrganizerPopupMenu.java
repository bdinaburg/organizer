package com.chaglei.organizer;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.chaglei.organizer.jtable.TableModel;
import com.mongodb.MongoClient;

import pojos.Documents;
import util.MongoDBUtils;

class OrganizerPopupMenu {
	JPopupMenu jPopupMenu = new JPopupMenu(); 
	JMenuItem menuItemDelete;
	JMenuItem menuItemUpdate;
	JTable jTable;
	static OrganizerPopupMenu selfReference = null;
	
	public static OrganizerPopupMenu getIntance(Point point, JTable jTable, MongoClient mongoClient, String strDBname){
		if(selfReference == null)
		{
			selfReference = new OrganizerPopupMenu(point, jTable, mongoClient, strDBname);
		}
		selfReference.jPopupMenu.show(jTable, point.x, point.y);
		return selfReference;

	}

	private OrganizerPopupMenu(Point point, JTable jTable, MongoClient mongoClient, String strDBname){
		this.jTable = jTable;
    	menuItemDelete = new JMenuItem("Delete");
    	menuItemDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	        	int row = jTable.getSelectedRow();
	        	row = jTable.convertRowIndexToModel(row);
	        	Documents document = ((TableModel)jTable.getModel()).getDocumentAtRow(row);
	        	if(document != null)
	        	{
	    			int dialogButton = JOptionPane.YES_NO_OPTION;
	    			dialogButton = JOptionPane.showConfirmDialog (null, "Confirm... are you sure, delete, \n" + document.getStrName(),"WARNING", dialogButton);
	                if(dialogButton == JOptionPane.YES_OPTION) 
	                {
	                	MongoDBUtils.deleteDocument(mongoClient, strDBname, document);
	                }
	        	}
			}
		});
    	
    	menuItemUpdate = new JMenuItem("Update");
    	menuItemUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	        	int row = jTable.getSelectedRow();
	        	row = jTable.convertRowIndexToModel(row);
	        	Documents document = ((TableModel)jTable.getModel()).getDocumentAtRow(row);
	        	if(document != null)
	        	{
                	new UpdateDocument(mongoClient, strDBname, document);
	        	}
			}
		});

    	jPopupMenu.add(menuItemDelete);
    	jPopupMenu.add(menuItemUpdate);
    	jPopupMenu.show(this.jTable, point.x, point.y);
    }
}
