package cn.e3.cart.service;

import cn.e3.common.pojo.E3Result;
import cn.e3.pojo.TbItem;

import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/11 15:08
 */
public interface CartService {

    /**
     * 将登录状态下的购物车写入redis
     *  数据类型是hash  value：商品信息
     * @param userId  key
     * @param itemId  field
     * @param num
     * @return
     */
    E3Result addCart(long userId, long itemId, int num);

    /**
     * 没登录时将商品放在了cookie
     *  后来又登录了将商品放在了服务端
     *  所以需要将二者合并
     * @param userId
     * @param itemList
     * @return
     */
    E3Result mergeCart(long userId, List<TbItem> itemList);

    /**
     *
     * @param userId
     * @return
     */
    List<TbItem> getCartList(long userId);

    /**
     *
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    E3Result updateCartNum(long userId, long itemId, int num);

    /**
     *
     * @param userId
     * @param itemId
     * @return
     */
    E3Result deleteCartItem(long userId, long itemId);
}
