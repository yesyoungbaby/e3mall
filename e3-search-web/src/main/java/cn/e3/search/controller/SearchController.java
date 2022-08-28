package cn.e3.search.controller;

import cn.e3.common.pojo.SearchResult;
import cn.e3.search.service.SearchService;
import javassist.compiler.ast.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.*;
import java.io.UnsupportedEncodingException;

/**
 * 处理首页搜索栏发来的请求
 *
 * @author yesyoungbaby
 * @date 2021/11/29 21:27
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String searchItemList(String keyword,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 Model model) {
        // 页面搜索的是中文。需要转码
        // 1.改tomcat配置文件,默认字符集ISO-8859-1改为UTF-8。但这里用的是tomcat插件
        // 2.手动转码
        try {
            keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        // 把结果传递给页面 ""内是页面需要的
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("recordCount", result.getRecordCount());
        model.addAttribute("itemList", result.getItemList() );
        //返回逻辑视图
        return "search";

    }
}
