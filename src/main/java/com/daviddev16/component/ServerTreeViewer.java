package com.daviddev16.component;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.daviddev16.component.dialog.FrmServerConnection;
import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.EntityDataNode;
import com.daviddev16.core.NodeState;
import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.core.component.event.TreeNodeInteractEvent;
import com.daviddev16.core.component.event.TreeNodeInteractionType;
import com.daviddev16.event.server.ColumnNodeInteractEvent;
import com.daviddev16.event.server.ConstraintsGroupNodeInteractEvent;
import com.daviddev16.event.server.DatabaseNodeInteractEvent;
import com.daviddev16.event.server.IndexGroupNodeInteractEvent;
import com.daviddev16.event.server.SchemaGroupNodeInteractEvent;
import com.daviddev16.event.server.SchemaNodeInteractEvent;
import com.daviddev16.event.server.SequenceGroupNodeInteractEvent;
import com.daviddev16.event.server.ServerNodeInteractEvent;
import com.daviddev16.event.server.TableNodeInteractEvent;
import com.daviddev16.node.Column;
import com.daviddev16.node.Database;
import com.daviddev16.node.Schema;
import com.daviddev16.node.Server;
import com.daviddev16.node.Table;
import com.daviddev16.node.group.ConstraintsGroup;
import com.daviddev16.node.group.IndexesGroup;
import com.daviddev16.node.group.SchemaGroup;
import com.daviddev16.node.group.SequencesGroup;
import com.daviddev16.node.group.ServersHierachyGroup;
import com.daviddev16.service.EventManager;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.util.TreeExpansionUtil;

public class ServerTreeViewer extends TreeViewer {

	private static final long serialVersionUID = 2647424773078682960L;

	private final EventManager eventManager = 
			ServicesFacade.getServices().getEventManager();

	private AtomicReference<DatabaseDataObject> lastSelectedDatabaseDataObject;
	private DefaultMutableTreeNode rootTreeNode;
	private ResourcedEntityDataNode lastSelectedNode;
	public TreeExpansionUtil expansionUtil;
	
	private JPopupMenu popupMenu;
	private JMenuItem mntmNewMenuItem;
	
