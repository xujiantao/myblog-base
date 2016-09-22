package me.jiantao.search;

import java.util.List;
import me.jiantao.common.PageResult;
import me.jiantao.po.Article;

public interface IArticleSearchService {
	
	public void saveArticle(Article article);

	public void updateArticle(Article article);

	public void deleteArticle(int id);
	
	public PageResult<Article> getArticleByPage(String keyword, PageResult<Article> pr);

	public void addAllArticleIndex(List<Article> list);
	
	public void deleteAllArticleIndex();
}
