package cn.e3.item.controller;

import cn.e3.item.pojo.Item;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;
import cn.e3.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/5 10:18
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String itemPage(@PathVariable Long itemId, Model model) {
        // 取商品信息
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        // 取商品描述信息
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        // 信息传递给页面
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", tbItemDesc);

        return "item";
    }
 }