	@Override
	public void initilize() {
		expansionUtil = new TreeExpansionUtil(this);
		lastSelectedDatabaseDataObject = new AtomicReference<DatabaseDataObject>();
		rootTreeNode = createTreeNodeByEntityData(new ServersHierachyGroup());
		getDefaultModel().setRoot(rootTreeNode);

		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)){
					int selRow = getRowForLocation(e.getX(), e.getY());
					TreePath selPath = getPathForLocation(e.getX(), e.getY());
					setSelectionPath(selPath); 
					if (selRow>-1){
						setSelectionRow(selRow); 
					}
				}
			}
		};
		addMouseListener(ml);
		
		popupMenu = new JPopupMenu();
				
		
		mntmNewMenuItem = new JMenuItem("Propriedades");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				if (clickedNode == null)
					return;

				ResourcedEntityDataNode entityDataNode = (ResourcedEntityDataNode) clickedNode.getUserObject();
				
				System.out.println(entityDataNode);
				if (entityDataNode instanceof Server) {
					Server editedServer = (Server) entityDataNode;
					FrmServerConnection dialog = new FrmServerConnection(true);
					dialog.setInitializeServerConfiguration(editedServer);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					
					System.out.println(editedServer.getNodeState());
					
					if (editedServer.getNodeState() == NodeState.CHANGED) {
						clickedNode.removeAllChildren();
						reloadAndRestoreExpandedState(clickedNode);
						System.out.println("a"+editedServer.getHost());
					}

					revalidate();
					
				}
			}
		});
		popupMenu.add(mntmNewMenuItem);
		SwingUtilities.updateComponentTreeUI(popupMenu);
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {

					DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
					if (clickedNode == null)
						return;

					ResourcedEntityDataNode entityDataNode = (ResourcedEntityDataNode) clickedNode.getUserObject();

					if (entityDataNode instanceof DatabaseDataObject) {
						lastSelectedNode = entityDataNode;
						requestFocus();
					}
					
					addPopup(e, popupMenu);
					
					return;
				}

				super.mousePressed(e);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) {
					return;
				}
				DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
				if (clickedNode == null) {
					return;
				}
				ResourcedEntityDataNode entityDataNode = (ResourcedEntityDataNode) clickedNode.getUserObject();
				try {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					handleDoubleClickTreeNodeEvent(entityDataNode, clickedNode);
					lastSelectedNode = entityDataNode;
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void handleDoubleClickTreeNodeEvent(ResourcedEntityDataNode entityDataNode, 
												DefaultMutableTreeNode clickedTreeNode) throws SQLException 
	{
		TreeNodeInteractEvent treeNodeInteractEvent = null;
		
		if (entityDataNode instanceof Server)
			treeNodeInteractEvent = 
				new ServerNodeInteractEvent(this, clickedTreeNode);		
		
		else if (entityDataNode instanceof Database)	
			treeNodeInteractEvent =
				new DatabaseNodeInteractEvent(this, clickedTreeNode);
		
		else if (entityDataNode instanceof Schema) 
			treeNodeInteractEvent = 
				new SchemaNodeInteractEvent(this, clickedTreeNode);
		
		else if (entityDataNode instanceof Table)
			treeNodeInteractEvent 
			= new TableNodeInteractEvent(this, clickedTreeNode);
		
		else if (entityDataNode instanceof SchemaGroup) 
			treeNodeInteractEvent = 
				new SchemaGroupNodeInteractEvent(ServerTreeViewer.this, clickedTreeNode);
		
		else if (entityDataNode instanceof SequencesGroup) 
			treeNodeInteractEvent = 
				new SequenceGroupNodeInteractEvent(ServerTreeViewer.this, clickedTreeNode);
		
		else if (entityDataNode instanceof IndexesGroup) 
			treeNodeInteractEvent = 
				new IndexGroupNodeInteractEvent(ServerTreeViewer.this, clickedTreeNode);
		
		else if (entityDataNode instanceof ConstraintsGroup) 
			treeNodeInteractEvent = 
				new ConstraintsGroupNodeInteractEvent(ServerTreeViewer.this, clickedTreeNode);
		
		else if (entityDataNode instanceof Column)
			treeNodeInteractEvent =
				new ColumnNodeInteractEvent(ServerTreeViewer.this, clickedTreeNode);
		
		/* objeto não foi mapeado / serve como um dummyObject */
		if (treeNodeInteractEvent == null)
			return;

		if (entityDataNode instanceof DatabaseDataObject)
			lastSelectedDatabaseDataObject.set(((DatabaseDataObject)entityDataNode));
		
		treeNodeInteractEvent.setTreeNodeInteractionType(TreeNodeInteractionType.DOUBLE_CLICK_EVENT);
		eventManager.dispatchEvent(treeNodeInteractEvent);
	}

	public Database getInheritedServerDatabase(DatabaseDataObject databaseDataObject) {
		if (databaseDataObject instanceof Database)
			return (Database) databaseDataObject;
		DatabaseDataObject parentDatabaseDataObject = databaseDataObject.getParent();
		if (parentDatabaseDataObject != null)
			return getInheritedServerDatabase(parentDatabaseDataObject);
		else
			return null;
	}

	private static void addPopup(MouseEvent e, final JPopupMenu popup) {
		popup.show(e.getComponent(), e.getX(), e.getY());
		SwingUtilities.updateComponentTreeUI(popup);
	}

	public DatabaseDataObject getLastSelectedDatabaseDataObject() {
		return lastSelectedDatabaseDataObject.get();
	}
	
	public void reloadAndRestoreExpandedState(DefaultMutableTreeNode clickedTreeNode) {
		expandPath(new TreePath(clickedTreeNode.getPath()));
		String originalState = expansionUtil.getExpansionState();
		getDefaultModel().reload();
		expansionUtil.setExpansionState(originalState);
	}
	
	public void addServerToTree(Server server) {
		DefaultMutableTreeNode serverNode = createTreeNodeByEntityData(server);
		rootTreeNode.insert(serverNode, 0);
		getDefaultModel().reload();
	}
	
	public EntityDataNode getLastSelected() {
		return lastSelectedNode;
	}

}
