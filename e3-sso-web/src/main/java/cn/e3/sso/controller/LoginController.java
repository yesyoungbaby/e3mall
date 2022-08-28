package cn.e3.sso.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.CookieUtils;
import cn.e3.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/7 14:50
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService service;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
                            HttpServletRequest request,
                          HttpServletResponse response) {
        E3Result res = service.login(username, password);
        // 判断是否登录成功
        if(res.getStatus() == 200) {
            String token = res.getData().toString();

            //token写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }

        return res;
    }
}
