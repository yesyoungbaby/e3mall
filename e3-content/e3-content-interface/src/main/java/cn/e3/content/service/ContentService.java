package cn.e3.content.service;

import cn.e3.common.pojo.E3Result;
import cn.e3.pojo.TbContent;

import java.util.List;

/**
 * 内容服务
 *
 * @author yesyoungbaby
 * @date 2021/11/22 9:13
 */
public interface ContentService {
    /**
     * 添加内容
     * @param content
     * @return
     */
    E3Result addContent(TbContent content);

    /**
     * 根据内容分类id查询内容列表
     * @param cid 内容分类id
     * @return
     */
    List<TbContent> getContentsByCid(long cid);
}
