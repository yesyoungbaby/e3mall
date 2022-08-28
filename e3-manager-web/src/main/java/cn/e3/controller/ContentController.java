package cn.e3.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.content.service.ContentService;
import cn.e3.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/11/22 9:43
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addContent(TbContent content) {
        return contentService.addContent(content);
    }
}
