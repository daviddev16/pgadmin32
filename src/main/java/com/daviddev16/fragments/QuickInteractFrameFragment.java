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
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import com.daviddev16.FrmApplicationMain;
import com.daviddev16.component.QueryEditorPanel;
import com.daviddev16.component.ScrollableTablePanel;
import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.component.SwingUtil;
import com.daviddev16.component.dialog.DlgErrorDetails;
import com.daviddev16.component.editor.TextEditor;
import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.TimeCounterWatcher;
import com.daviddev16.core.annotation.Documented;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.component.FrameFragment;
import com.daviddev16.core.component.ResultSetTableViewer;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.core.data.Statistic;
import com.daviddev16.core.data.TableProperty;
import com.daviddev16.core.event.EventPriority;
import com.daviddev16.entity.StatisticResourcedProperty;
import com.daviddev16.entity.TableResourcedProperty;
import com.daviddev16.event.interaction.HandleCreateScriptRequestEvent;
import com.daviddev16.event.interaction.HandleStatisticRequestEvent;
import com.daviddev16.event.interaction.HandleTablePropertyRequestEvent;
import com.daviddev16.event.interaction.QueryRequestEvent;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.listener.TableEventListener;
import com.daviddev16.node.Database;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.style.CustomStylePropertiesHolder;

/**
 * <code>QuickInteractFrameFragment</code> é um painel fragmentado do JFrame principal.
 * Ele é responsável por conter todos os componentes relacionados a visualização rápida
 * do <code>DatabaseDataObject</code> selecionado na árvore de servidores principal.
 * 
 * @see FrmApplicationMain
 */
