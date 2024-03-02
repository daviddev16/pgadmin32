package com.daviddev16.core.component;

import java.awt.Container;

import javax.swing.JPanel;

import com.daviddev16.core.CustomStyleComponentConfiguration;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.Documented;
import com.daviddev16.service.EventManager;
import com.daviddev16.service.ServicesFacade;

/**
 * Um componente onde seus eventos são independentes dos eventos do frame que cria 
 * o objeto. É um tipo de <code>JPanel</code> que consume eventos e integra ao frame 
 * principal da aplicação. Um <code>FrameFragment</code> permite o isolamento de 
 * componentes de forma organizada e automatizada.
 * 
 * @author David Duarte
 */
@Documented
public abstract class FrameFragment<C extends Container> extends JPanel implements EventListener, 
													 		  				       CustomStyleComponentConfiguration {

	private static final long serialVersionUID = 1L;

	private EventManager eventManager;
	{
		/* permite que o WindowBuilder Edito não lance uma exceção indevidamente */
		if (ServicesFacade.getServices() != null)
			eventManager = ServicesFacade.getServices().getEventManager();
	}

	private C directParentContainer;
	
	/**
	 * Cria um <code>FrameFragment</code> vazio, que possui seu objeto registrado
	 * no <code>EventManager</code>, através do facade de Serviços.
	 * 
	 * @see EventManager
	 */
	public FrameFragment(C directParentContainer) { 
		super();

		this.directParentContainer = directParentContainer;
		
		if (eventManager != null)
			eventManager.registerListener(this);
	}
	
	/**
	 * Retorna a instância imutável de <code>EventManager</code>.
	 */
	public EventManager getEventManager() {
		return eventManager;
	}

	/**
	 * O <code>Container</code> parente ao fragmento.
	 */
	public C getDirectParentContainer() {
		return directParentContainer;
	}
	
}
