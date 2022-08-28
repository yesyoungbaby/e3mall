package cn.e3.search.mapper;

import cn.e3.common.pojo.SearchItem;

import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/11/27 21:00
 */
public interface ItemSearchMapper {
    /**
     * 表关联查询商品部分信息
     * @return
     */
    List<SearchItem> getSearchItemList();

    /**
     * 根据id搜商品
     * @param itemId
     * @return
     */
    SearchItem getSearchItemById(long itemId);
}
