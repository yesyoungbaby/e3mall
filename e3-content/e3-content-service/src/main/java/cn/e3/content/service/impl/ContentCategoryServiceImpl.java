package cn.e3.content.service.impl;

import cn.e3.common.pojo.E3Result;
import cn.e3.common.pojo.EasyUITreeNode;
import cn.e3.content.service.ContentCategoryService;
import cn.e3.mapper.TbContentCategoryMapper;
import cn.e3.pojo.TbContentCategory;
import cn.e3.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author yesyoungbaby
 * @date 2021/11/12 17:50
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        // 根据parentid查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        //执行查询，得到内容目录列表
        List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);

        //内容目录列表转换为要求的EasyUITreeNode格式列表
        List<EasyUITreeNode> nodeList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : catList) {

            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()? "closed":"open");

            nodeList.add(node);
        }
        return nodeList;
    }

    /**
     * 传来了部分属性
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public E3Result addContentCategory(long parentId, String name) {
        // 创建pojo
        TbContentCategory contentCategory = new TbContentCategory();
        // 属性设值
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        // 1-正常 2-删除 新添加的节点为正常
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        // 新添加的节点为叶子节点
        contentCategory.setIsParent(false);
        // 落库
        contentCategoryMapper.insert(contentCategory);

        // 处理其父节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        // 因为有了新的子节点，所以要保证该字段为true
        if(!parent.getIsParent()) {
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        return E3Result.ok(contentCategory);
    }
}
