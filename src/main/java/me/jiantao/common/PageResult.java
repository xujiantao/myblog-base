package me.jiantao.common;

import java.util.List;

import me.jiantao.dao.Handle;
import me.jiantao.util.CollectionUtil;
/**
 * 分页对象，可以根据此对象设置查询条件，
 * 然后后端查询结束之后，直接在根据此对象
 * 设置返回条件。
 * 注意在后端查询完毕之后，必须设置list和
 * rowsCount两个字段，然后调用handle()
 * 方法自动计算别的属性
 * 
 * @author xujiantao
 *
 * @param <T>
 */
public class PageResult<T> {
	
	public static final String SORT_ASC = "asc";
	public static final String SORT_DESC = "desc";
	
	private int pageNo = 1;
	private int pageSize = 10;
	// 排序方式 asc desc
	private String sort = SORT_ASC;
	// 排序字段
	private String sortColumn = "sortCount";
	// 查询返回的列表，必须设置
	private List<T> list;
	// 是否有上一页
	private boolean hasPrev;
	// 上一页的页数
	private int prevPageNo;
	// 是否有下一页
	private boolean hasNext;
	// 下一页的页数
	private int nextPageNo;
	// 总行数，必须设置
	private long rowsCount;
	// 总页数
	private int pageCount;
	// 用来存储其他数据
	private Object other;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(long rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	private void setPageCount() {
		pageCount = (int) (rowsCount % pageSize == 0 ? rowsCount / pageSize
				: rowsCount / pageSize + 1);
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	private void setHasNext() {
		if (pageNo == getPageCount()) {
			hasNext = false;
		} else {
			hasNext = true;
		}
	}

	public boolean isHasPrev() {
		return hasPrev;
	}

	private void setHasPrev() {
		if (pageNo == 1) {
			hasPrev = false;
		} else {
			hasPrev = true;
		}
	}

	public int getPrevPageNo() {
		return prevPageNo;
	}

	private void setPrevPageNo() {
		if (isHasPrev()) {
			prevPageNo = pageNo - 1;
		}
	}

	public int getNextPageNo() {
		return nextPageNo;
	}

	private void setNextPageNo() {
		if (isHasNext()) {
			nextPageNo = pageNo + 1;
		}
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	

	public Object getOther() {
		return other;
	}

	public void setOther(Object other) {
		this.other = other;
	}

	public void handle() {
		setPageCount();
		setHasNext();
		setHasPrev();
		setNextPageNo();
		setPrevPageNo();
		if(CollectionUtil.isNotEmpty(list)){
			T t = list.get(0);
			if(t instanceof Handle){
				list.forEach(nt -> {
					Handle handle = (Handle)nt;
					handle.handle();
				});
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("pageNo:").append(pageNo);
		sb.append(",pageSize:").append(pageSize);
		sb.append(",rowsCount:").append(rowsCount);
		sb.append(",pageCount:").append(getPageCount());
		sb.append(",list:").append(list);
		sb.append("}\n");
		return sb.toString();
	}
}
