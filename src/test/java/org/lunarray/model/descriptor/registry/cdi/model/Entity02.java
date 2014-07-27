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
package org.lunarray.model.descriptor.registry.cdi.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Test entity.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class Entity02 {

	public static final Entity02 SAMPLE_01;

	private String value01;

	static {
		SAMPLE_01 = new Entity02();
		Entity02.SAMPLE_01.setValue01("TestValue0201");
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * Gets the value for the value01 field.
	 * 
	 * @return The value for the value01 field.
	 */
	public String getValue01() {
		return this.value01;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Sets a new value for the value01 field.
	 * 
	 * @param value01
	 *            The new value for the value01 field.
	 */
	public void setValue01(final String value01) {
		this.value01 = value01;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
