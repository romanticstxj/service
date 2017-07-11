package com.madhouse.platform.premiummad.util;

import java.util.Collection;

public class ObjectUtils {

	/**
	 * Determine whether the given array is empty:
	 * i.e. {@code null} or of zero length.
	 * @param array the array to check
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}
	
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.size() == 0);
	}
}
