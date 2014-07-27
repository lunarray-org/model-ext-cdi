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

import java.util.Set;

import javax.annotation.Resource;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.registry.Registry;
import org.lunarray.model.descriptor.registry.exceptions.BeanNotFoundException;
import org.lunarray.model.descriptor.registry.exceptions.RegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A CDI registry implementation.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class CdiRegistry
		implements Registry<Bean<?>> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CdiRegistry.class);

	/**
	 * Creates the builder.
	 * 
	 * @return The builder.
	 */
	public static Builder createBuilder() {
		return new Builder();
	}

	/** The bean manager. */
	@Resource
	private transient BeanManager beanManager;

	/** Default constructor. */
	protected CdiRegistry() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public Object lookup(final Bean<?> name) throws RegistryException {
		CdiRegistry.LOGGER.debug("Finding bean with name '{}'.", name);
		if (CheckUtil.isNull(name)) {
			throw new BeanNotFoundException("Bean may not be null.");
		}
		final CreationalContext<?> context = this.beanManager.createCreationalContext(name);
		return this.beanManager.getReference(name, Object.class, context);
	}

	/** {@inheritDoc} */
	@Override
	public <B> B lookup(final Class<B> clazz) throws RegistryException {
		CdiRegistry.LOGGER.debug("Finding bean with type '{}'.", clazz);
		if (CheckUtil.isNull(clazz)) {
			throw new BeanNotFoundException("Clazz may not be null.");
		}
		final Set<Bean<?>> beans = this.beanManager.getBeans(clazz);
		if (beans.isEmpty()) {
			throw new BeanNotFoundException(String.format("Could not locate a bean of type '%s'.", clazz.getName()));
		}
		final Bean<?> bean = this.beanManager.resolve(beans);
		final CreationalContext<?> context = this.beanManager.createCreationalContext(bean);
		final Object result = this.beanManager.getReference(bean, clazz, context);
		return clazz.cast(result);
	}

	/** {@inheritDoc} */
	@Override
	public <B> B lookup(final Class<B> clazz, final Bean<?> name) throws RegistryException {
		CdiRegistry.LOGGER.debug("Finding bean with type '{}' and name '{}'.", clazz, name);
		if (CheckUtil.isNull(name) && CheckUtil.isNull(name)) {
			throw new BeanNotFoundException("Clazz or name may not both be null.");
		}
		B result;
		if (CheckUtil.isNull(name)) {
			result = this.lookup(clazz);
		} else {
			final Object instance = this.lookup(name);
			if (clazz.isInstance(instance)) {
				result = clazz.cast(instance);
			} else {
				throw new RegistryException(String.format("Could not find bean with name '%s', found type '%s', expected '%s'.", name,
						instance.getClass(), clazz));
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public <B> Set<Bean<?>> lookupAll(final Class<B> clazz) throws RegistryException {
		CdiRegistry.LOGGER.debug("Finding beans with type '{}'.", clazz);
		if (CheckUtil.isNull(clazz)) {
			throw new BeanNotFoundException("Bean type may not be null.");
		}
		return this.beanManager.getBeans(clazz);
	}

	/**
	 * Sets the bean manager.
	 * 
	 * @param beanManager
	 *            The bean manager.
	 */
	public void setBeanManager(final BeanManager beanManager) {
		this.beanManager = beanManager;
	}

	/**
	 * A registry builder.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class Builder {
		/** The bean manager. */
		private transient BeanManager beanManagerBuilder;

		/** Default constructor. */
		protected Builder() {
			// Default constructor.
		}

		/**
		 * Injects the bean manager.
		 * 
		 * @param beanManager
		 *            The bean manager.
		 * @return The builder.
		 */
		public Builder beanManager(final BeanManager beanManager) {
			this.beanManagerBuilder = beanManager;
			return this;
		}

		/**
		 * Creates the registry.
		 * 
		 * @return The registry.
		 */
		public CdiRegistry create() {
			BeanManager manager = this.beanManagerBuilder;
			if (CheckUtil.isNull(manager)) {
				try {
					final Object obj = new InitialContext().lookup("java:comp/BeanManager");
					if (obj instanceof BeanManager) {
						manager = (BeanManager) obj;
					} else {
						CdiRegistry.LOGGER.warn("Could not get BeanManager.");
					}
				} catch (final NamingException e) {
					CdiRegistry.LOGGER.warn("Could not get BeanManager through JNDI.", e);
				}
			}
			Validate.notNull(manager, "Bean manager may not be null.");
			final CdiRegistry registry = new CdiRegistry();
			registry.setBeanManager(manager);
			return registry;
		}
	}
}
