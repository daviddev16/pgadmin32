package com.daviddev16;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.daviddev16.component.ScrollableTableViewer;
import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.component.dialog.FrmServerConnection;
import com.daviddev16.component.editor.TextEditor;
import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.ResourceLocator;
import com.daviddev16.core.TimeCounterWatcher;
import com.daviddev16.core.component.ResultSetTableViewer;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.node.Database;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.service.configuration.ServerConfiguration;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.SpringLayout;

public class FrmApplicationMain extends JFrame {

	private static FrmApplicationMain mainUI;
	private final ServerConfiguration serverConfiguration; 
	private final ResourceLocator resourceLocator;

	{
		serverConfiguration = ServicesFacade.getServices()
				.getServerConfigurationHandler().getHandledConfiguration();

		resourceLocator = ServicesFacade.getServices()
				.getFileResourceLocator();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSplitPane splitPane;
	private ServerTreeViewer serverTreeViewer;
	private TableViewer dataSetTable;
	private ScrollableTableViewer table;
	private JPanel panel_5;
	private JTabbedPane tabbedPane_1;
	ResultSetTableViewer resultSetTableViewer ;
	public TextEditor sntxtxtrNoSql;
	public JTextPane textPane;
	private JSplitPane splitPane_1;
	private RTextScrollPane scrollPane_1;
	private JPanel panel_4;
	private JScrollPane scrollPane_2;
	private JPanel panel_3;
	private ScrollableTableViewer scrollableTableViewer;
	private JPanel panel_1;
	private JToolBar toolBar;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JPanel panel_2;
	private JToolBar toolBar_1;
	private JLabel lblFooterInfo;

	/**
	 * Create the frame.
	 */
	public FrmApplicationMain() {


		mainUI = this;
		setTitle("PostgreSQL Admin 3.2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 735);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		toolBar = new JToolBar();
		toolBar.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(3, 2, 3, 2)));
		
		splitPane = new JSplitPane();
		splitPane.setDividerSize(10);
		splitPane.setOpaque(false);
		splitPane.setOneTouchExpandable(true);

		splitPane.setFocusTraversalPolicyProvider(true);
		splitPane.setFocusable(false);
		splitPane.setBorder(null);
		splitPane.setResizeWeight(0.1);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
				.addComponent(toolBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
				.addComponent(splitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
		);
		SpringLayout sl_panel_6 = new SpringLayout();
		panel_6.setLayout(sl_panel_6);
		
		lblFooterInfo = new JLabel("Hello, world!");
		lblFooterInfo.setBorder(new EmptyBorder(0, 0, 0, 6));
		sl_panel_6.putConstraint(SpringLayout.NORTH, lblFooterInfo, -1, SpringLayout.NORTH, panel_6);
		sl_panel_6.putConstraint(SpringLayout.WEST, lblFooterInfo, -1, SpringLayout.WEST, panel_6);
		sl_panel_6.putConstraint(SpringLayout.SOUTH, lblFooterInfo, -1, SpringLayout.SOUTH, panel_6);
		sl_panel_6.putConstraint(SpringLayout.EAST, lblFooterInfo, -1, SpringLayout.EAST, panel_6);
		lblFooterInfo.setHorizontalAlignment(SwingConstants.TRAILING);
		panel_6.add(lblFooterInfo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setOpaque(false);
		scrollPane.setFocusable(false);
		scrollPane.setBorder(null);
		splitPane.setLeftComponent(scrollPane);


		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		tabbedPane.setOpaque(false);

		panel_2 = new JPanel();
		tabbedPane.addTab("Query Rápida", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));

		serverTreeViewer = new ServerTreeViewer();


		serverTreeViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 ) {
					DatabaseDataObject databaseDataObject = serverTreeViewer.getLastSelectedDatabaseDataObject();
					if (databaseDataObject != null) {
						Database database = getInheritedServerDatabase(databaseDataObject);
						if (database == null) {
							return;
						}
						tabbedPane.setTitleAt(0,  "Query Rápida em " + database.getDatabaseName() );

					}
				}
			}
		});
		scrollPane.setViewportView(serverTreeViewer);
		serverTreeViewer.setBorder(new LineBorder(Color.LIGHT_GRAY));
		final Color treeBorderLine = ((LineBorder)serverTreeViewer.getBorder()).getLineColor();
		serverTreeViewer.setShowsRootHandles(true);
		serverTreeViewer.setBorder(new EmptyBorder(1, 1, 1, 1));
		serverTreeViewer.setRowHeight(20);


		toolBar_1 = new JToolBar();
		toolBar_1.setRollover(true);
		toolBar_1.setBorder(new EmptyBorder(2, 1, 2, 1));

		scrollPane.setColumnHeaderView(toolBar_1);
		scrollPane.setBorder(new LineBorder(treeBorderLine, 1));

		JButton btnCollapseTree = new JButton("");
		btnCollapseTree.setToolTipText("Collapse");
		btnCollapseTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 2; i < serverTreeViewer.getRowCount(); i++) {				
					if (serverTreeViewer.isExpanded(i)) {
						serverTreeViewer.collapseRow(i);
					}
				}
			}
		});
		btnCollapseTree.setIcon(resourceLocator.cachedImageIcon("Collapse16px"));
		toolBar_1.add(btnCollapseTree);

		JButton btnNewButton = new JButton("");
		btnNewButton.setToolTipText("New Server...");
		btnNewButton.setHorizontalTextPosition(SwingConstants.CENTER);
		btnNewButton.setVerticalAlignment(SwingConstants.BOTTOM);
		btnNewButton.setActionCommand("");
		toolBar_1.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmServerConnection dialog = new FrmServerConnection();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		btnNewButton.setIcon(resourceLocator.cachedImageIcon("Server16px"));

		panel = new JPanel();
		
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setRightComponent(panel);


		panel_3 = new JPanel();
		panel_3.setFocusable(false);
		panel_3.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("Propriedades", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));

		dataSetTable = new TableViewer();
		dataSetTable.setBorder(null);
		scrollableTableViewer = new ScrollableTableViewer(dataSetTable);
		scrollableTableViewer.setFocusable(false);
		panel_3.add(scrollableTableViewer);

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("Estatisticas", null, panel_1, null);

		splitPane_1 = new JSplitPane();

		splitPane_1.setResizeWeight(0.7);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel_2.add(splitPane_1);

		TextEditor sntxtxtrSelectnnspnamecrelowner = new TextEditor();
		sntxtxtrSelectnnspnamecrelowner.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					String selectedQuery = sntxtxtrSelectnnspnamecrelowner.getSelectedText();
					DatabaseDataObject databaseDataObject = serverTreeViewer.getLastSelectedDatabaseDataObject();
					try {
						if (databaseDataObject != null) {
							queryTableToUI(selectedQuery, databaseDataObject);
						}
					} catch (Exception e1) {
						textPane.setText(e1.getMessage());
						e1.printStackTrace();
					}
				}
			}
		});
		sntxtxtrSelectnnspnamecrelowner.setTabSize(2);
		sntxtxtrSelectnnspnamecrelowner.setText("SELECT\r\n\tn.nspname, \r\n\tc.relowner, \r\n\tc.relname, \r\n\ta.attname \r\nFROM "
				+ "\r\n\tpg_attribute a \r\nLEFT JOIN \r\n\tpg_class c \r\nON \r\n\t(c.oid = a.attrelid) \r\nLEFT JOIN\r\n\tpg_namespace n "
				+ "\r\nON \r\n\t(n.oid = c.relnamespace)\r\nWHERE \r\n\trelname = 'wshop';");
		sntxtxtrSelectnnspnamecrelowner.setFont(new Font("Consolas", Font.PLAIN, 13));
		sntxtxtrSelectnnspnamecrelowner.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		sntxtxtrSelectnnspnamecrelowner.setLineWrap(true);
		scrollPane_1 = new RTextScrollPane(sntxtxtrSelectnnspnamecrelowner);
		splitPane_1.setLeftComponent(scrollPane_1);

		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		splitPane_1.setRightComponent(tabbedPane_1);

		panel_4 = new JPanel();
		tabbedPane_1.addTab("Mensagem", null, panel_4, null);
		panel_4.setLayout(new BorderLayout(0, 0));

		textPane = new JTextPane();
		textPane.setFont(new Font("Courier New", Font.PLAIN, 11));
		textPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		textPane.setEditable(false);
		panel_4.add(textPane, BorderLayout.CENTER);

		panel_5 = new JPanel();
		tabbedPane_1.addTab("Dados", null, panel_5, null);
		panel_5.setLayout(new BorderLayout(0, 0));

		resultSetTableViewer = new ResultSetTableViewer();
		table = new ScrollableTableViewer(resultSetTableViewer);
		panel_5.add(table);

		scrollPane_2 = new JScrollPane();
		tabbedPane_1.addTab("SQL", null, scrollPane_2, null);

		sntxtxtrNoSql = new TextEditor();
		sntxtxtrNoSql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		sntxtxtrNoSql.setEditable(false);
		sntxtxtrNoSql.setText("-- no SQL script");
		scrollPane_2.setViewportView(sntxtxtrNoSql);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
					.addGap(1))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		restoreDefaults();
		setupAllServers();
		loadAllComponentsCustomColors();
	}

	public ServerTreeViewer getServerTreeViewer() {
		return serverTreeViewer;
	}

	private void queryTableToUI(String query, DatabaseDataObject dataObject) throws Exception {

		TimeCounterWatcher counterWatcher = new TimeCounterWatcher() {
			
			@Override
			public void tick(long currentTimeSpent) {
				lblFooterInfo.setText("Query execution -> Seconds: " + String.format("%02d,%02d", ((currentTimeSpent % 1000) / 1000) % 60, currentTimeSpent % 1000) + 
						"s / Milliseconds: " + currentTimeSpent + "ms");
			}
			
		};
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				ResultSet resultSet = null;
				Statement statement = null;
				try {
					Database database = getInheritedServerDatabase(dataObject);
					if (database == null) {
						return;
					}
					statement = database.getConnection().createStatement();
					counterWatcher.start();
					resultSet = statement.executeQuery(query);
					counterWatcher.stop();
					resultSetTableViewer.setResultSet(resultSet);
				} catch (Exception e) {
					counterWatcher.stop();
				} finally {
					if (statement != null) {
						try {
							statement.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (resultSet != null) {
						try {
							resultSet.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						resultSet = null;
					}
				}				
			}
		}).start();

	}

	public void loadAllComponentsCustomColors() {
		final Color innerComponentBackgroundColor = UIManager.getColor("Style.innerComponentBackgroundColor");
		textPane.setBackground(innerComponentBackgroundColor);
		tabbedPane_1.setBackground(innerComponentBackgroundColor);
		toolBar.setBackground(innerComponentBackgroundColor);
		panel.setBackground(innerComponentBackgroundColor);
		splitPane_1.setBackground(innerComponentBackgroundColor);
		tabbedPane.setBackground(innerComponentBackgroundColor);
		panel_2.setBackground(innerComponentBackgroundColor);
		serverTreeViewer.setBackground(innerComponentBackgroundColor);
		toolBar_1.setBackground(innerComponentBackgroundColor);
	}
	
	private Database getInheritedServerDatabase(DatabaseDataObject databaseDataObject) {
		if (databaseDataObject instanceof Database)
			return (Database) databaseDataObject;
		DatabaseDataObject parentDatabaseDataObject = databaseDataObject.getParent();
		if (parentDatabaseDataObject != null)
			return getInheritedServerDatabase(parentDatabaseDataObject);
		else
			return null;
	}

	public static FrmApplicationMain getMainUI() {
		return mainUI;
	}


	public TableViewer getDataSetTableViewer() {
		return dataSetTable;
	}

	private void setupAllServers() {
		serverConfiguration.getServers().forEach(server -> { 
			serverTreeViewer.addServerToTree(server); 
		});
	}

	private void restoreDefaults() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				splitPane.setDividerLocation(270);
			}
		});
	}
}
