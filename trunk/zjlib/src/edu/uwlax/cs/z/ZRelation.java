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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;

public class ZRelation<K, V> extends ZSet<ZOrderedPair<K, V>> {

	public static <T> ZRelation<T, T> id(ZSet<T> set) {
		ZRelation<T, T> r = new ZRelation<T, T>();
		for (T t : set)
			r.add(new ZOrderedPair<T, T>(t, t));
		return r;
	}

	ZRelation() {
		super();
	}

	public ZRelation(Collection<ZOrderedPair<K, V>> c) {
		super(c);
	}

	public ZRelation(SortedSet<ZOrderedPair<K, V>> c) {
		super(c);
	}

	ZRelation(ZOrderedPair<K, V>... c) {
		super(c);
	}

	public ZRelation(ZSet<ZOrderedPair<K, V>> c) {
		super(c);
	}

	/**
	 * Returns the relational composition of this relation with the specified
	 * relation.
	 */
	public <X> ZRelation<K, X> composition(ZRelation<V, X> rel) {
		ZRelation<K, X> r = new ZRelation<K, X>();
		for (ZOrderedPair<K, V> p1 : this)
			for (ZOrderedPair<V, X> p2 : rel)
				if (p1.getV().equals(p2.getK()))
					r.add(new ZOrderedPair<K, X>(p1.getK(), p2.getV()));
		return r;
	}

	/**
	 * Returns the backward relational composition of this relation with the
	 * specified relation.
	 */
	public <X> ZRelation<V, X> compositionBackward(ZRelation<X, K> rel) {
		ZRelation<V, X> r = new ZRelation<V, X>();
		for (ZOrderedPair<K, V> p1 : this)
			for (ZOrderedPair<X, K> p2 : rel)
				if (p1.getK().equals(p2.getV()))
					r.add(new ZOrderedPair<V, X>(p1.getV(), p2.getK()));
		return r;
	}

	/**
	 * Return the domain of this relation.
	 */
	public ZSet<K> domain() {
		ZSet<K> r = new ZSet<K>();
		for (ZOrderedPair<K, V> p : set())
			r.add(p.getK());
		return r;
	}

	public Map<K, V> domainMap() {
		Map<K, V> map = new HashMap<K, V>();
		for (ZOrderedPair<K, V> p : set())
			map.put(p.getK(), p.getV());
		return map;
	}

	public final ZRelation<K, V> domainRestriction(K dom) {
		return domainRestriction(new ZSet<K>(dom));
	}

	/**
	 * Returns the domain restriction of this relation with the specified set.
	 */
	public ZRelation<K, V> domainRestriction(ZSet<K> dom) {
		ZRelation<K, V> r = new ZRelation<K, V>();
		for (ZOrderedPair<K, V> p : this)
			if (dom.isMember(p.getK()))
				r.add(p);
		return r;
	}

	public final ZRelation<K, V> domainSubtraction(K dom) {
		return domainSubtraction(new ZSet<K>(dom));
	}

	/**
	 * Returns the domain subtraction of this relation with the specified set.
	 */
	public ZRelation<K, V> domainSubtraction(ZSet<K> dom) {
		ZRelation<K, V> r = new ZRelation<K, V>();
		for (ZOrderedPair<K, V> p : this)
			if (!dom.isMember(p.getK()))
				r.add(p);
		return r;
	}

	/**
	 * Returns the relational image with the specified set.
	 */
	public ZSet<V> image(ZSet<K> dom) {
		ZSet<V> r = new ZSet<V>();
		for (ZOrderedPair<K, V> p : this)
			if (dom.isMember(p.getK()))
				r.add(p.getV());
		return r;
	}

	/**
	 * Returns the inverse of this relation.
	 */
	public ZRelation<V, K> inverse() {
		ZRelation<V, K> r = new ZRelation<V, K>();
		for (ZOrderedPair<K, V> p : this)
			r.add(new ZOrderedPair<V, K>(p.getV(), p.getK()));
		return r;
	}

	public boolean isMember(K k, V v) {
		return isMember(new ZOrderedPair<K, V>(k, v));
	}

	public ZRelation<K, V> override(ZRelation<K, V> dom) {
		return new ZRelation<K, V>(domainSubtraction(dom.domain()).union(dom));
	}

	/**
	 * Return the range of this relation.
	 */
	public ZSet<V> range() {
		ZSet<V> r = new ZSet<V>();
		for (ZOrderedPair<K, V> p : set())
			r.add(p.getV());
		return r;
	}

	public Map<V, K> rangeMap() {
		Map<V, K> map = new HashMap<V, K>();
		for (ZOrderedPair<K, V> p : set())
			map.put(p.getV(), p.getK());
		return map;
	}

	public final ZRelation<K, V> rangeRestriction(V ran) {
		return rangeRestriction(new ZSet<V>(ran));
	}

	/**
	 * Returns the range restriction of this relation with the specified set.
	 */
	public ZRelation<K, V> rangeRestriction(ZSet<V> ran) {
		ZRelation<K, V> r = new ZRelation<K, V>();
		for (ZOrderedPair<K, V> p : this)
			if (ran.isMember(p.getV()))
				r.add(p);
		return r;
	}

	public final ZRelation<K, V> rangeSubtraction(V ran) {
		return rangeSubtraction(new ZSet<V>(ran));
	}

	/**
	 * Returns the range subtraction of this relation with the specified set.
	 */
	public ZRelation<K, V> rangeSubtraction(ZSet<V> ran) {
		ZRelation<K, V> r = new ZRelation<K, V>();
		for (ZOrderedPair<K, V> p : this)
			if (!ran.isMember(p.getV()))
				r.add(p);
		return r;
	}

	public void removeAllDomains(ZSet<K> dom) {
		for (Iterator<ZOrderedPair<K, V>> i = set().iterator(); i.hasNext();)
			if (dom.isMember(i.next().getK()))
				i.remove();
	}

	public void removeAllRanges(ZSet<V> ran) {
		for (Iterator<ZOrderedPair<K, V>> i = set().iterator(); i.hasNext();)
			if (ran.isMember(i.next().getV()))
				i.remove();
	}

}
