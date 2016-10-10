package me.jiantao.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import me.jiantao.common.PageResult;
import me.jiantao.util.CollectionUtil;
/**
 * 公共DAO，提供了一些通用的操作方法
 * @param <T>
 */
public abstract class BaseDao<T>{
	
	@Resource
	private SessionFactory sessionFactory;
	
	protected Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	protected Session openSession(){
		return sessionFactory.openSession();
	}
	
	private Session getSession(){
		return getCurrentSession();
	}
	/**
	 * 保存对象，返回对象的id
	 * 
	 * @param t
	 * @return
	 */
	protected Serializable save(T t) {
		return getSession().save(t);
	}

	/**
	 * 删除对象
	 * 
	 * @param t
	 */
	protected void delete(T t) {
		getSession().delete(t);
	}

	/**
	 * 修改对象
	 * 
	 * @param t
	 */
	protected void update(T t) {
		getSession().update(t);
	}

	/**
	 * 根据主键id查询对象，子类可以调用此方法，并传入具体的类型，此方法不使用延迟加载
	 * 
	 * @param cla
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final T get(Class<T> cla, Serializable id) {
		return (T) getSession().get(cla, id);
	}

	/**
	 * 子类覆写，调用get(Class<T> cla, Serializable id)，传入cla
	 * 
	 * @param id
	 * @return
	 */
	protected T get(Serializable id) {
		return null;
	};

	/**
	 * 根据主键id查询对象，子类可以调用此方法，并传入具体的类型，此方法会使用延迟加载
	 * 
	 * @param cla
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final T load(Class<T> cla, Serializable id) {
		return (T) getSession().load(cla, id);
	}

	/**
	 * 子类覆写，调用父类的load(Class<T> cla, Serializable id)，传入cla
	 * 
	 * @param id
	 * @return
	 */
	protected T load(Serializable id) {
		return null;
	}

	/**
	 * 直接执行指定的hql语句
	 * 
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> list(String hql, Map<String, Object> params) {
		Query query = getSession().createQuery(hql);
		setParamsToQuery(query, params);
		return (List<T>) query.list();
	}
	
	private void setParamsToQuery(Query query, Map<String, Object> params){
		if (CollectionUtil.isNotEmpty(params)) {
			params.forEach((key, value) -> {
				query.setParameter(key, value);
			});
		}
	}

	/**
	 * 返回唯一结果集，一般用来执行聚合函数
	 * 
	 * @param hql
	 * @return
	 */
	protected Object uniqueResult(String hql, Map<String, Object> params) {
		Query query = getSession().createQuery(hql);
		setParamsToQuery(query, params);
		return query.uniqueResult();
	}

	/**
	 * 返回唯一实体
	 * 
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T uniqueBean(String hql, Map<String, Object> params) {
		return (T) uniqueResult(hql, params);
	}

	/**
	 * 分页执行hql
	 * 
	 * @param hql hql语句
	 * @param pr 分页对象，里面封装了分页相关信息
	 * @return
	 */
	protected PageResult<T> list(String hql, Map<String, Object> params, PageResult<T> pr) {
		Query query = getSession().createQuery(hql);
		setParamsToQuery(query, params);
		query.setFirstResult((pr.getPageNo() - 1) * pr.getPageSize());
		query.setMaxResults(pr.getPageSize());
		pr.setList(list(query));
		// 下面的是计算当前hql语句的总行数
		String newHql = hql.toLowerCase();
		int index = newHql.indexOf("from");
		String countHql = "";
		if (index == 0) {
			countHql = "select count(*) " + hql;
		} else {
			String last = newHql.substring(index + 4, newHql.length() - 1);
			countHql = "select count(*) " + last;
		}
		pr.setRowsCount((Long) uniqueResult(countHql, params));
		pr.handle();
		return pr;
	}

	protected Criteria createCriteria(Class<?> cla) {
		return getSession().createCriteria(cla);
	}

	/**
	 * 子类覆写，调用createCriteria(Class<?> cla)，传入cla
	 * 
	 * @return
	 */
	protected Criteria createCriteria() {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<T> list(Criteria criteria) {
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	protected List<T> list(Query query) {
		return query.list();
	}

	/**
	 * QBC方式分页查询
	 * 
	 * @param criteria
	 * @param pr
	 * @return
	 */
	protected PageResult<T> list(Criteria criteria, PageResult<T> pr) {
		// 先计算总行数
		criteria.setProjection(Projections.rowCount());
		long rowsCount = Long.valueOf((Integer) criteria.uniqueResult());
		pr.setRowsCount(rowsCount);
		criteria.setProjection(null);// 在计算完总行数之后把此属性设置为null就可以正常执行查询操作
		// 分页
		criteria.setFirstResult((pr.getPageNo() - 1) * pr.getPageSize());
		criteria.setMaxResults(pr.getPageSize());
		pr.setList(list(criteria));
		pr.handle();
		return pr;
	}
	
}
