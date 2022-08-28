package cn.e3.content.service;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/11/12 17:50
 */
public interface ContentCategoryService {
    /**
     * 获取内容分类目录
     * @param parentId
     * @return
     */
    List<EasyUITreeNode> getContentCatList(long parentId);

    /**
     * 增加内容分类
     * @param parentId
     * @param name
     * @return
     */
    E3Result addContentCategory(long parentId, String name);
}
