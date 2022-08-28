package cn.e3.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.EasyUIDataGridResult;
import cn.e3.pojo.TbItem;
import cn.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/10/14 16:57
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     *
     * @param itemId 需要手输
     * @return 返回的是POJO 并用json格式返回，所以要用@ResponseBody
     */
    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        // controller将从前端接收到的参数传给接口 经历了自动拆箱
        TbItem item = itemService.getItemById(itemId);
        return item;
    }

    /**
     * 分页获取商品列表
     * 接收到的前端的两个整形参数
     * @param page 分页显式第几页数据。DataGrid默认的 1，传至后台
     * @param rows 该页多少行数据。 DataGrid初始化自带 30  不需要手输
     * @return 返回的是带有记录列表的类
     */
    @RequestMapping(value = "/item/list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }

    /**
     *
     * @param item
     * @param desc
     * @return 返回的是用哪个E3Result 能够满足前端的需求 携带了status 200 msg ok
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item, String desc) {
        return itemService.addItem(item, desc);
    }
}
