package me.jiantao.util;

import java.util.Collection;
import java.util.Map;

public class CollectionUtil {
	
	private CollectionUtil(){};
	
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}
	
	public static boolean isEmpty(Object[] objs) {
		return (objs == null || objs.length == 0);
	}
}
