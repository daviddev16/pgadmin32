package com.daviddev16.component.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
	private LabeledTextField txFdNmServidor;
	private LabeledTextField txFdEnderecoHost;
	private LabeledTextField txFdPortaHost;
	private LabeledTextField txFdNmUsuario;
	private LabeledTextField txFdSenha;
	
	private Server server;
	
	public FrmServerConnection(boolean editMode) {
		
		setTitle("Configurador de conexão");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 361, 361);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txFdNmServidor = new LabeledTextField();
		txFdNmServidor.getTextField().setText("Nome do Servidor");
		txFdNmServidor.setTitle("Nome do Servidor:");
		txFdNmServidor.setBounds(10, 11, 325, 49);
		contentPanel.add(txFdNmServidor);
		
		txFdEnderecoHost = new LabeledTextField();
		txFdEnderecoHost.getTextField().setText("127.0.0.1");
		txFdEnderecoHost.setTitle("Endereço do Host:");
		txFdEnderecoHost.setBounds(10, 65, 325, 49);
		contentPanel.add(txFdEnderecoHost);
		
		txFdPortaHost = new LabeledTextField();
		txFdPortaHost.getTextField().setText("5432");
		txFdPortaHost.setTitle("Porta do Host:");
		txFdPortaHost.setBounds(10, 119, 325, 49);
		contentPanel.add(txFdPortaHost);
		
		txFdNmUsuario = new LabeledTextField();
		txFdNmUsuario.getTextField().setText("postgres");
		txFdNmUsuario.setTitle("Usuário:");
		txFdNmUsuario.setBounds(10, 173, 325, 49);
		contentPanel.add(txFdNmUsuario);
		
		txFdSenha = new LabeledTextField();
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
						if (!editMode) {
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
							
						} else if (server != null) {
							
							server.setServerName(txFdNmServidor.getInput());
							server.setHost(txFdEnderecoHost.getInput());
							server.setPassword(encryptedPassword);
							server.setPort(Integer.parseInt(txFdPortaHost.getInput()));
							server.setUsername(txFdNmUsuario.getInput());
							try {
								ServicesFacade.getServices().getServerConfigurationHandler().save();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void setInitializeServerConfiguration(Server server) {
		this.server = server;
		txFdNmUsuario.getTextField().setText(server.getUsername());
		txFdNmServidor.getTextField().setText(server.getServerName());
		txFdEnderecoHost.getTextField().setText(server.getHost());
		txFdPortaHost.getTextField().setText("" + server.getPort());
	}
}
