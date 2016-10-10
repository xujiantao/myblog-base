package me.jiantao.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import me.jiantao.common.AutoConvert;

public class BeanUtil {
	
	private BeanUtil(){};
	
	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 */
	public static <T>T MapToBean(Class<T> type, Map<String,Object> map){
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
			T obj = type.newInstance(); // 创建 JavaBean 对象

			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
					Object value = map.get(propertyName);
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 */
	public static <T>Map<String,Object> BeanToMap(T bean){
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> cla = bean.getClass();
		Field [] fields = cla.getDeclaredFields();
		if(CollectionUtil.isNotEmpty(fields)){
			for (Field field : fields) {
				if(cla.isAnnotationPresent(AutoConvert.class)
					||field.isAnnotationPresent(AutoConvert.class)){
					field.setAccessible(true);
					String key = field.getName();
					try {
						Object value = field.get(bean);
						map.put(key, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		Class<?> superCla = cla.getSuperclass();
		Field [] superFields = superCla.getDeclaredFields();
		if(superFields != null && superFields.length > 0){
			for (Field field : superFields) {
				if(cla.isAnnotationPresent(AutoConvert.class)
						||field.isAnnotationPresent(AutoConvert.class)){
					field.setAccessible(true);
					String key = field.getName();
					try {
						Object value = field.get(bean);
						map.put(key, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		return map;
	}
	
	public static <T>SolrInputDocument BeanToSolrInputDocument(T bean){
		SolrInputDocument doc = new SolrInputDocument();
		Map<String, Object> map = BeanToMap(bean);
		if(CollectionUtil.isNotEmpty(map)){
			map.forEach((key, value) -> {
				doc.addField(key, value);
			});
		}
		return doc;
	}
	
	public static <T>List<T> SolrDocumentListToBeanList(Class<T> cla, SolrDocumentList docList){
		List<T> list = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(docList)){
			docList.forEach(doc -> {
				Collection<String> names = doc.getFieldNames();
				Map<String, Object> map = new HashMap<>();
				names.forEach(name -> {
					map.put(name, doc.getFieldValue(name));
				});
				list.add(MapToBean(cla, map));
			});
		}
		return list;
	}
	
}
