package cn.e3.order.controller;

import cn.e3.cart.service.CartService;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单Controller
 *  走到这里的逻辑一定是用户登录的
 *  需要拦截器
 *
 * @author yesyoungbaby
 * @date 2021/12/14 9:34
 */
@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    /**
     * 取用户购物车数据，返回给订单结算页面
     * @param request
     * @return
     */
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request) {
        // 从登录拦截器中取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        // 根据用户信息，查收货地址（表）选择的付款方式，这些逻辑都没写
        // 取用户购物车列表数据
        List<TbItem> itemList = cartService.getCartList(user.getId());
        // req将所有查询结果传递给页面 ""里是页面需要的参数，这里只传递了购物车列表数据
        request.setAttribute("cartList", itemList);
        return "order-cart";
    }
}
