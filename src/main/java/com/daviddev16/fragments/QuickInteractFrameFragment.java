package com.daviddev16.fragments;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import com.daviddev16.FrmApplicationMain;
import com.daviddev16.component.QueryEditorPanel;
import com.daviddev16.component.ScrollableTableViewer;
import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.component.editor.TextEditor;
import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.TimeCounterWatcher;
import com.daviddev16.core.annotation.Documented;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.component.FrameFragment;
import com.daviddev16.core.component.ResultSetTableViewer;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.core.event.EventPriority;
import com.daviddev16.event.query.QueryRequestEvent;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.node.Database;
import com.daviddev16.style.CustomStylePropertiesHolder;
import com.formdev.flatlaf.FlatLaf;

/**
 * <code>QuickInteractFrameFragment</code> é um painel fragmentado do JFrame principal.
 * Ele é responsável por conter todos os componentes relacionados a visualização rápida
 * do <code>DatabaseDataObject</code> selecionado na árvore de servidores principal.
 * 
 * @see FrmApplicationMain
 */
public final @Documented class QuickInteractFrameFragment extends FrameFragment<FrmApplicationMain> {

	private TableViewer dataSetTable;
	private ScrollableTableViewer table;
	private JPanel panel_5;
	private JTabbedPane tabbedPane_1;
	private ResultSetTableViewer resultSetTableViewer ;
	private TextEditor sntxtxtrNoSql;
	private JTextPane textPane;
	private JSplitPane splitPane_1;
	private JScrollPane scrollPane_2;
	private JPanel pnProperties;
	private ScrollableTableViewer scrollableStatisticsTableViewer;
	private JPanel pnStatistics;
	private JPanel pnQueryExecutor;

	private ServerTreeViewer mainServerTreeViewer;


	private static final long serialVersionUID = -5034141671685089553L;
	private JPanel panel;

	public QuickInteractFrameFragment(FrmApplicationMain directParentContainer) {
		super(directParentContainer);

		if (directParentContainer != null)
			mainServerTreeViewer = directParentContainer.getServerTreeViewer();

		setPreferredSize(new Dimension(620, 607));
		setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setOpaque(false);

		pnProperties = new JPanel();
		pnProperties.setFocusable(false);

		tabbedPane.addTab("Propriedades", null, pnProperties, null);
		pnProperties.setLayout(new BorderLayout(0, 0));

		pnQueryExecutor = new JPanel();
		tabbedPane.addTab("Query Rápida", null, pnQueryExecutor, null);

		textPane = new JTextPane();

		pnStatistics = new JPanel();

		tabbedPane.addTab("Estatisticas", null, pnStatistics, null);

		dataSetTable = new TableViewer();

		pnStatistics.setLayout(new BorderLayout(0, 0));
		scrollableStatisticsTableViewer = new ScrollableTableViewer(dataSetTable);
		scrollableStatisticsTableViewer.setFocusable(false);
		pnStatistics.add(scrollableStatisticsTableViewer);
		pnQueryExecutor.setLayout(new BorderLayout(0, 0));


		splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(10);
		splitPane_1.setOneTouchExpandable(true);
		splitPane_1.setContinuousLayout(true);

		splitPane_1.setResizeWeight(0.1);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pnQueryExecutor.add(splitPane_1);


		QueryEditorPanel queryExecutorPanel = new QueryEditorPanel();
		splitPane_1.setLeftComponent(queryExecutorPanel);
		splitPane_1.setDividerLocation(370);

		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setOpaque(true);
		tabbedPane_1.setFocusable(false);
		splitPane_1.setRightComponent(tabbedPane_1);


		panel_5 = new JPanel();
		tabbedPane_1.addTab("Dados", null, panel_5, null);
		panel_5.setLayout(new BorderLayout(0, 0));

		resultSetTableViewer = new ResultSetTableViewer();
		table = new ScrollableTableViewer(resultSetTableViewer);
		panel_5.add(table);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setFocusable(false);
		tabbedPane_1.addTab("SQL", null, scrollPane_2, null);

		sntxtxtrNoSql = new TextEditor();
		sntxtxtrNoSql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		sntxtxtrNoSql.setEditable(false);
		sntxtxtrNoSql.setText("-- no SQL script");
		scrollPane_2.setViewportView(sntxtxtrNoSql);

		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(textPane);
		tabbedPane_1.addTab("New tab", null, panel, null);


		getMainServerTreeViewer().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 ) {
					tabbedPane_1.setSelectedIndex(2);
					DatabaseDataObject databaseDataObject = getMainServerTreeViewer().getLastSelectedDatabaseDataObject();
					if (databaseDataObject != null) {
						Database database = getMainServerTreeViewer().getInheritedServerDatabase(databaseDataObject);
						if (database == null) {
							return;
						}
						tabbedPane.setTitleAt(0,  "Query Rápida em " + database.getDatabaseName() );

					}
				}
			}
		});


	}

	private void queryTableToUI(String query, DatabaseDataObject dataObject) throws Exception {
		TimeCounterWatcher counterWatcher = new TimeCounterWatcher() {
			@Override
			public void tick(long currentTimeSpent) {
				System.out.println("Query execution -> Seconds: " + String.format("%02d,%02d", ((currentTimeSpent % 1000) / 1000) % 60, currentTimeSpent % 1000) + 
						"s / Milliseconds: " + currentTimeSpent + "ms");
			}
		};

		new Thread(new Runnable() {
			@Override
			public void run() {
				ResultSet resultSet = null;
				Statement statement = null;
				try {
					Database database = getMainServerTreeViewer().getInheritedServerDatabase(dataObject);
					if (database == null) {
						return;
					}
					statement = database.getConnection().createStatement();
					counterWatcher.start();
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					resultSet = statement.executeQuery(query);
					counterWatcher.stop();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					resultSetTableViewer.setResultSet(resultSet);
					tabbedPane_1.setSelectedIndex(1);
					ok("Query finalizada sem erros.");
				} catch (Exception e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					error(e.getMessage());
					tabbedPane_1.setSelectedIndex(0);
					tabbedPane_1.revalidate();
					counterWatcher.stop();
				} finally {
					if (statement != null) {
						try {
							statement.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						statement = null;
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

					System.gc();
				}				
			}
		}).start();

	}


	public void say(String message, Color fgColor) {
		textPane.setText(message);
		textPane.setForeground(fgColor);
	}

	public void ok(String message) {
		say(message, Color.GREEN);
	}

	public void error(String message) {
		say(message, Color.RED);
	}


	@EventHandler(priority = EventPriority.MEDIUM)
	public void onQueryRequestEvent(QueryRequestEvent queryRequestEvent) {

		if (!(queryRequestEvent.getSender() instanceof QueryEditorPanel))
			return;

		DatabaseDataObject databaseDataObject = getMainServerTreeViewer().getLastSelectedDatabaseDataObject();
		try {
			if (databaseDataObject != null) {
				queryTableToUI(queryRequestEvent.getQueryText(), databaseDataObject);
			} else {
				throw new IllegalStateException("Não é possível executar uma query sem um conector selecionado.");
			}
		} catch (Exception e1) {
			error("[" + e1.getClass().getSimpleName() + "] " +  e1.getMessage());
			e1.printStackTrace();
		}

	}

	@Override
	public void configureComponentsStyleConfiguration(CustomStylePropertiesHolder stylePropertiesHolder)
	{
		final Color customInnerBackgroundColor = 
				stylePropertiesHolder.getInnerBackgroundColor();
		
		final Color outerComponentLineColor = 
				stylePropertiesHolder.getOuterComponentLineColor();
		
		FlatLaf.updateUI();

		textPane   .setBackground(customInnerBackgroundColor);
		splitPane_1.setBackground(customInnerBackgroundColor);
		pnQueryExecutor    .setBackground(customInnerBackgroundColor);
		pnProperties		.setBackground(customInnerBackgroundColor);
		pnStatistics		.setBackground(customInnerBackgroundColor);

		dataSetTable.setBorder(null);
		tabbedPane_1.setBorder(new MatteBorder(1, 0, 0, 0, outerComponentLineColor));
		textPane    .setBorder(new EmptyBorder(10, 10, 10, 10));

		final LineBorder commonLineBorder = 
				new LineBorder(outerComponentLineColor, 1);

		setBorder(commonLineBorder);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChangedStyleStateEvent(ChangedStyleStateEvent styleStateEvent) 
	{
		configureComponentsStyleConfiguration(new CustomStylePropertiesHolder());
	}

	public ServerTreeViewer getMainServerTreeViewer() {
		return mainServerTreeViewer;
	}

}
