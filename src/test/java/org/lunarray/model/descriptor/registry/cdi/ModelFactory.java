/* 
 * Model Tools.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.model.descriptor.registry.cdi;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;

import org.lunarray.model.descriptor.builder.annotation.simple.SimpleBuilder;
import org.lunarray.model.descriptor.dictionary.Dictionary;
import org.lunarray.model.descriptor.dictionary.composite.registry.CompositeRegistryDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.objectfactory.ObjectFactory;
import org.lunarray.model.descriptor.objectfactory.simple.SimpleObjectFactory;
import org.lunarray.model.descriptor.registry.cdi.model.Entity01;
import org.lunarray.model.descriptor.registry.cdi.model.Entity02;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;

/**
 * A factory for the model tests.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class ModelFactory {

	/** Create a named dictionary. */
	@Produces
	@Named("registry-dictionary")
	public Dictionary createDictionary() {
		return new CompositeRegistryDictionary<String>();
	}

	/** Create the model. */
	@Produces
	public Model<Object> createModel(final SimpleClazzResource<Object> resource, final CdiRegistry registry) throws Exception {
		final SimpleBuilder<Object> builder = SimpleBuilder.createBuilder();
		return builder.extensions(registry).resources(resource).build();
	}

	/** Create an object factory.. */
	@Produces
	public ObjectFactory createObjectFactory() {
		return new SimpleObjectFactory();
	}

	/** Create the registry. */
	@Produces
	public CdiRegistry createRegistry(final BeanManager beanManager) {
		return CdiRegistry.createBuilder().beanManager(beanManager).create();
	}

	/** Create the model resource. */
	@SuppressWarnings("unchecked")
	@Produces
	public SimpleClazzResource<Object> createResourceModel() {
		return new SimpleClazzResource<Object>(Entity01.class, Entity02.class);
	}
}
