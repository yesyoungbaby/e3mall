package cn.e3.cart.service.impl;

import cn.e3.cart.service.CartService;
import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.JedisClient;
import cn.e3.common.util.JsonUtils;
import cn.e3.mapper.TbItemMapper;
import cn.e3.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/12/11 15:13
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 商品添加至购物车
     * @param userId  key
     * @param itemId  field
     * @param num
     * @return
     */
    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        // 拿参数判断该用户的购物车是否在redis中
        Boolean hashExist =  jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        // 如果存在则直接商品数量相加
        if(hashExist) {
            // redis中取数据
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            // 将数据转成商品
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            item.setNum(item.getNum() + num);
            // 商品信息再写回redis
            // 存储的数据类型是hash key:userId field:itemId v:商品信息
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
            return E3Result.ok();
        }

        //如果不存在，根据商品id取商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //设置购物车数据量
        item.setNum(num);
        //取一张图片
        String image = item.getImage();
        if (StringUtils.isNotBlank(image)) {
            item.setImage(image.split(",")[0]);
        }
        // 将商品添加到购物车列表
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    /**
     *同一用户两种状态的购物车合并
     * @param userId
     * @param itemList
     * @return
     */
    @Override
    public E3Result mergeCart(long userId, List<TbItem> itemList) {

        //遍历商品列表
        //把列表添加到购物车。
        //判断购物车中是否有此商品
        //如果有，数量相加
        //如果没有添加新的商品

        for (TbItem tbItem : itemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        //返回成功
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户id查询购车列表
        // 取hash类型的value
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String string : jsonList) {
            //创建一个TbItem对象
            TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
            //添加到列表
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public E3Result updateCartNum(long userId, long itemId, int num) {
        //从redis中取商品信息
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        //更新商品数量
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        tbItem.setNum(num);
        //写入redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        // 删除购物车商品
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }
}
