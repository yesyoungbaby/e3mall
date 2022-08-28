package cn.e3.sso.controller;

import cn.e3.common.pojo.E3Result;
import cn.e3.pojo.TbUser;
import cn.e3.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/6 17:09
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterService service;

    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkInfo(@PathVariable String param, @PathVariable Integer type) {
        return service.checkInfo(param, type);
    }

    @RequestMapping(value="/user/register", method= RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user) {
        E3Result e3Result = service.register(user);
        return e3Result;
    }
}
