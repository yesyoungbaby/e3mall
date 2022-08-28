package cn.e3.portal.controller;

import cn.e3.content.service.ContentService;
import cn.e3.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 商城首页展示
 *
 * @author yesyoungbaby
 * @date 2021/11/8 17:02
 */
@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Value("${cid}")
    private Long cid;


   /**
     * 显示门户首页
     * @return 首页的逻辑视图
     */
   /*
    @RequestMapping("/index")
    public String showIndex() {

        return "index";
    }*/

   @RequestMapping("/index")
    public String showIndex(Model model) {
        // 调内容服务查询内容列表
        List<TbContent> list = contentService.getContentsByCid(cid);
        model.addAttribute("ad1List", list);
        return "index";
    }

}
