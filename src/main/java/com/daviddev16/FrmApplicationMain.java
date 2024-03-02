package com.daviddev16;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.daviddev16.component.BorderlessButton;
import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.component.StyleConfiguratorComboBox;
import com.daviddev16.component.dialog.FrmServerConnection;
import com.daviddev16.core.CustomStyleComponentConfiguration;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.ResourceLocator;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.component.ResultSetTableViewer;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.core.event.EventPriority;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.fragments.QuickInteractFrameFragment;
import com.daviddev16.service.EventManager;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.service.configuration.ServerConfiguration;
import com.daviddev16.style.CustomStylePropertiesHolder;
import com.daviddev16.style.StyleManager;

public class FrmApplicationMain extends JFrame implements EventListener, 
														  CustomStyleComponentConfiguration,
														  ActionListener {

	public static final String BTN_ACT_ADD_SERVER_COMMAND = "button.addserver.command";
	public static final String BTN_ACT_COLLAPSE_COMMAND   = "button.collapse.command";
	
	private static FrmApplicationMain mainUI;
	private final ServerConfiguration serverConfiguration; 
	private final ResourceLocator resourceLocator;
	private final StyleManager styleManager;
	private final EventManager eventManager;

	{
		styleManager = ServicesFacade.getServices()
				.getStyleManager();
		
		serverConfiguration = ServicesFacade.getServices()
				.getServerConfigurationHandler().getHandledConfiguration();

		resourceLocator = ServicesFacade.getServices()
				.getFileResourceLocator();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSplitPane treeSplitPane;
	private ServerTreeViewer serverTreeViewer;
	private TableViewer dataSetTable;
	ResultSetTableViewer resultSetTableViewer ;
	private JToolBar headerToolBar;
	private JToolBar serverTreeToolBar;
	private JLabel lblFooterInfo;
	private JPanel footerPanel;
	private JScrollPane treeScrollPane;
	private QuickInteractFrameFragment quickInteractFrameFragment;
	private BorderlessButton btnCollapseTree;
	private BorderlessButton btnAddServer;
	private StyleConfiguratorComboBox cmbBxStyleSelector;
  	
	public FrmApplicationMain() {
		mainUI = this;
		
		eventManager = ServicesFacade.getServices().getEventManager();
		eventManager.registerListener(this);

		
		setTitle("pgAdmin32");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImages(createImageListOfPostgresAdminIcons());
		setBounds(100, 100, 920, 756);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		setContentPane(contentPane);

		headerToolBar = new JToolBar();

		treeSplitPane = new JSplitPane();
		treeSplitPane.setOneTouchExpandable(true);
		treeSplitPane.setDividerSize(10);
		treeSplitPane.setOpaque(false);
		treeSplitPane.setResizeWeight(0.1);
		treeSplitPane.setFocusable(false);

		footerPanel = new JPanel();
	
		/*TODO:*/
		cmbBxStyleSelector = new StyleConfiguratorComboBox();
    	cmbBxStyleSelector.loadAllStyles(styleManager.getAsListOfStyleConfigurators());
		cmbBxStyleSelector.setMaximumSize(new Dimension(200, 40));
		headerToolBar.add(cmbBxStyleSelector);
		/*TODO:*/
		
		SpringLayout sl_footerPanel = new SpringLayout();

		lblFooterInfo = new JLabel("Hello, world!");
		lblFooterInfo.setHorizontalAlignment(SwingConstants.TRAILING);
		footerPanel.add(lblFooterInfo);

		sl_footerPanel.putConstraint(SpringLayout.NORTH, lblFooterInfo, -1, SpringLayout.NORTH, footerPanel);
		sl_footerPanel.putConstraint(SpringLayout.WEST, lblFooterInfo, -1, SpringLayout.WEST, footerPanel);
		sl_footerPanel.putConstraint(SpringLayout.SOUTH, lblFooterInfo, -1, SpringLayout.SOUTH, footerPanel);
		sl_footerPanel.putConstraint(SpringLayout.EAST, lblFooterInfo, -1, SpringLayout.EAST, footerPanel);

		serverTreeViewer = new ServerTreeViewer();
		serverTreeViewer.setShowsRootHandles(true);
		serverTreeViewer.setRowHeight(20);

		serverTreeToolBar = new JToolBar();

		treeScrollPane = new JScrollPane();
		treeScrollPane.setFocusable(false);
		treeScrollPane.setViewportView(serverTreeViewer);
		

		treeScrollPane.setColumnHeaderView(serverTreeToolBar);
		treeSplitPane.setLeftComponent(treeScrollPane);
		
		btnCollapseTree = new BorderlessButton();
		btnCollapseTree.setIcon(resourceLocator.cachedImageIcon("Collapse16px"));
		btnCollapseTree.setToolTipText("Collapse");
		btnCollapseTree.addActionListener(this);
		
		btnAddServer = new BorderlessButton();
		btnAddServer.setIcon(resourceLocator.cachedImageIcon("Server16px"));
		btnAddServer.setToolTipText("New Server...");
		btnAddServer.setActionCommand(BTN_ACT_ADD_SERVER_COMMAND);
		btnAddServer.addActionListener(this);

		quickInteractFrameFragment = new QuickInteractFrameFragment(this);
		treeSplitPane.setRightComponent(quickInteractFrameFragment);

		dataSetTable = new TableViewer();
		resultSetTableViewer = new ResultSetTableViewer();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(footerPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
				.addComponent(headerToolBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
				.addComponent(treeSplitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(headerToolBar, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(treeSplitPane, GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(footerPanel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
				);

		serverTreeToolBar.add(btnCollapseTree);
		serverTreeToolBar.add(btnAddServer);
		
		serverTreeToolBar.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		headerToolBar    .setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		contentPane      .setLayout(gl_contentPane);
		footerPanel      .setLayout(sl_footerPanel);

		restoreDefaults();
		setupAllServers();
	}

	@Override
	public void configureComponentsStyleConfiguration(CustomStylePropertiesHolder stylePropertiesHolder) 
	{
		final Color customInnerBackgroundColor = 
				stylePropertiesHolder.getInnerBackgroundColor();
		
		final Color outerComponentLineColor = 
				stylePropertiesHolder.getOuterComponentLineColor();

		serverTreeToolBar.setOpaque(true);

		headerToolBar    .setBackground(customInnerBackgroundColor);
		serverTreeViewer .setBackground(customInnerBackgroundColor);
		serverTreeToolBar.setBackground(customInnerBackgroundColor);
		treeScrollPane   .setBackground(customInnerBackgroundColor);
		serverTreeToolBar.setBackground(customInnerBackgroundColor);


		final LineBorder commonLineBorder = 
				new LineBorder(outerComponentLineColor, 1);

		footerPanel     .setBorder(commonLineBorder);
		treeScrollPane  .setBorder(commonLineBorder);
		serverTreeViewer.setBorder(commonLineBorder);

		lblFooterInfo    .setBorder(new EmptyBorder(0, 0, 0, 6));
		serverTreeViewer .setBorder(new EmptyBorder(1, 1, 1, 1));
		serverTreeToolBar.setBorder(new EmptyBorder(2, 1, 2, 1));
		contentPane      .setBorder(new EmptyBorder(5, 5, 5, 5));
		
		headerToolBar    .setBorder(new CompoundBorder(new LineBorder(outerComponentLineColor), 
													   new EmptyBorder(3, 2, 3, 2)));
		treeSplitPane.setBorder(null);
		dataSetTable .setBorder(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChangedStyleStateEvent(ChangedStyleStateEvent styleStateEvent) 
	{
		CustomStylePropertiesHolder customStylePropertiesHolder = styleStateEvent.getCustomStylePropertiesHolder();
		configureComponentsStyleConfiguration(customStylePropertiesHolder);	
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChangedStyleStateLastEvent(ChangedStyleStateEvent styleStateEvent) 
	{
		SwingUtilities.updateComponentTreeUI(this);
		System.out.println("kakaokoka");
	}

	private List<Image> createImageListOfPostgresAdminIcons() {
		return Arrays.asList(
				resourceLocator.image("pgadmin1"), resourceLocator.image("pgadmin2"), 
				resourceLocator.image("pgadmin3"), resourceLocator.image("pgadmin4")
				);
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
				treeSplitPane.setDividerLocation(270);
			}
		});
	}

	public ServerTreeViewer getServerTreeViewer() {
		return serverTreeViewer;
	}

	public static FrmApplicationMain getMainUI() {
		return mainUI;
	}


	public TableViewer getDataSetTableViewer() {
		return dataSetTable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == BTN_ACT_ADD_SERVER_COMMAND) 
		{
			FrmServerConnection dialog = new FrmServerConnection(false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} 
		else if (e.getActionCommand() == BTN_ACT_COLLAPSE_COMMAND) 
		{
			for (int i = 2; i < serverTreeViewer.getRowCount(); i++) {				
				if (serverTreeViewer.isExpanded(i)) {
					serverTreeViewer.collapseRow(i);
				}
			}
		}
		
	}

	
}
