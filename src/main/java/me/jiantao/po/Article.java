package me.jiantao.po;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import me.jiantao.common.Handle;
import me.jiantao.util.DateUtil;
import me.jiantao.util.StringUtil;

@Entity
@Table(name = "article")
public class Article extends BaseEntity<Integer> implements Handle {

	@Column(name = "title", length = 20, nullable = false)
	private String title;

	@Column(name = "lead", length = 200)
	private String lead;

	@Column(name = "content", length = 20000, nullable = false)
	private String content;

	@Column(name = "tags")
	private String tags;

	@Column(name = "visit_count")
	private int visitCount = 0;

	@Column(name = "sort_count")
	private int sortCount = 0;

	@Column(name = "is_top", columnDefinition = "default 0")
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getShowCreateDate() {
		return showCreateDate;
	}

	public void setShowCreateDate(String showCreateDate) {
		this.showCreateDate = showCreateDate;
	}

	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public int getSortCount() {
		return sortCount;
	}

	public void setSortCount(int sortCount) {
		this.sortCount = sortCount;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public String getShowContent() {
		return showContent;
	}

	public void setShowContent(String showContent) {
		this.showContent = showContent;
	}

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
		if (StringUtil.IsNotNull(tags)) {
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
