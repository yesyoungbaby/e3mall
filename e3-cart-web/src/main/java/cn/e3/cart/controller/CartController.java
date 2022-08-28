package cn.e3.cart.controller;

import cn.e3.cart.service.CartService;
import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.CookieUtils;
import cn.e3.common.util.JsonUtils;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbUser;
import cn.e3.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/10 10:19
 */
@Controller
public class CartController {

    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    /**
     * 商品添加进购物车
     * @param itemId 从url中取参数
     *   将URL中占位符参数{xxx}绑定到处理器类的方法形参中@PathVariable(“xxx“)
     * @param num  从request中取参数
     * @param request  用于取cookie中的数据
     * @param response 再将数据写入cookie
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId,
                          @RequestParam(defaultValue = "1")Integer num,
                          HttpServletRequest request, HttpServletResponse response) {

        // 判断用户是否登录 通过拦截器中的代码
        TbUser user = (TbUser) request.getAttribute("user");
        // 登录状态，把购物车写入redis，结束方法
        if(user != null) {
            cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }

        // 未登录，购物车写入cookie
        // 从cookie中取商品购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        // 判断要添加的商品是否已经在购物车列表中
        boolean flag = false;
        for(TbItem item : cartList) {
            // 在则更新购物车商品数量，跳出循环
            if (item.getId() == itemId.longValue()) {
                flag = true;
                item.setNum(item.getNum() + num);
                break;
            }
        }
        // 找不到则，根据商品id查找商品，将其数量设置为1
        if(!flag) {
            TbItem item = itemService.getItemById(itemId);
            item.setNum(num);
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                item.setImage(image.split(",")[0]);
            }
            // 将商品添加到购物车列表
            cartList.add(item);
        }
        // 将购物车列表写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);

        //返回添加成功页面
        return "cartSuccess";
    }

    /**
     * 从cookie中取商品购物车列表
     * @param request
     * @return
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        // json转对象列表
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }

    /**
     * 展示购物车商品列表 从cookie中取数据
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response) {

        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);

        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录状态
        if (user != null) {

            //如果不为空，把cookie中的购物车商品和服务端的购物车商品合并。
            cartService.mergeCart(user.getId(), cartList);
            //把cookie中的购物车删除
            CookieUtils.deleteCookie(request, response, "cart");
            //从服务端取购物车列表
            cartList = cartService.getCartList(user.getId());

        }


        request.setAttribute("cartList", cartList);
        return "cart";
    }

    /**
     * 更新购物车商品数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,
                                  @PathVariable Integer num,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.updateCartNum(user.getId(), itemId, num);
            return E3Result.ok();
        }

        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        // 遍历列表找到数量变更的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                //更新数量
                tbItem.setNum(num);
                break;
            }
        }
        //把购物车列表写回cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        //返回成功
        return E3Result.ok();
    }

    /**
     * 删除购物车商品
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
                                 HttpServletResponse response) {

        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }

        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历列表，找到要删除的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                //删除商品  注意此步
                cartList.remove(tbItem);
                //跳出循环
                break;
            }
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        //返回逻辑视图
        return "redirect:/cart/cart.html";
    }
}
