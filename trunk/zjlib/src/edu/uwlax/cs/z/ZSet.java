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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class ZSet<T> implements Iterable<T>, Cloneable {

	/**
	 * Returns the intersection of a list of sets.
	 */
	public static <T> ZSet<T> generalizedIntersection(Iterable<ZSet<T>> sets) {
		ZSet<T> s = new ZSet<T>();
		Iterator<ZSet<T>> i = sets.iterator();
		if (i.hasNext())
			s.set.addAll(i.next().set);
		while (i.hasNext())
			s.set.retainAll(i.next().set);
		return s;
	}

	/**
	 * Returns the union of a list of sets.
	 */
	public static <T> ZSet<T> generalizedUnion(Iterable<ZSet<T>> sets) {
		ZSet<T> s = new ZSet<T>();
		for (ZSet<T> i : sets)
			s.set.addAll(i.set);
		return s;
	}

	private TreeSet<T> set;

	ZSet() {
		set = new TreeSet<T>();
	}

	public ZSet(Collection<T> c) {
		set = new TreeSet<T>(c);
	}

	public ZSet(SortedSet<T> c) {
		set = new TreeSet<T>(c);
	}

	ZSet(T... c) {
		set = new TreeSet<T>();
		for (T t : c)
			set.add(t);
	}

	ZSet(ZSet<T> c) {
		set = new TreeSet<T>(c.set);
	}

	public void add(ZSet<T> o) {
		set.addAll(o.set);
	}

	public void add(T o) {
		set.add(o);
	}

	/**
	 * Returns the cardinality of this set.
	 */
	public int cardinality() {
		return set.size();
	}

	public <X extends ZSet<T>> X clone() {
		try {
			X x = (X) super.clone();
			x.set = (TreeSet) set.clone();
			return x;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the difference of this set with another set.
	 */
	public ZSet<T> difference(ZSet<T> s) {
		ZSet<T> r = new ZSet<T>(this);
		r.set.removeAll(s.set);
		return r;
	}

	/**
	 * Test if another set is equivalent to this set.
	 */
	public boolean equals(Object obj) {
		return (obj instanceof ZSet) ? set.equals(((ZSet) obj).set) : false;
	}

	/**
	 * Returns the intersection of this set with another set.
	 */
	public ZSet<T> intersection(ZSet<T> s) {
		ZSet<T> r = new ZSet<T>(this);
		r.set.retainAll(s.set);
		return r;
	}

	/**
	 * Returns true if the set contains no elements.
	 */
	public boolean isEmpty() {
		return set.isEmpty();
	}

	/**
	 * Test if another set is equivalent to this set
	 */
	public boolean isEqual(ZSet<T> s) {
		return set.equals(s.set);
	}

	/**
	 * Tests if the specified object is a member of this set.
	 */
	public boolean isMember(T o) {
		return set.contains(o);
	}

	/**
	 * Test if another set is a proper subset of this set
	 */
	public boolean isProperSubset(ZSet<T> s) {
		return set.containsAll(s.set) && !set.equals(s.set);
	}

	/**
	 * Test if another set is a subset of this set
	 */
	public boolean isSubset(ZSet<T> s) {
		return set.containsAll(s.set);
	}

	public Iterator<T> iterator() {
		return set.iterator();
	}

	/**
	 * Returns all the subsets of this set.
	 */
	public ZSet<ZSet<T>> powerSet() {
		ZSet<ZSet<T>> r = new ZSet<ZSet<T>>();
		powerSet(r, this);
		return r;
	}

	private void powerSet(ZSet<ZSet<T>> result, ZSet<T> subset) {
		result.add(subset);
		if (subset.cardinality() <= 1)
			return;
		for (T t : subset) {
			ZSet<T> s = new ZSet<T>(subset);
			s.remove(t);
			powerSet(result, subset);
		}
	}

	public void remove(Iterable<T> t) {
		for (T x : t)
			set.remove(x);
	}

	public void remove(T o) {
		set.remove(o);
	}

	public Set<T> set() {
		return set;
	}

	public ZSet<T> substract(ZSet<T> p) {
		ZSet<T> s = new ZSet<T>(set);
		s.set.removeAll(p.set);
		return s;
	}

	public ArrayList<T> toArrayList() {
		return new ArrayList<T>(set);
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(getClass().getSimpleName());
		b.append(" (" + cardinality() + " items)\n");
		for (T t : set) {
			b.append("  " + t.toString() + "\n");
		}
		return b.toString();
	}

	public Vector<T> toVector() {
		return new Vector<T>(set);
	}

	/**
	 * Returns the union of this set with another set.
	 */
	public ZSet<T> union(ZSet<T> s) {
		ZSet<T> r = new ZSet<T>(set);
		r.set.addAll(s.set);
		return r;
	}

}
