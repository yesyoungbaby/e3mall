package cn.e3.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.EasyUITreeNode;
import cn.e3.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商城首页内容
 *
 * @author yesyoungbaby
 * @date 2021/11/12 16:52
 */
@Controller
public class ContentCatController {

    /**
     * 不能只是install接口不安装其实现类，否则无法注入
     */
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 获取内容分类列表
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/content/category/list")
    public List<EasyUITreeNode> getContentCatList(
            @RequestParam(name = "id", defaultValue = "0")Long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
        return list;
    }

    /**
     * 添加内容分类节点
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(value="/content/category/create", method= RequestMethod.POST)
    @ResponseBody
    public E3Result createContentCategory(Long parentId, String name) {
        //调用服务添加节点
        E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }
}
