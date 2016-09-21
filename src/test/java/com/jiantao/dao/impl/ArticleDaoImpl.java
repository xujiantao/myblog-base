package com.jiantao.dao.impl;

import javax.annotation.Resource;
import me.jiantao.common.PageResult;
import me.jiantao.dao.ArticleDao;
import me.jiantao.po.Article;
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
public class ArticleDaoImpl {
	
	private int id = 3;
	
	@Resource
	private ArticleDao articleDao;
	
	@Test
	@Ignore
	public void saveArticle(){
		Article article = new Article();
		article.setTitle("我是文章标题");
		article.setContent("我是内容");
		articleDao.saveArticle(article);
		Assert.assertTrue(true);
	};
	
	@Test
	@Ignore
	public void updateArticle(){
		Article article = articleDao.getArticleById(id);
		Assert.assertNotNull(article);
		String title = article.getTitle()+System.currentTimeMillis();
		String content = article.getContent()+System.currentTimeMillis();
		String tags = article.getTags()+System.currentTimeMillis();
		article.setTitle(title);
		article.setContent(content);
		article.setTags(tags);
		articleDao.updateArticle(article);
		Article newArticle = articleDao.getArticleById(id);
		String [] oneArr = { title, content, tags };
		String [] twoArr = { 
			newArticle.getTitle(), 
			newArticle.getContent(), 
			newArticle.getTags() 
		};
		Assert.assertArrayEquals(oneArr, twoArr);
		
	};
	
	@Test
	@Ignore
	public void deleteArticle(){
		Article article = articleDao.getArticleById(id);
		Assert.assertNotNull(article);
		articleDao.deleteArticle(article);
		Article newArticle = articleDao.getArticleById(id);
		Assert.assertNull(newArticle);
	};
	
	@Test
	@Ignore
	public void getArticleById(){
		Article article = articleDao.getArticleById(id);
		Assert.assertNotNull(article);
	};
	
	@Test
	@Ignore
	public void getArticleByPage(){
		PageResult<Article> pr = new PageResult<Article>();
		String hql = "from Article";
		articleDao.getArticleByPage(hql, null, pr);
		Assert.assertTrue(pr.getRowsCount()>0);
	};
	
	@Test
	public void getMaxSortCount(){
		int max = articleDao.getMaxSortCount();
		System.out.println("--------------"+max);
	}
	
}
