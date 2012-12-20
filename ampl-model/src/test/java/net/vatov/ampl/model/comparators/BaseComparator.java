/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

import java.util.List;
import java.util.Map;

public abstract class BaseComparator<T> {

	/**
	 * Като Comparator интерфейса, но не ни интересува кое е по-голямо/по-малко,
	 * а само дали са различни
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public abstract ComparisonResult compare(T o1, T o2);

	protected <L> ComparisonResult compareLists(List<L> l1, List<L> l2,
			BaseComparator<L> c) {
		if (l1 == l2) {
			return ComparisonResult.EQUALS;
		} else if (null != l1 && null != l2) {
			if (l1.size() != l2.size()) {
				return ComparisonResult.DIFFER;
			} else {
				for (int i = 0; i < l1.size(); ++i) {
					if (ComparisonResult.DIFFER.equals(c.compare(l1.get(i),
							l2.get(i)))) {
						return ComparisonResult.DIFFER;
					}
				}
				return ComparisonResult.EQUALS;
			}
		}
		return ComparisonResult.DIFFER;
	}

	@SuppressWarnings("unchecked")
	protected <K, V> ComparisonResult compareMaps(
			Map<K, V> map1,
			Map<K, V> map2,
			@SuppressWarnings("rawtypes") Map<K, BaseComparator> c) {
		
		if (map1 == map2) {
			return ComparisonResult.EQUALS;
		} else if (null != map1 && null != map2) {
			if (map1.size() != map2.size()) {
				return ComparisonResult.DIFFER;
			} else {
				for (Map.Entry<K, V> e : map1.entrySet()) {
					boolean missingAttr = !map2.containsKey(e.getKey());
					ComparisonResult attEquality = c.get(e.getKey()).compare(
							e.getValue(), map2.get(e.getKey()));
					if (missingAttr || ComparisonResult.DIFFER.equals(attEquality)) {
						return ComparisonResult.DIFFER;
					}
				}
				return ComparisonResult.EQUALS;
			}
		}
		return ComparisonResult.DIFFER;
	}
}
