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

public class Z {
	public static abstract class ZPredicate1<A> {
		public boolean condition(A arg0) {
			return true;
		}

		public abstract boolean predicate(A arg0);
	}

	public static abstract class ZPredicate2<A, B> {
		public boolean condition(A arg0, B arg1) {
			return true;
		}

		public abstract boolean predicate(A arg0, B arg1);
	}

	public static abstract class ZSelect1<A, B> {
		public boolean condition(A arg0) {
			return true;
		}

		public abstract ZSet<B> select(A arg0);
	}

	public static <T, U> boolean exists(Iterable<T> arg0Set,
			Iterable<U> arg1Set, ZPredicate2<T, U> pred) {
		for (T arg0 : arg0Set)
			for (U arg1 : arg1Set)
				if (pred.condition(arg0, arg1))
					if (pred.predicate(arg0, arg1))
						return true;
		return false;
	}

	public static <T> boolean exists(Iterable<T> arg0Set, ZPredicate1<T> pred) {
		for (T arg0 : arg0Set)
			if (pred.condition(arg0))
				if (pred.predicate(arg0))
					return true;
		return false;
	}

	public static <T, U> boolean forAll(Iterable<T> arg0Set,
			Iterable<U> arg1Set, ZPredicate2<T, U> pred) {
		for (T arg0 : arg0Set)
			for (U arg1 : arg1Set)
				if (pred.condition(arg0, arg1))
					if (!pred.predicate(arg0, arg1))
						return false;
		return true;
	}

	public static <T> boolean forAll(Iterable<T> arg0Set, ZPredicate1<T> pred) {
		for (T arg0 : arg0Set)
			if (pred.condition(arg0))
				if (!pred.predicate(arg0))
					return false;
		return true;
	}

	public static <K, V> ZOrderedPair<K, V> OP(K k, V v) {
		return new ZOrderedPair<K, V>(k, v);
	}

	public static <K, V> ZRelation<K, V> Rel(ZOrderedPair<K, V>... ops) {
		return new ZRelation<K, V>(ops);
	}

	public static <T> ZSet<T> Set(T... t) {
		ZSet<T> r = new ZSet<T>();
		for (T x : t)
			r.add(x);
		return r;
	}

	public static <T, U> ZSet<T> Set(ZSet<U> set, ZSelect1<U, T> sel) {
		ZSet<T> r = new ZSet<T>();
		for (U u : set)
			if (sel.condition(u))
				r.add(sel.select(u));
		return r;
	}

}
