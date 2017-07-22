package com.chaglei.organizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import util.ConfigData;
import util.FrameUtil;

public class ColumnFormatter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtDateFormat;
	private JCheckBox jCheckBoxFullPath;
	private SimpleDateFormat simpleDateFormat;
	private boolean boolHidePath;
	private static ColumnFormatter columnFormatter;

	public ColumnFormatter(boolean showGUI) {
		buildGUI();
		populateSettingsForRetrieval();
		setVisible(showGUI);
		FrameUtil.setSize(this, Integer.parseInt(ConfigData.getLoginCredentialsWidth()), Integer.parseInt(ConfigData.getLoginCredentialsHeight()));
		FrameUtil.centerWindow(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void populateSettingsForRetrieval()
	{
		this.simpleDateFormat = new SimpleDateFormat(this.txtDateFormat.getText());
		this.boolHidePath = this.jCheckBoxFullPath.isSelected();
	}
	
	public SimpleDateFormat getDateFormat() { return simpleDateFormat; }
	public boolean getHidePath() { return boolHidePath; } 
	
	public static void main(String[] args) 
	{
		getInstance(true);
	}
	
	public static ColumnFormatter getInstance(boolean showGUI)
	{
		if(columnFormatter == null)
		{
			columnFormatter = new ColumnFormatter(showGUI);
		}
		columnFormatter.setVisible(showGUI);
		return columnFormatter;
		
	}
	
	protected void buildGUI()
	{
		setTitle("Format Columns");
		setResizable(false);
		setAlwaysOnTop(true);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
		);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConfigData.setColumnDateFormat(txtDateFormat.getText());
				setTxtDateFormatText();
				ConfigData.setColumnHidePath(jCheckBoxFullPath.isSelected());
				populateSettingsForRetrieval();
			}
		});
		
		JButton btnCancel = new JButton("Close");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doClose();
			}
		});
		
		JLabel lblDateFormat = new JLabel("Dates Format:");
		
		txtDateFormat = new JTextField();
		setTxtDateFormatText();
		txtDateFormat.setColumns(10);
		
		JLabel lblFullPath = new JLabel("Don't show full path:");
		
		jCheckBoxFullPath = new JCheckBox();
		String strHideSetting = ConfigData.getColumnHidePath();
		boolean hidePath = false;
		if (strHideSetting != null && strHideSetting.toLowerCase().contains("true")) {
			hidePath = true;
		}
		jCheckBoxFullPath.setSelected(hidePath);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(69)
					.addComponent(btnCancel)
					.addPreferredGap(ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
					.addComponent(btnApply, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addGap(79))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFullPath)
						.addComponent(lblDateFormat, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(jCheckBoxFullPath, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
						.addComponent(txtDateFormat, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDateFormat)
						.addComponent(txtDateFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFullPath)
						.addComponent(jCheckBoxFullPath))
					.addPreferredGap(ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnApply))
					.addGap(26))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}

	protected void doClose() {
		this.setVisible(false);
	}
	
	/**
	 * the save of the data doesn't allow an invalid format to be saved,
	 * so if you retrieve a format from the disk it has to be valid
	 */
	private void setTxtDateFormatText()
	{
		String dateFormat = ConfigData.getColumnDateFormat();
		if(dateFormat == null || dateFormat.length() < 3)
		{
			txtDateFormat.setText("yyyy-MM-dd");
		}
		else
		{
			txtDateFormat.setText(dateFormat);
		}
	}

}
