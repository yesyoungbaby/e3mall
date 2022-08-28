package cn.e3.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.EasyUIDataGridResult;
import cn.e3.common.util.IDUtils;
import cn.e3.common.util.JedisClient;
import cn.e3.common.util.JsonUtils;
import cn.e3.mapper.TbItemDescMapper;
import cn.e3.mapper.TbItemMapper;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;
import cn.e3.pojo.TbItemExample;
import cn.e3.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.*;


/**
 * 服务启动后，spring扫描到此处，创建首字母小写的bean itemServiceImpl
 *
 * @author yesyoungbaby
 * @date 2021/10/14 16:46
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    /**
     * 和web已经是两个工程了
     * @param itemId bigInt  传递的参数
     * @return
     */
    @Override
    public TbItem getItemById(long itemId){
/*      参数应该判空吧，，，
        if(itemId = null) {}*/

        // 商品详情 查商品时先查缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNoneBlank(json)) {
                TbItem res = JsonUtils.jsonToPojo(json, TbItem.class);
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        // 查询的结果添加至缓存
        try {
            // set(key-string 前后缀, value-json字符串)
           jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
           // expire(k, 过期时间)
           jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }

    /**
     *
     * @param page  显式第几页
     * @param rows  该页记录数
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        // 1.设置分页信息 静态方法
        PageHelper.startPage(page, rows);
        // 2.查询
        TbItemExample example = new TbItemExample();
        // dao层返回的是记录列表 后续的将其包装返回
        List<TbItem> list = itemMapper.selectByExample(example);
        // 3.创建返回值
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //   3.1设置total值 也可以直接统计list的size
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        //   3.2设置rows
        result.setRows(list);
        // 取分页查询结果

        return result;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        // 0.其实首先应该做参数校验
        // 1.自己为新增的商品生成id
        final long itemId = IDUtils.genItemId();
        // 2.表单/前端没填的属性 需要自己对商品属性补全
        item.setId(itemId);
        //  1-正常 2-下架 3-删除  注意删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        // 3.商品描述填入对应的pojo并补全属性
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        // 4.数据落库
        itemMapper.insert(item);
        itemDescMapper.insert(itemDesc);

        //发送商品添加消息
        jmsTemplate.send(topicDestination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });

        // 5.调用无参的ok 返回的E3Result携带了哪些信息？ status 200 msg ok
        return E3Result.ok();
    }

    /**
     * 商品描述 也对应一个表
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc getItemDescById(long itemId) {
        // 商品描述 查商品时先查缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(StringUtils.isNoneBlank(json)) {
                TbItemDesc res = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        // 查询的结果添加至缓存
        try {
            // set(key-string 前后缀, value-json字符串)
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            // expire(k, 过期时间)
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }
}
