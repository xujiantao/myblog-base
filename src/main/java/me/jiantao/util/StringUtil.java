package me.jiantao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	private StringUtil(){};
	
	public static boolean hasText(String text) {
		return (text != null && text.trim() != "");
	}

	public static boolean hasNoText(String text) {
		return !hasText(text);
	}
	
	public static String filterHtmlTag(String text){
		text = text.trim();
		text = text.replaceAll("&nbsp;", "");
		//Pattern pattern = Pattern.compile("<[a-zA-Z=0-9/\"'.; -\\\\]+>");
		Pattern pattern = Pattern.compile("<[^<^>]+>");
		Matcher matcher = pattern.matcher(text);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String str = "<p><span style=\"color: rgb(0, 0, 0); font-family: Helvetica, &#39;Hiragino Sans GB&#39;, 微软雅黑, &#39;Microsoft YaHei UI&#39;, SimSun, SimHei, arial, sans-serif; font-size: 15px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20.8696px; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px;\"></span></p><p style=\"margin: 0pt 0px; text-align: justify; line-height: 23pt;\"><span style=\"font-family: &#39;Times new roman&#39;; font-size: 12pt;\"><span style=\"font-family: 宋体; font-size: 12pt;\"><span style=\"font-family:Tahoma\">一</span>、</span><span style=\"font-size: 12pt;\"><strong>勤于读书</strong></span><span style=\"font-family: 宋体; font-size: 12pt;\">：</span><span style=\"font-size: 12pt;\">读书和学习都是在和智慧聊天，用别人的经验，长自己的智慧，</span><span style=\"font-size: 12pt;\">何乐而不为。把读书变成一种习惯";
		System.out.println(filterHtmlTag(str));
	}
	
	public static String HtmlEscape(String text){
		return text.replace("<", "&lt;").replace(">", "&gt;");
	}
	
}
