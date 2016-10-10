package me.jiantao.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.jiantao.common.AutoConvert;
import me.jiantao.dao.Handle;
import me.jiantao.util.DateUtil;
import me.jiantao.util.StringUtil;

@Entity
@Table(name = "article")
@EqualsAndHashCode(callSuper = false)
@Data
public class Article extends BaseEntity<Integer> implements Handle {
	
	@Transient
	public static final int IS_TOP = 1;
	@Transient
	public static final int IS_NOT_TOP = 0;
	
	@Column(name = "title", length = 20, nullable = false)
	@AutoConvert
	private String title;

	@Column(name = "lead", length = 200)
	@AutoConvert
	private String lead;

	@Column(name = "content", length = 20000, nullable = false)
	@AutoConvert
	private String content;

	@Column(name = "tags")
	@AutoConvert
	private String tags;

	@Column(name = "visit_count")
	@AutoConvert
	private int visitCount = 0;

	@Column(name = "sort_count")
	@AutoConvert
	private int sortCount = 0;

	@Column(name = "is_top")
	@AutoConvert
	private int isTop = 0;

	@Transient
	private long startDate = 0;
	@Transient
	private long endDate = 0;
	@Transient
	private List<String> tagList;
	@Transient
	private String showCreateDate;
	@Transient
	private String showContent;

	@Override
	public String toString() {
		handle();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("id:").append(id);
		sb.append(",title:").append(title);
		sb.append(",lead:").append(lead);
		sb.append(",showCreateDate:").append(showCreateDate);
		sb.append(",visitCount:").append(visitCount);
		sb.append("}\n");
		return sb.toString();
	}

	public void handle() {
		if (StringUtil.hasText(tags)) {
			String[] arrTag = tags.split(",");
			if (arrTag != null) {
				List<String> tagList = new ArrayList<String>();
				for (String tag : arrTag) {
					tagList.add(tag);
				}
				this.setTagList(tagList);
			}
		}
		this.setShowCreateDate(DateUtil.MsToString(createDate));
	}
}
