package me.jiantao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;

import me.jiantao.exception.SearchException;

public class SolrUtil {

	private volatile static SolrUtil solrUtil;

	private String solrUrl = "";

	private SolrClient articleClient;

	private SolrUtil() {
		Properties config = new Properties();
		try {
			config.load(SolrUtil.class.getClassLoader().getResourceAsStream(
					"solr.properties"));
			solrUrl = config.getProperty("solr.url");
			articleClient = new HttpSolrClient(solrUrl + "article");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void create() {
		if (solrUtil == null) {
			solrUtil = new SolrUtil();
		}
	}

	public static SolrUtil getInstance() {
		if (solrUtil == null) {
			create();
		}
		return solrUtil;
	}

	public SolrClient getArticleClient() {
		return articleClient;
	}

	/**
	 * 给指定的语句分词。
	 * 
	 * @param sentence 被分词的语句
	 * @return 分词结果
	 */
	public List<String> getAnalysis(String sentence) {
		SolrClient client = SolrUtil.getInstance().getArticleClient();
		FieldAnalysisRequest request = new FieldAnalysisRequest(
				"/analysis/field");
		request.addFieldName("title");// 字段名，随便指定一个支持中文分词的字段
		request.setFieldValue("");// 字段值，可以为空字符串，但是需要显式指定此参数
		request.setQuery(sentence);

		FieldAnalysisResponse response = null;
		try {
			response = request.process(client);
		} catch (Exception e) {
			throw new SearchException(e, "获取分词出错");
		}

		List<String> results = new ArrayList<String>();
		Iterator<AnalysisPhase> it = response.getFieldNameAnalysis("title")
				.getQueryPhases().iterator();
		while (it.hasNext()) {
			AnalysisPhase pharse = (AnalysisPhase) it.next();
			//只返回经过StopFilter过滤器之后的分词结果
			if ("org.apache.lucene.analysis.core.StopFilter".equals(pharse
					.getClassName())) {
				List<TokenInfo> list = pharse.getTokens();
				list.forEach(info -> {
					results.add(info.getText());
				});
			}

		}
		return results;
	}

}
