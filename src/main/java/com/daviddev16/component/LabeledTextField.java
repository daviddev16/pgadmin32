package com.daviddev16.component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class LabeledTextField extends JPanel {

	
	private static final long serialVersionUID = 509390001096404242L;
	private JLabel lblTitle;
	private JTextField txtFieldValue;

	public LabeledTextField() {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblTitle = new JLabel("Title");
		springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblTitle, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblTitle, 0, SpringLayout.EAST, this);
		add(lblTitle);
		
		txtFieldValue = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, txtFieldValue, 6, SpringLayout.SOUTH, lblTitle);
		springLayout.putConstraint(SpringLayout.WEST, txtFieldValue, 0, SpringLayout.WEST, lblTitle);
		springLayout.putConstraint(SpringLayout.EAST, txtFieldValue, 0, SpringLayout.EAST, lblTitle);
		add(txtFieldValue);
		txtFieldValue.setColumns(10);
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
	}
	
	public String getInput() {
		return txtFieldValue.getText();
	}
	
	public JTextField getTextField() {
		return txtFieldValue;
	}
	
}
