package cn.e3.search.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.SearchItem;
import cn.e3.search.mapper.ItemSearchMapper;
import cn.e3.search.service.ItemSearchService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品一键导入索引库
 *
 * @author yesyoungbaby
 * @date 2021/11/27 22:10
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private ItemSearchMapper mapper;
    /**
     * 为什么要注入而不自己new
     * 单例模式就够，而不是每一次调用都创建一个solrServer
     */
    @Autowired
    private SolrServer solrServer;

    /**
     * 一键导入索引库
     * @return
     */
    @Override
    public E3Result importAllItems() {
        List<SearchItem> list = mapper.getSearchItemList();
        try {
            // 对每一个商品搜索信息
            for(SearchItem searchItem : list) {
                // 创建solr文档对象
                SolrInputDocument document = new SolrInputDocument();
                // 向文档对象中添加业务域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSellPoint());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategoryName());
                // 将文档对象写入索引库
                solrServer.add(document);
            }
            solrServer.commit();
            return E3Result.ok();
        }catch(Exception e) {
            e.printStackTrace();
            return E3Result.build(500, "数据导入索引库失败");
        }
    }
}
