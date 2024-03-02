package com.daviddev16.component;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.daviddev16.core.StyleConfigurator;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.style.StyleManager;

public class StyleConfiguratorComboBox extends JComboBox<StyleConfigurator> implements ItemListener, 
ListCellRenderer<StyleConfigurator> {

	private static final long serialVersionUID = 3185162423391093540L;

	private StyleManager styleManager;

	public StyleConfiguratorComboBox() {
		super();
		styleManager = ServicesFacade.getServices().getStyleManager();
		{
			setModel(new DefaultComboBoxModel<StyleConfigurator>());
			addItemListener(this);
			setRenderer(this);
		}	
	}

	@Override
	public void itemStateChanged(ItemEvent e) {	
		if (getSelectedIndex() < 0) {
			return;
		}
		StyleConfigurator selectedConfigurator = (StyleConfigurator) getSelectedItem();
		styleManager.configureStyle(selectedConfigurator);
	}

	public void loadAllStyles(Collection<StyleConfigurator> styleConfigurators) {
		DefaultComboBoxModel<StyleConfigurator> defBoxModel = (DefaultComboBoxModel<StyleConfigurator>) getModel();
		defBoxModel.removeAllElements();
		defBoxModel.addAll(styleConfigurators);
		revalidate();
	}

	public Component getListCellRendererComponent(JList<? extends StyleConfigurator> list, StyleConfigurator value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			return new JLabel(value.getStyleDisplayName());
		}
		return new JLabel();
	}

}
