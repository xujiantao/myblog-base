package me.jiantao.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import me.jiantao.common.PageResult;
import me.jiantao.dao.ArticleDao;
import me.jiantao.po.Article;
import me.jiantao.search.IArticleSearchService;
import me.jiantao.service.IArticleService;
import me.jiantao.util.StringUtil;

@Service
public class ArticleServiceImpl implements IArticleService {

	@Resource
	private ArticleDao articleDao;

	@Resource
	private IArticleSearchService iArticleSearchService;

	@Override
	public int saveArticle(Article article) {
		Assert.notNull(article, "参数不能为null");
		Assert.hasText(article.getTitle(), "文章标题为空");
		Assert.hasText(article.getContent(), "文章内容为空");
		
		article.setCreateDate(System.currentTimeMillis());
		return articleDao.saveArticle(article);
	}

	@Override
	public void updateArticle(Article article) {
		Assert.notNull(article, "参数不能为null");
		Assert.hasText(article.getTitle(), "文章标题为空");
		Assert.hasText(article.getContent(), "文章内容为空");
		
		Article oldArticle = articleDao.getArticleById(article.getId());
		oldArticle.setTitle(article.getTitle());
		oldArticle.setLead(article.getLead());
		oldArticle.setContent(article.getContent());
		oldArticle.setTags(article.getTags());
		articleDao.updateArticle(oldArticle);
	}

	@Override
	public void deleteArticle(int id) {
		Assert.state(id >= 0, "参数错误");
		Article article = getArticleById(id);
		Assert.notNull(article, "文章不存在");
		
		articleDao.deleteArticle(article);
	}

	@Override
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}

	@Override
	public PageResult<Article> getArticleByPage(Article article,
			PageResult<Article> pr) {

		StringBuilder hql = new StringBuilder("from Article");
		Map<String, Object> params = new HashMap<String, Object>();
		if (article != null) {
			hql.append(" where");
			if (StringUtil.IsNotNull(article.getTitle())) {
				hql.append(" title like :title");
				params.put("title", "%" + article.getTitle() + "%");
			}
		}
		// hql.append(" order by ").append(pr.getSortColumn()).append(" ").append(pr.getSort());
		hql.append(" order by ").append("isTop").append(" ")
				.append(PageResult.SORT_DESC);
		hql.append(",sortCount").append(" ").append(PageResult.SORT_DESC);
		hql.append(",id").append(" ").append(PageResult.SORT_DESC);

		return articleDao.getArticleByPage(hql.toString(), params, pr);
	}

	@Override
	public Article getArticleByIdAndAddVisitCount(int id) {
		Article article = articleDao.getArticleById(id);
		int visitCount = article.getVisitCount() + 1;
		article.setVisitCount(visitCount);
		articleDao.updateArticle(article);
		return article;
	}

	@Override
	public void addAllArticleIndex() {
		PageResult<Article> pr = new PageResult<>();
		pr.setPageSize(99999);
		articleDao.getArticleByPage("from Article", null, pr);
		iArticleSearchService.addAllArticleIndex(pr.getList());
	}

	@Override
	public void deleteAllArticleIndex() {
		iArticleSearchService.deleteAllArticleIndex();
	}

	@Override
	public void executeSetTop(int id) {
		Article article = articleDao.getArticleById(id);
		checkArticleExists(article);
		// 取消原先的置顶
		Map<String, Object> params = new HashMap<>();
		params.put("isTop", Article.IS_TOP);
		Article topArticle = articleDao.getUniqueArticle(
				"from Article where isTop=:isTop", params);
		if (topArticle != null) {
			topArticle.setIsTop(Article.IS_NOT_TOP);
			articleDao.updateArticle(topArticle);
		}
		// 置顶新文章
		article.setIsTop(Article.IS_TOP);
		articleDao.updateArticle(article);
	}

	@Override
	public void cancelSetTop(int id) {
		Article article = articleDao.getArticleById(id);
		checkArticleExists(article);
		article.setIsTop(Article.IS_NOT_TOP);
		articleDao.updateArticle(article);
	}

	@Override
	public void executeRecommend(int id) {
		Article article = articleDao.getArticleById(id);
		checkArticleExists(article);
		int maxSortCount = articleDao.getMaxSortCount();
		int sortCount = maxSortCount + 1;
		article.setSortCount(sortCount);
		articleDao.updateArticle(article);
	}

	@Override
	public void cancelRecommend(int id) {
		Article article = articleDao.getArticleById(id);
		checkArticleExists(article);
		article.setSortCount(0);
		articleDao.updateArticle(article);
	}
	
	private void checkArticleExists(Article article){
		Assert.notNull(article, "文章不存在");
	}

}
