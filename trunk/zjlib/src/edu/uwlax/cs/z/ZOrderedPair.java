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

public class ZOrderedPair<K, V> implements Comparable {
	private K k;

	private V v;

	ZOrderedPair(K k, V v) {
		super();
		this.k = k;
		this.v = v;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ZOrderedPair))
			return false;
		ZOrderedPair o = (ZOrderedPair) obj;
		return k.equals(o.k) && v.equals(o.v);
	}

	public K getK() {
		return k;
	}

	public V getV() {
		return v;
	}

	public int hashCode() {
		return k.hashCode() + v.hashCode();
	}

	public String toString() {
		return "(" + k.toString() + ", " + v.toString() + ")";
	}

	public int compareTo(Object o) {
		int r = 0;
		if (o instanceof ZOrderedPair) {
			ZOrderedPair p = (ZOrderedPair) o;
			if (k instanceof Comparable)
				r = ((Comparable) k).compareTo(p.k);
			if ((r == 0) && v instanceof Comparable)
				r = ((Comparable) v).compareTo(p.v);
		}
		return r;
	}
}