public final @Documented class QuickInteractFrameFragment extends FrameFragment<FrmApplicationMain> {

	private TableViewer dataSetTable;
	private TableViewer dataSetTable1;
	
	private ScrollableTablePanel table;
	private JPanel panel_5;
	private JTabbedPane tbPnQueryDados;
	private ResultSetTableViewer resultSetTableViewer ;
	private TextEditor sntxtxtrNoSql;
	private JTextPane textPane;
	private JSplitPane splitPane_1;
	private JScrollPane scrollPane_2;
	private ScrollableTablePanel scrStatisticsTableViewer;
	private JPanel pnStatistics;
	private JPanel pnQueryExecutor;

	private ServerTreeViewer mainServerTreeViewer;


	private static final long serialVersionUID = -5034141671685089553L;
	private JPanel panel;
	private JSplitPane spPnProperties;
	private JTabbedPane tbPnSQL;
	private ScrollableTablePanel scrPropertiesTableViewer;

	public QuickInteractFrameFragment(FrmApplicationMain directParentContainer) {
		super(directParentContainer);
		setToolTipText("Quick Interact Window");

		if (directParentContainer != null)
			mainServerTreeViewer = directParentContainer.getServerTreeViewer();

		setPreferredSize(new Dimension(620, 607));
		setLayout(new BorderLayout(0, 0));

		JTabbedPane tbPnPrincipal = new JTabbedPane(JTabbedPane.TOP);
		tbPnPrincipal.setToolTipText(getToolTipText());
		add(tbPnPrincipal, BorderLayout.CENTER);

		tbPnPrincipal.setOpaque(false);

		spPnProperties = new JSplitPane();
		spPnProperties.setDividerSize(10);
		spPnProperties.setResizeWeight(0.1);
		spPnProperties.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spPnProperties.setRightComponent(tbPnSQL);
		spPnProperties.setDividerLocation(300);

		pnStatistics = new JPanel();
		pnStatistics.setLayout(new BorderLayout(0, 0));

		tbPnSQL = new JTabbedPane(JTabbedPane.TOP);

		dataSetTable = new TableViewer();
		scrStatisticsTableViewer = new ScrollableTablePanel(dataSetTable);
		scrStatisticsTableViewer.setFocusable(false);
		pnStatistics.add(scrStatisticsTableViewer);

		pnQueryExecutor = new JPanel();
		textPane = new JTextPane();
		textPane.setEditable(false);

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

		tbPnQueryDados = new JTabbedPane(JTabbedPane.TOP);
		tbPnQueryDados.setOpaque(true);
		tbPnQueryDados.setFocusable(false);
		splitPane_1.setRightComponent(tbPnQueryDados);


		panel_5 = new JPanel();
		tbPnQueryDados.addTab("Dados", null, panel_5, null);
		panel_5.setLayout(new BorderLayout(0, 0));

		resultSetTableViewer = new ResultSetTableViewer();
		table = new ScrollableTablePanel(resultSetTableViewer);
		panel_5.add(table);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setFocusable(false);

		sntxtxtrNoSql = new TextEditor();
		sntxtxtrNoSql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		sntxtxtrNoSql.setEditable(false);
		sntxtxtrNoSql.setText("-- no SQL script");
		scrollPane_2.setViewportView(sntxtxtrNoSql);

		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(textPane);

		tbPnQueryDados.addTab("Mensagem", null, panel, null);

		tbPnSQL.addTab("SQL", null, scrollPane_2, null);
		spPnProperties.setRightComponent(tbPnSQL);
		
		tbPnPrincipal.addTab("Propriedades", null, spPnProperties, null);
		
		dataSetTable1 = new TableViewer();
		scrPropertiesTableViewer = new ScrollableTablePanel(dataSetTable1);
		spPnProperties.setLeftComponent(scrPropertiesTableViewer);
		scrPropertiesTableViewer.setFocusable(false);

		tbPnPrincipal.addTab("Estatisticas", null, pnStatistics, null);
		tbPnPrincipal.addTab("Query Rápida", null, pnQueryExecutor, null);

		getMainServerTreeViewer().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2 ) {
					DatabaseDataObject databaseDataObject = getMainServerTreeViewer().getLastSelectedDatabaseDataObject();
					if (databaseDataObject != null) {
						Database database = getMainServerTreeViewer().getInheritedServerDatabase(databaseDataObject);
						if (database == null) {
							return;
						}
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
					tbPnQueryDados.setSelectedIndex(0);
					ok("Query finalizada sem erros.");
				} catch (Exception e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					error(e);
					tbPnQueryDados.setSelectedIndex(1);
					tbPnQueryDados.revalidate();
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

	public void error(Exception exception) {
		DlgErrorDetails.showForExcetion(exception);
	}
	
	@EventHandler
	public void onHandleCreateScriptRequestEvent(HandleCreateScriptRequestEvent createScriptRequestEvent) 
	{
		if (createScriptRequestEvent.getSender() instanceof TableEventListener)
			sntxtxtrNoSql.setText(createScriptRequestEvent.getSQLScript());
	}

	@EventHandler
	public void onHandleTablePropertyRequestEvent(HandleTablePropertyRequestEvent tablePropertyRequestEvent) 
	{

		List<TableProperty> tableProperties = tablePropertyRequestEvent.getTableProperties();
		
		final DefaultTableModel defaultTableModel = new DefaultTableModel() {
			private static final long serialVersionUID = -6901805822459648889L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dataSetTable1.setColumnModel(new DefaultTableColumnModel());
		dataSetTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		defaultTableModel.setColumnIdentifiers(new String[] {"Property", "Value"});
		int[] widths = new int[tableProperties.size()];
		int j = 0;
		for (TableProperty tableProperty : tableProperties) {
			Vector<Object> dataVector = new Vector<Object>();
			TableResourcedProperty tableResourcedProperty = new TableResourcedProperty(tableProperty);
			dataVector.add(tableResourcedProperty);
			String descricaoStat = tableResourcedProperty.getNodeName();
			int fontStrWidth = dataSetTable1.getFontMetrics(dataSetTable1.getFont()).stringWidth(descricaoStat) + 40;
			if (widths[j] < fontStrWidth) {
				widths[j] = fontStrWidth;
			}
			j++;
			dataVector.add(tableProperty.getPropertyValue().toString());
			defaultTableModel.addRow(dataVector);
		}

		dataSetTable1.setModel(defaultTableModel);
		TableColumn column = null;
		for (int i = 0; i < dataSetTable1.getColumnModel().getColumnCount(); i++) {
			column = dataSetTable1.getColumnModel().getColumn(i);
			column.setPreferredWidth(widths[i]);
		}
		dataSetTable1.revalidate();
	}
	
	@EventHandler
	public void onHandleStatisticRequestEvent(HandleStatisticRequestEvent statisticRequestEvent) 
	{
		List<Statistic> statistics = statisticRequestEvent.getStatistics();
		
		final DefaultTableModel defaultTableModel = new DefaultTableModel() {
			private static final long serialVersionUID = -6901805822459648889L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dataSetTable.setColumnModel(new DefaultTableColumnModel());
		dataSetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		defaultTableModel.setColumnIdentifiers(new String[] {"Statistic", "Value"});
		int[] widths = new int[statistics.size()];
		int j = 0;
		for (Statistic statistic : statistics) {
			Vector<Object> dataVector = new Vector<Object>();
			boolean showStatisticBracketInformation = ServicesFacade.getServices()
					.getOptionsConfiguration().isStatisticBracketInformationEnabled();
			StatisticResourcedProperty statisticResourcedProperty = new StatisticResourcedProperty(statistic, showStatisticBracketInformation);
			dataVector.add(statisticResourcedProperty);
			String descricaoStat = statisticResourcedProperty.getNodeName();
			int fontStrWidth = dataSetTable.getFontMetrics(dataSetTable.getFont()).stringWidth(descricaoStat) + 40;
			if (widths[j] < fontStrWidth) {
				widths[j] = fontStrWidth;
			}
			j++;
			dataVector.add(statistic.getPropertyValue().toString());
			defaultTableModel.addRow(dataVector);
		}

		dataSetTable.setModel(defaultTableModel);
		TableColumn column = null;
		for (int i = 0; i < dataSetTable.getColumnModel().getColumnCount(); i++) {
			column = dataSetTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(widths[i]);
		}
		dataSetTable.revalidate();
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onChangedStyleStateEvent(ChangedStyleStateEvent styleStateEvent) 
	{
		configureComponentsStyleConfiguration(new CustomStylePropertiesHolder());
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
			error(e1);
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

		textPane   .setBackground(customInnerBackgroundColor);
		splitPane_1.setBackground(customInnerBackgroundColor);
		
		pnQueryExecutor     .setBackground(customInnerBackgroundColor);
		spPnProperties		.setBackground(customInnerBackgroundColor);
		pnStatistics		.setBackground(customInnerBackgroundColor);
		
		tbPnQueryDados .setBorder(new MatteBorder(1, 0, 0, 0, outerComponentLineColor));
		textPane     .setBorder(new EmptyBorder(10, 10, 10, 10));
		sntxtxtrNoSql.setBorder(SwingUtil.INNER_10_INSETS_EMPTY);
		
		final LineBorder commonLineBorder = 
				new LineBorder(outerComponentLineColor, 1);

		setBorder(commonLineBorder);
		
		dataSetTable.setBorder(null);
	}



	public ServerTreeViewer getMainServerTreeViewer() {
		return mainServerTreeViewer;
	}

}
