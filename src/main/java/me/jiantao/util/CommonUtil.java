package me.jiantao.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonUtil {
	
	private CommonUtil(){};
	
	public static boolean listIsNull(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean listIsNotNull(List<?> list) {
		return !listIsNull(list);
	}
	
	public static boolean setIsNull(Set<?> set) {
		if (set == null || set.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean SetIsNotNull(Set<?> set) {
		return !setIsNull(set);
	}
	
	public static boolean mapIsNull(Map<?, ?> map) {
		if (map == null || map.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean mapIsNotNull(Map<?, ?> map) {
		return !mapIsNull(map);
	}
	
}
