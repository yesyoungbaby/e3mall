package cn.e3.sso.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.JsonUtils;
import cn.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/8 17:22
 */
@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    /**
     *往上回溯流程：
     *  1.各个需要用户登录信息的页面通过js从浏览器cookie中取token
     *  2.页面ajax携带token请求该链接
     *  3.执行该逻辑
     *  4.token值是redis中的k 用户信息是v
     * @param token
     * @param callback  跨域的话js传来此参数
     * @return
     */
    @RequestMapping(value = "user/token/{token}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getUerByToken(@PathVariable String token, String callback) {
        E3Result res = tokenService.getUserByToken(token);
        //响应结果前需要判断是否为jsonp请求
        if(StringUtils.isBlank(callback)) {
            return JsonUtils.objectToJson(res);
        }
        // 如果不为空，则将结果封装为js语句再响应
        return callback + "(" + JsonUtils.objectToJson(res) + ");";
    }
}
