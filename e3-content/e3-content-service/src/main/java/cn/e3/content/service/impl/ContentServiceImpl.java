package cn.e3.content.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.util.JedisClient;
import cn.e3.common.util.JsonUtils;
import cn.e3.content.service.ContentService;
import cn.e3.mapper.TbContentMapper;
import cn.e3.pojo.TbContent;
import cn.e3.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/11/22 9:17
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper mapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());

        mapper.insert(content);

        // 增删改时需要将表的改动同步到缓存
        // 如何同步？删除缓存中的该部分数据，这样再查询时就重新走db，取最新数据重新缓存
        jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentsByCid(long cid) {

        // 先查缓存 k-cid v-content
        // 缓存类型hash
        // 基本数据类型long转str cid+""，而上面的包装类型Long直接toString()
        try {
            String json = jedisClient.hget(CONTENT_LIST, cid+"");
            if(StringUtils.isNoneBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 没有缓存再查库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        // 查询结果包含大文本content，selectByExample()不包含
        List<TbContent> list = mapper.selectByExampleWithBLOBs(example);

        // 将查询结果加入缓存
        try {
            jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
