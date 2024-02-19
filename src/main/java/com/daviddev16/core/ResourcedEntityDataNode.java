package com.daviddev16.core;

import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.service.FileResourceLocator;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Utilizado para definição a renderização de objetos em uma arvore através de 
 * resources da aplicação.
 * */
public interface ResourcedEntityDataNode extends EntityDataNode {

	/**
	 * 
	 * Definição do nome que será apresetado na arvore de visualização.
	 * 
	 * */
	@JsonIgnore
	String getNodeName();
	
	/**
	 * 
	 * Define qual será o resource utilizado na renderização do icone do
	 * objeto primário.
	 * 
	 * */
	@JsonIgnore
	default String getResourceIdentifier() {
		Resourced resourcedAnnotation = getClass().getAnnotation(Resourced.class);
		return (resourcedAnnotation != null) ? 
				resourcedAnnotation.resourceIdentifier() : FileResourceLocator.UNKNOWN;
	}
	
	/**
	 * 
	 * Define o parente deste objeto, na arvore de visualização, caso necessário.
	 * 
	 * */
	@JsonIgnore
	default ResourcedEntityDataNode getParent() {
		return null;
	}
	
}
