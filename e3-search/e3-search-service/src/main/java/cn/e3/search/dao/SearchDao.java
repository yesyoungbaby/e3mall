package cn.e3.search.dao;

import cn.e3.common.pojo.SearchItem;
import cn.e3.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 搜索通用逻辑
 *
 * @author yesyoungbaby
 * @date 2021/11/29 17:42
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    /**
     * 根据条件查询索引库
     *
     * @param query
     * @return
     */
    public SearchResult search(SolrQuery query) {
        try {
            // 根据query查询索引库
            QueryResponse queryResponse = solrServer.query(query);
            // 取查询结果
            SolrDocumentList solrDocuments = queryResponse.getResults();
            // 取查询结果总记录数
            long numFound = solrDocuments.getNumFound();
            //
            SearchResult result = new SearchResult();
            result.setRecordCount(numFound);

            //取商品列表，需要取高亮显示
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            List<SearchItem> itemList = new ArrayList<>();
            for (SolrDocument solrDocument : solrDocuments) {
                SearchItem item = new SearchItem();
                item.setId((String) solrDocument.get("id"));
                item.setCategoryName((String) solrDocument.get("item_category_name"));
                item.setImage((String) solrDocument.get("item_image"));
                item.setPrice((long) solrDocument.get("item_price"));
                item.setSellPoint((String) solrDocument.get("item_sell_point"));
                //取高亮显示
                List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
                String title = "";
                if (list != null && list.size() > 0) {
                    title = list.get(0);
                } else {
                    title = (String) solrDocument.get("item_title");
                }
                item.setTitle(title);
                //添加到商品列表
                itemList.add(item);
            }
            result.setItemList(itemList);
            //返回结果
            return result;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
