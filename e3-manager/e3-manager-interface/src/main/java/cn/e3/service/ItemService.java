package cn.e3.service;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.EasyUIDataGridResult;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/10/14 16:43
 */
public interface ItemService {
    /**
     * id取商品
     * @param itemId bigInt
     * @return
     */
    TbItem getItemById(long itemId);

    /**
     * 获取商品分页列表
     * @param page  显式第几页
     * @param rows  该页记录数
     * @return
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    /**
     * 增加商品
     * @param item
     * @param desc 商品描述 对应商品描述类里的itemDesc字段
     * @return
     */
    E3Result addItem(TbItem item, String desc);

    /**
     * 查询商品描述
     * @param itemId
     * @return
     */
    TbItemDesc getItemDescById(long itemId);
}
