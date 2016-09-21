package me.jiantao.search;

import java.io.IOException;
import java.util.List;

import me.jiantao.common.PageResult;
import me.jiantao.po.Article;

import org.apache.solr.client.solrj.SolrServerException;

public interface IArticleSearchService {
	
	public void saveArticle(Article article);

	public void updateArticle(Article article);

	public void deleteArticle(int id);
	
	public PageResult<Article> getArticleByPage(String keyword, PageResult<Article> pr) throws SolrServerException, IOException;

	public void addAllArticleIndex(List<Article> list);
	
	public void deleteAllArticleIndex();
}
