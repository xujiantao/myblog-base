package me.jiantao.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import me.jiantao.common.PageResult;
import me.jiantao.dao.ArticleDao;
import me.jiantao.dao.BaseDao;
import me.jiantao.po.Article;
import me.jiantao.search.IArticleSearchService;

import org.springframework.stereotype.Component;

@Component
public class ArticleDaoImpl extends BaseDao<Article> implements ArticleDao {
	
	@Resource
	private IArticleSearchService iArticleSearchService;

	@Override
	public int saveArticle(Article article) {
		int id = (Integer) save(article);
		iArticleSearchService.saveArticle(article);
		return id;
	}

	@Override
	public void updateArticle(Article article) {
		update(article);
		iArticleSearchService.updateArticle(article);
	}

	@Override
	public void deleteArticle(Article article) {
		delete(article);
		iArticleSearchService.deleteArticle(article.getId());
	}

	@Override
	public Article getArticleById(int id) {
		Article article=get(Article.class, id);
		if(article!=null){
			article.handle();
		}
		return article;
	}

	@Override
	public PageResult<Article> getArticleByPage(String hql,
			Map<String, Object> params, PageResult<Article> pr) {
		return list(hql, params, pr);
	}
	
	public int getMaxSortCount(){
		String hql = "select max(sortCount) from Article";
		return (int)uniqueResult(hql, null);
	}

	@Override
	public Article getUniqueArticle(String hql, Map<String, Object> params) {
		return uniqueBean(hql, params);
	}

}
