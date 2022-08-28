package cn.e3.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品搜索结果pojo
 * 使用搜索服务时响应搜索操作的pojo，controller响应前端
 *
 * @author yesyoungbaby
 * @date 2021/11/29 17:28
 */
public class SearchResult implements Serializable  {

    private long recordCount;
    private int totalPages;
    private List<SearchItem> itemList;

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
