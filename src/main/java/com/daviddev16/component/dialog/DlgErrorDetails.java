package com.daviddev16.component.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.daviddev16.service.ServicesFacade;

public class DlgErrorDetails extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;

	public static void showForExcetion(Exception exception) {
		new DlgErrorDetails(exception).setVisible(true);
	}
	
	private DlgErrorDetails(Exception exception) {
		setModal(true);
		setAlwaysOnTop(true);
		getContentPane().setLayout(new BorderLayout());
		setSize(new Dimension(700, 550));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblNewLabel = new JLabel(exception.getMessage());
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setIconTextGap(5);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblNewLabel.setSize(new Dimension(32, 32));
		lblNewLabel.setPreferredSize(new Dimension(32, 32));
		lblNewLabel.setIcon(ServicesFacade.getServices().getFileResourceLocator().cachedImageIcon("Error32px"));
		{
			okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DlgErrorDetails.this.dispose();
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(okButton))
				.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(okButton))
		);
		
		JTextPane txStacktrace = new JTextPane();
		txStacktrace.setText(getStacktraces(exception));
		txStacktrace.setOpaque(false);
		txStacktrace.setEditable(false);
		scrollPane.setViewportView(txStacktrace);
		contentPanel.setLayout(gl_contentPanel);
	}
	
	private String getStacktraces(Throwable exception) {
		return internalGetStacktraces(exception, false);
	}
	
	private String internalGetStacktraces(Throwable exception, boolean causedBy) {
		
		StringBuilder tempStringBuilder = new StringBuilder();
		
		final String exceptionTitle = (causedBy) ? 
				String.format("Causado por -> %s", exception.getClass().getName()) : exception.getMessage();
		
		appendWithLine(tempStringBuilder, exceptionTitle);
		
		for (StackTraceElement stackTraceElement : exception.getStackTrace())
			appendWithLine(tempStringBuilder, String.format("	%s", stackTraceElement.toString()));

		if (exception.getCause() != null)
			appendWithLine(tempStringBuilder, internalGetStacktraces(exception.getCause(), false));
		
		return tempStringBuilder.toString();

	}
	
	private void appendWithLine(StringBuilder stringBuilder, String text) {
		stringBuilder.append(text).append("\n");
	}
	
}
