package cn.e3.search.service;

import cn.e3.common.pojo.SearchResult;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/11/29 18:23
 */
public interface SearchService {

    /**
     * 商城首页搜索栏功能
     * @param keyword 搜索关键词
     * @param page 分页数
     * @param rows
     * @return
     */
    SearchResult search(String keyword, int page, int rows);
}
