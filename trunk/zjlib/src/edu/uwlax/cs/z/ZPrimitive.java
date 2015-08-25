/* This file is part of ZJLib, a library written in java to support 
 the implementation of specifications written in Z-Notation in Java 5.
 
 Copyright (C) 2007  Moritz Eysholdt <Moritz@Eysholdt.de>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package edu.uwlax.cs.z;

import java.util.HashMap;

public class ZPrimitive implements Comparable {
	private static class ClsItem {
		private Class cls;

		private String name;

		public ClsItem(Class cls, String name) {
			super();
			this.cls = cls;
			this.name = name;
		}

		public boolean equals(Object obj) {
			if (obj instanceof ClsItem) {
				ClsItem i = (ClsItem) obj;
				return cls.equals(i.cls) && name.equals(i.name);
			}
			return super.equals(obj);
		}

		public int hashCode() {
			return name.hashCode() + cls.hashCode();
		}

	}

	private static HashMap<ClsItem, ZPrimitive> instance = new HashMap<ClsItem, ZPrimitive>();

	private static int itemCounter = 1;

	public static <T extends ZPrimitive> T getAnonymousInstance(Class<T> cls,
			String name) {
		T p = null;
		try {
			p = (T) cls.newInstance();
			p.name = name;
			p.updateName(name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return p;
	}

	public static <T extends ZPrimitive> T getInstance(Class<T> cls, String name) {
		T p = (T) instance.get(new ClsItem(cls, name));
		if (p == null) {
			try {
				p = (T) cls.newInstance();
				setInstance(p, name);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return p;
	}

	public static void setInstance(ZPrimitive p, String newName) {
		if (p.getName() != null)
			instance.remove(new ClsItem(p.getClass(), p.getName()));
		ClsItem key = new ClsItem(p.getClass(), newName);
		if (instance.get(key) != null)
			throw new RuntimeException("An object of type \""
					+ p.getClass().getSimpleName() + "\" with the name \""
					+ newName + "\" alredy exists.");
		instance.put(key, p);
		p.name = newName;
		p.updateName(newName);
	}

	private String name;

	public ZPrimitive() {
		super();
		setInstance(this, getClass().getSimpleName() + (itemCounter++));
	}

	public ZPrimitive(String name) {
		super();
		setInstance(this, name);
	}

	public int compareTo(Object o) {
		if (o instanceof ZPrimitive)
			return name.compareToIgnoreCase(((ZPrimitive) o).name);
		return 0;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ZPrimitive))
			return false;
		return name.equals(((ZPrimitive) obj).name);
	}

	public String getName() {
		return name;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public void initByString(String var) {
		name = var;
	}

	public void setName(String name) {
		setInstance(this, name);
	}

	public String toString() {
		return name;
	}

	protected void updateName(String name) {
		this.name = name;
	}

}
