package com.chaglei.organizer;

import java.awt.Dimension;
import java.awt.MouseInfo;
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

class OrganizerPopupMenu extends JPopupMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem deleteMenuItem;
	static OrganizerPopupMenu selfReference = null;
	
	public static OrganizerPopupMenu getIntance(Point point, JTable jTable, MongoClient mongoClient, String strDBname){
		if(selfReference == null)
		{
			selfReference = new OrganizerPopupMenu(point, jTable, mongoClient, strDBname);
		}
    	point = MouseInfo.getPointerInfo().getLocation();
		selfReference.setLocation(point);
		selfReference.setVisible(true);
		selfReference.setMinimumSize(new Dimension(100, 10));
		selfReference.setMaximumSize(new Dimension(100, 10));
		return selfReference;
    	
	}
	
    public static void removeFromView()
    {
    	if(selfReference != null)
    	{
    		selfReference.setVisible(false);
    	}
    }
	
    private OrganizerPopupMenu(Point point, JTable jTable, MongoClient mongoClient, String strDBname){
    	deleteMenuItem = new JMenuItem("Delete");
    	deleteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	        	int row = jTable.getSelectedRow();
	        	Documents document = ((TableModel)jTable.getModel()).getDocumentAtRow(row);
	        	if(document != null)
	        	{
	    			int dialogButton = JOptionPane.YES_NO_OPTION;
	    			dialogButton = JOptionPane.showConfirmDialog (null, "Confirm... are you sure, delete?","WARNING", dialogButton);
	                if(dialogButton == JOptionPane.YES_OPTION) 
	                {
	                	MongoDBUtils.deleteDocument(mongoClient, strDBname, document);
	                }
	        	}
	        	selfReference.setVisible(false);
			}
		});
        add(deleteMenuItem);
    	this.setLocation(point);
    	this.setVisible(true);
    	this.setMinimumSize(new Dimension(100, 10));
    	this.setMaximumSize(new Dimension(100, 10));
    }
}
