package me.jiantao.search.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import me.jiantao.common.Constant;
import me.jiantao.common.PageResult;
import me.jiantao.exception.SearchException;
import me.jiantao.po.Article;
import me.jiantao.search.IArticleSearchService;
import me.jiantao.util.BeanUtil;
import me.jiantao.util.CommonUtil;
import me.jiantao.util.SolrUtil;
import me.jiantao.util.StringUtil;

@Service
public class ArticleSearchServiceImpl implements IArticleSearchService {

	private static Logger logger = Logger.getLogger(ArticleSearchServiceImpl.class);
	
	@Override
	public void saveArticle(Article article) {
		SolrClient articleClient = SolrUtil.getInstance().getArticleClient();
		try {
			SolrInputDocument doc = BeanUtil.BeanToSolrInputDocument(article);
			String content = StringUtil.filterHtmlTag(article.getContent());
			String showContent;
			if (content.length() > 200) {
				showContent = content.substring(0, 200) + "...";
			}else{
				showContent = content;
			}
			doc.setField(Constant.INDEX_FIELD_SHOW_CONTENT, showContent);
			articleClient.add(doc);
			articleClient.commit();
		} catch (SolrServerException e) {
			throw new SearchException(e, "添加索引异常, id: " + article.getId());
		} catch (IOException e) {
			throw new SearchException(e, "添加索引异常, id: " + article.getId());
		}
	}

	@Override
	public void updateArticle(Article article) {
		deleteArticle(article.getId());
		saveArticle(article);
	}

	@Override
	public void deleteArticle(int id) {
		SolrClient articleClient = SolrUtil.getInstance().getArticleClient();
		try {
			articleClient.deleteById(id + "");
			articleClient.commit();
		} catch (SolrServerException e) {
			throw new SearchException(e, "删除索引异常, id: " + id);
		} catch (IOException e) {
			throw new SearchException(e, "删除索引异常, id: " + id);
		}
	}

	@Override
	public PageResult<Article> getArticleByPage(String keyword,
			PageResult<Article> pr) throws SolrServerException, IOException {
		SolrClient client = SolrUtil.getInstance().getArticleClient();
		SolrQuery query = new SolrQuery(); // 查询对象
		String queryParam = buildQueryParams(keyword);
		logger.info(queryParam);
		query.set("q", queryParam); // 查询关键字
		query.set("start", (pr.getPageNo() - 1) * pr.getPageSize()); // 从哪一行开始
		query.set("rows", pr.getPageSize()); // 返回多少行
		//query.set("sort", pr.getSortColumn() + " " + pr.getSort()); // 排序  //使用权重去排序
		//设置高亮
		query.setHighlight(true); // 开启高亮组件或用query.setParam("hl", "true");     songQuery.addHighlightField("Song_Name,Song_SingerName");// 高亮字段
		query.set("hl.fl", Constant.INDEX_FIELD_TITLE + "," + Constant.INDEX_FIELD_LEAD + "," + Constant.INDEX_FIELD_SHOW_CONTENT);//启用多字段高亮
		query.set("hl.fragsize", 0);  //返回的最大字符数，默认100,0为不限制
		query.setHighlightSimplePre("<font style=\"color:red;font-weight:bold;\">"); //标记，高亮关键字前缀
		query.setHighlightSimplePost("</font>");//后缀
		
		//权重设置
		query.set("defType","dismax");
		String qf = Constant.INDEX_FIELD_TITLE + "^3 "+ Constant.INDEX_FIELD_LEAD +"^1 " + Constant.INDEX_FIELD_SHOW_CONTENT + " " + Constant.INDEX_FIELD_PINYIN + " " + Constant.INDEX_FIELD_PINYIN_FIRST;
		query.set("qf", qf);
		
		QueryResponse rsp = client.query(query); // 对返回结果的封装
		SolrDocumentList docList = rsp.getResults(); // 返回返回的列表
		pr.setRowsCount(docList.getNumFound()); // 设置总行数
		List<Article> list = convertHighlight(rsp);
		logger.info(list);
		handleArticle(list); // 处理对list进行一下别的处理
		pr.setList(list); // 设置值
		pr.handle(); // 计算分页对象的属性
		pr.setOther(queryParam); //存储分词结果
		return pr;
	}
	
	private String buildQueryParams(String keyword){
		StringBuilder queryParam = new StringBuilder();
		List<String> words = SolrUtil.getInstance().getAnalysis(keyword);
		Set<String> set = new HashSet<String>(words);
		String [] strs = new String[set.size()];
		Arrays.sort(set.toArray(strs), new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int a = o1.length();
				int b = o2.length();
				return a - b;
			}
		});
		
		for (String word : strs) {
			queryParam.append(word).append(" ");
		}
		return queryParam.toString();
	}
	
	public static void main(String[] args) {
		Set<String> set = new TreeSet<String>();
		
		set.add("ccc");
		set.add("dddd");
		set.add("eeeee");
		set.add("1111");
		set.add("a");
		set.add("bb");
		
		String [] strs = new String[set.size()];
		
		Arrays.sort(set.toArray(strs), new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				int a = o1.toString().length();
				int b = o2.toString().length();
				return a - b;
			}
		});
		
		System.out.println(Arrays.toString(strs));
		
	}
	
	//处理文章列表
	private void handleArticle(List<Article> list) {
		if (list != null && list.size() > 0) {
			for (Article article : list) {
				article.setContent("");
			}
		}
	}
	
	//为文章列表添加索引
	@Override
	public void addAllArticleIndex(List<Article> list) {
		if(CommonUtil.listIsNotNull(list)){
			list.forEach(article -> {
				saveArticle(article);
			});
		}
	}
	
	//删除所有索引
	@Override
	public void deleteAllArticleIndex() {
		SolrClient client = SolrUtil.getInstance().getArticleClient();
		String query = "*";
		try {
			client.deleteByQuery(query);
			client.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将高亮的数据封装到搜索结果中
	 * @param rsp
	 * @return
	 */
	private List<Article> convertHighlight(QueryResponse rsp){
		SolrDocumentList docList = rsp.getResults(); // 返回返回的列表
		Map<String, Map<String, List<String>>> map = rsp.getHighlighting();
		List<Article> list = BeanUtil.SolrDocumentListToBeanList(Article.class, docList); // 数据转换
		if(CommonUtil.listIsNotNull(list)){
			list.forEach(article -> {
				Map<String, List<String>> newMap = map.get(article.getId().toString());
				if(CommonUtil.mapIsNotNull(newMap)){
					newMap.forEach((key, value) -> {
						if(Constant.INDEX_FIELD_TITLE.equals(key)){
							article.setTitle(value.get(0));
						}
						if(Constant.INDEX_FIELD_LEAD.equals(key)){
							article.setLead(value.get(0));
						}
						if(Constant.INDEX_FIELD_SHOW_CONTENT.equals(key)){
							article.setShowContent(value.get(0));
						}
					});
				}
			});
		}
		return list;
	}
	
}
