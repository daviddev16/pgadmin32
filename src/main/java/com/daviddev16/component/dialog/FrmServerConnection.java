package com.daviddev16.component.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.daviddev16.component.LabeledTextField;
import com.daviddev16.event.interaction.CreatedServerEvent;
import com.daviddev16.node.Server;
import com.daviddev16.service.AES256Manager;
import com.daviddev16.service.ServicesFacade;

public class FrmServerConnection extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	public FrmServerConnection() {
		setTitle("Configurador de conexão");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 361, 361);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		LabeledTextField txFdNmServidor = new LabeledTextField();
		txFdNmServidor.getTextField().setText("Nome do Servidor");
		txFdNmServidor.setTitle("Nome do Servidor:");
		txFdNmServidor.setBounds(10, 11, 325, 49);
		contentPanel.add(txFdNmServidor);
		
		LabeledTextField txFdEnderecoHost = new LabeledTextField();
		txFdEnderecoHost.getTextField().setText("127.0.0.1");
		txFdEnderecoHost.setTitle("Endereço do Host:");
		txFdEnderecoHost.setBounds(10, 65, 325, 49);
		contentPanel.add(txFdEnderecoHost);
		
		LabeledTextField txFdPortaHost = new LabeledTextField();
		txFdPortaHost.getTextField().setText("5432");
		txFdPortaHost.setTitle("Porta do Host:");
		txFdPortaHost.setBounds(10, 119, 325, 49);
		contentPanel.add(txFdPortaHost);
		
		LabeledTextField txFdNmUsuario = new LabeledTextField();
		txFdNmUsuario.getTextField().setText("postgres");
		txFdNmUsuario.setTitle("Usuário:");
		txFdNmUsuario.setBounds(10, 173, 325, 49);
		contentPanel.add(txFdNmUsuario);
		
		LabeledTextField txFdSenha = new LabeledTextField();
		txFdSenha.getTextField().setText("#abc123#");
		txFdSenha.setTitle("Senha:");
		txFdSenha.setBounds(10, 227, 325, 49);
		contentPanel.add(txFdSenha);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) 
					{
						String encryptedPassword = null;
						try {
							encryptedPassword = AES256Manager.encrypt(txFdSenha.getInput());
						} catch (Exception e1) {
							throw new IllegalStateException("Failed to encrypt the password.", e1);
						}
						Server server = Server
								.builder()
									.serverName(txFdNmServidor.getInput())
									.host(txFdEnderecoHost.getInput())
									.password(encryptedPassword)
									.port(Integer.parseInt(txFdPortaHost.getInput()))
									.username(txFdNmUsuario.getInput())
								.build();
						
						ServicesFacade.getServices().getEventManager()
							.dispatchEvent(new CreatedServerEvent(FrmServerConnection.this, server));
						
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
