package me.jiantao.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * 网络方面的工具类，需要依赖httpclient4.5.1
 * 和httpmime4.5.1
 * @author xujiantao
 *
 */
public class NetUtil {
	
	private NetUtil(){};
	
	/**
	 * 发送get请求
	 * 
	 * @param url 请求的url
	 * @param params 参数Map
	 * @return
	 */
	public static String sendGet(String url, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		if (CollectionUtil.isEmpty(params)) {
			params.forEach((key, value) -> {
				if(value != null){
					sb.append(key).append("=").append(value).append("&");
				}
			});
			String psStr = sb.substring(0, sb.length() - 1);
			if (url.contains("?")) {
				url += ("&" + psStr);
			} else {
				url += ("?" + psStr);
			}
		}
		// 构造client对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 构造get请求
		HttpGet get = new HttpGet(url);
		get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		try {
			// 执行
			HttpResponse resp = client.execute(get);
			// 获得相应的内容
			HttpEntity entity = resp.getEntity();
			// 将相应的内容转化为字符串的形式
			String result = EntityUtils.toString(entity);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("发送get请求失败");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送post请求
	 * 
	 * @param url 请求的url
	 * @param params 参数Map
	 * @return
	 */
	public static String sendPost(String url, Map<String, Object> params) {
		// 构造client对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 构造请求
		HttpPost post = new HttpPost(url);
		post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// post请求独有的设置参数方式
		List<NameValuePair> readParams = new ArrayList<NameValuePair>();
		if (CollectionUtil.isEmpty(params)) {
			params.forEach((key, value) -> {
				if(value != null){
					if (value instanceof Collection) {
						Collection<?> list = (Collection<?>) value;
						list.forEach(item -> {
							readParams.add(new BasicNameValuePair(key, item.toString()));
						});
					} else {
						readParams.add(new BasicNameValuePair(key, value.toString()));
					}
				}
			});
		}
		try {
			// 封装参数
			UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(readParams, "utf-8");
			post.setEntity(httpEntity);
			// 执行请求
			HttpResponse resp = client.execute(post);
			// Header[] hs =resp.getAllHeaders(); //所有的响应头
			HttpEntity entity = resp.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("发送post请求失败");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 上传
	 * 
	 * @param url 请求的url
	 * @param params 要上传的文件对象
	 * @return
	 */
	public static String sendPostUpload(String url, File file) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			FileBody bin = new FileBody(file);
			StringBody comment = new StringBody("A binary file of some kind",ContentType.TEXT_PLAIN);

			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addPart("file", bin).addPart("comment", comment).build();

			httppost.setEntity(reqEntity);
			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: "
							+ resEntity.getContentLength());
				}
				String result = EntityUtils.toString(resEntity);
				return result;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("上传请求失败");
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
