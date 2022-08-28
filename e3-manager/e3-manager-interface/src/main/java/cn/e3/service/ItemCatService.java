package cn.e3.service;

import java.util.List;

import cn.e3.common.pojo.EasyUITreeNode;

/**
 * 商品类目服务
 * @author
 */
public interface ItemCatService {

	/**
	 * 获取商品类目
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getItemCatlist(long parentId);
}
