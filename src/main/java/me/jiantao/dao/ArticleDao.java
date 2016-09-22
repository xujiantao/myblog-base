package me.jiantao.dao;

import java.util.Map;

import me.jiantao.common.PageResult;
import me.jiantao.po.Article;

public interface ArticleDao {
	
	public int saveArticle(Article article);
	
	public void updateArticle(Article article);
	
	public void deleteArticle(Article article);
	
	public Article getArticleById(int id);
	
	public Article getUniqueArticle(String hql, Map<String, Object> params);
	
	public PageResult<Article> getArticleByPage(String hql, Map<String, Object> params, PageResult<Article> pr);

	public int getMaxSortCount();
}
