package me.jiantao.service;

import me.jiantao.common.PageResult;
import me.jiantao.common.Result;
import me.jiantao.po.Article;

public interface IArticleService {
	
	public int saveArticle(Article article);

	public void updateArticle(Article article);
	/**
	 * 置顶和取消置顶
	 * @param id 
	 * @param type  0：取消置顶   1:置顶
	 * @return
	 */
	public void setTop(int id, int type);
	
	/**
	 * 推荐和取消推荐
	 * @param id
	 * @param type 0：取消推荐   1:推荐
	 * @return
	 */
	public void recommend(int id, int type);
	
	public void deleteArticle(int id);

	public Article getArticleById(int id);
	
	public Article getArticleByIdAndAddVisitCount(int id);

	public PageResult<Article> getArticleByPage(Article article, PageResult<Article> pr);
	
	public void addAllArticleIndex();
	
	public void deleteAllArticleIndex();
	
}
