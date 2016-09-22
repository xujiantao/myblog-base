package me.jiantao.service;

import me.jiantao.common.PageResult;
import me.jiantao.po.Article;

public interface IArticleService {
	
	public Article getArticleById(int id);
	public Article getArticleByIdAndAddVisitCount(int id);
	public PageResult<Article> getArticleByPage(Article article, PageResult<Article> pr);
	
	public int saveArticle(Article article);
	public void updateArticle(Article article);
	public void deleteArticle(int id);
	
	public void executeSetTop(int id);
	public void cancelSetTop(int id);
	public void executeRecommend(int id);
	public void cancelRecommend(int id);
	
	public void addAllArticleIndex();
	public void deleteAllArticleIndex();
	
}
