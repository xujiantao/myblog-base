package com.jianao.service.impl;

import java.io.IOException;

import javax.annotation.Resource;

import me.jiantao.common.PageResult;
import me.jiantao.common.Result;
import me.jiantao.po.Article;
import me.jiantao.service.IArticleService;
import me.jiantao.util.BeanUtil;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration({"/spring-base.xml"}) //加载配置文件，可以是多个
@Transactional //添加事务
//事务配置，指定事务管理器，指定自动回滚
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)  
public class ArticleServiceImpl {
	//测试数据id
	private int id=3;
	
	@Resource
	private IArticleService IArticleService;
	
	@Test
	@Ignore
	public void saveArticle() {
		Article article = new Article();
		article.setTitle("我是文章标题");
		article.setContent("我是文章内容");
		int id = IArticleService.saveArticle(article);
		System.out.println(resultInfo);
		Assert.assertTrue(resultInfo.getStatus() == Result.STATUS_SUCCESS);
	}
	@Test
	public void getArticleById() {
		Article article=IArticleService.getArticleById(id);
		
		
//		String serverUrl = "http://localhost:8983/solr/article";
//	    SolrServer solrServer = new HttpSolrServer(serverUrl);  
//	    SolrInputDocument doc = new SolrInputDocument();
//	    doc.setField("id", article.getId());
//	    doc.setField("title", article.getTitle());
//	    doc.setField("content", article.getContent());
//	    doc.setField("createDate", article.getCreateDate());
//	    doc.setField("tags", article.getTags());
//	    doc.setField("visitCount", article.getVisitCount());
//	    try {
//			solrServer.add(doc);
//			solrServer.commit();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	    
	}
	
	@Test
	@Ignore
	public void getArticleByPage() {
		PageResult<Article> pr = new PageResult<Article>();
		IArticleService.getArticleByPage(null, pr);
		Assert.assertTrue(pr.getRowsCount()>0);
	}
	@Test
	@Ignore
	public void updateArticle() {
		Article article = IArticleService.getArticleById(id);
		article.setTitle("我是文章标题");
		article.setContent("我是文章内容");
		Result resultInfo=IArticleService.updateArticle(article);
		Assert.assertTrue(resultInfo.getStatus()==Result.STATUS_SUCCESS);
	}
	@Test
	@Ignore
	public void deleteArticle() {
		Result resultInfo=IArticleService.deleteArticle(id);
		Assert.assertTrue(resultInfo.getStatus()==Result.STATUS_SUCCESS);
	}
}
