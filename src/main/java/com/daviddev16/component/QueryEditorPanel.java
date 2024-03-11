package com.daviddev16.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.daviddev16.component.editor.TextEditor;
import com.daviddev16.core.CustomStyleComponentConfiguration;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.event.EventPriority;
import com.daviddev16.event.interaction.QueryRequestEvent;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.service.EventManager;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.style.CustomStylePropertiesHolder;
import com.daviddev16.util.TextUtil;
import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;

public class QueryEditorPanel extends JPanel implements EventListener, 
														CustomStyleComponentConfiguration {

	private final EventManager eventManager;
	
	private Map<String, String> upperKeywordMap = new HashMap<String, String>();
	{
		upperKeywordMap.put("select", "SELECT");
		upperKeywordMap.put("from", "FROM");
		upperKeywordMap.put("left join", "LEFT JOIN");
		upperKeywordMap.put("inner join", "INNER JOIN");
		upperKeywordMap.put("right join", "RIGHT JOIN");
		upperKeywordMap.put("where", "WHERE");
		upperKeywordMap.put("order by", "ORDER BY");
		upperKeywordMap.put("having", "HAVING");
		upperKeywordMap.put("distinct", "DISTINCT");
		upperKeywordMap.put("limit", "LIMIT");
		upperKeywordMap.put("offset", "OFFSET");
		upperKeywordMap.put("as", "AS");
		upperKeywordMap.put("with", "WITH");
	}
	
	private static final long serialVersionUID = 1L;
	private BorderlessButton btnExecuteSql;
	private BorderlessToggleButton tgBtnEnableUpperKeyword;
	
	private TextEditor textEditor;
	private JScrollPane txEdtScrollPane;
	private JPanel pnToolBar;
	
	public QueryEditorPanel() {
		
		eventManager = ServicesFacade.getServices().getEventManager();
		eventManager.registerListener(this);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		textEditor = new TextEditor();
		txEdtScrollPane = new RTextScrollPane(textEditor);
		textEditor.setCodeFoldingEnabled(true);
		textEditor.setUseSelectedTextColor(false);

		
		pnToolBar = new JPanel();
		txEdtScrollPane.setColumnHeaderView(pnToolBar);

		pnToolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnExecuteSql = new BorderlessButton();
		btnExecuteSql.setPreferredSize(SwingUtil.DIMENSION_SQUARE_24);
		btnExecuteSql.setToolTipText("Executar SQL selecionado");
		btnExecuteSql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispatchQueryRequestEvent();
			}
		});

		btnExecuteSql.setIcon(ServicesFacade.getServices()
				.getFileResourceLocator().cachedImageIcon("Execute16px"));
		pnToolBar.add(btnExecuteSql);
		
		tgBtnEnableUpperKeyword = new BorderlessToggleButton();
		tgBtnEnableUpperKeyword.setPreferredSize(SwingUtil.DIMENSION_SQUARE_24);
		tgBtnEnableUpperKeyword.setSelected(false);
		tgBtnEnableUpperKeyword.setToolTipText("Identação com palavras-chave em caixa alta");

		tgBtnEnableUpperKeyword.setIcon(ServicesFacade.getServices()
				.getFileResourceLocator().cachedImageIcon("Up16px"));
		pnToolBar.add(tgBtnEnableUpperKeyword);
		
		textEditor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				String queryText = TextUtil.isNullOrEmpty(textEditor.getSelectedText()) 
						? textEditor.getText() : textEditor.getSelectedText();

				if (tgBtnEnableUpperKeyword.isSelected())
					queryText = upperMaskText(queryText);
				
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I) {
					String formattedQuery = SqlFormatter.of(Dialect.PostgreSql).format(queryText);
					textEditor.setText(formattedQuery);
				}
				
			}
		});
		textEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		textEditor.setText("");
		
		add(txEdtScrollPane);
		
		textEditor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) 
				{
					dispatchQueryRequestEvent();
				}
			}
		});
		textEditor.setTabSize(2);
	}
	
	public String upperMaskText(String text) {
		String maskedText = text;
		for(Entry<String, String> toUpperKeyword : upperKeywordMap.entrySet()) {
			maskedText = maskedText.replaceAll(toUpperKeyword.getKey(), toUpperKeyword.getValue());
		}
		return maskedText;
	}
	
	private void dispatchQueryRequestEvent() {
		String effectiveQuery = TextUtil.stringWithDefault(textEditor
				.getSelectedText(), textEditor.getText());
		eventManager.dispatchEvent(
				new QueryRequestEvent(QueryEditorPanel.this, effectiveQuery));
	}

	@Override
	public void configureComponentsStyleConfiguration(CustomStylePropertiesHolder stylePropertiesHolder) 
	{
		final Color customInnerBackgroundColor = 
				stylePropertiesHolder.getInnerBackgroundColor();

		pnToolBar.setBackground(customInnerBackgroundColor);

		txEdtScrollPane        .setBorder(new EmptyBorder(0, 0, 0, 0));
		btnExecuteSql          .setBorder(null);
		tgBtnEnableUpperKeyword.setBorder(null);
		pnToolBar              .setBorder(null);
	}
	
	@EventHandler(priority = EventPriority.MEDIUM)
	public void onChangedStyleStateEvent(ChangedStyleStateEvent styleStateEvent) 
	{
		configureComponentsStyleConfiguration(styleStateEvent.getCustomStylePropertiesHolder());	
	}
	
}
