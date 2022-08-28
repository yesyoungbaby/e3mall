package cn.e3.search.service;

import cn.e3.common.pojo.E3Result;

/**
 * 索引库维护接口
 *
 * @author yesyoungbaby
 * @date 2021/11/27 22:09
 */
public interface ItemSearchService {
    /**
     * 向索引库导入商品数据
     * manager-web的一键导入功能
     * @return
     */
    E3Result importAllItems();
}
