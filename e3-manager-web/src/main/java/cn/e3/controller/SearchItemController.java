package cn.e3.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品索引
 *
 * @author yesyoungbaby
 * @date 2021/11/28 17:56
 */
@Controller
public class SearchItemController {

    @Autowired
    private ItemSearchService itemSearchService;


    @RequestMapping(value = "/index/item/import")
    @ResponseBody
    public E3Result importItemList() {
       return itemSearchService.importAllItems();
    }
}
