package cn.e3.search.service.impl;

import cn.e3.common.pojo.SearchResult;
import cn.e3.search.dao.SearchDao;
import cn.e3.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首页商品搜索功能
 *
 * @author yesyoungbaby
 * @date 2021/11/29 20:47
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    /**
     * 从索引库中搜索商品
     * @param keyword 搜索关键词
     * @param page 分页数
     * @param rows
     * @return
     */
    @Override
    public SearchResult search(String keyword, int page, int rows) {
        //使用参数填充一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(keyword);
        //设置分页条件
        if (page <=0 ) {
            page =1;
        }
        query.setStart((page - 1) * rows);
        query.setRows(rows);
        //设置默认搜索域
        query.set("df", "item_title");
        //开启高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        //调用dao执行查询
        SearchResult searchResult = searchDao.search(query);
        //计算总页数
        long recordCount = searchResult.getRecordCount();
        int totalPage = (int) (recordCount / rows);
        if (recordCount % rows > 0) {
            totalPage ++;
        }
        //添加到返回结果
        searchResult.setTotalPages(totalPage);
        //返回结果
        return searchResult;
    }

}
