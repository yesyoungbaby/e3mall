package cn.e3.common.pojo;

import java.io.Serializable;

/**
 *
 * 符合EasyUI数据格式的节点信息
 * manager-web中新增商品功能，做商品类目选择时
 *
 * @author yesyoungbaby
 * @date 2021/10/21 16:34
 */
public class EasyUITreeNode implements Serializable {

    private long id;
    private String text;
    /**
     * 商品节点是顶级节点（有子节点）为closed，如果没有子节点则为open
     */
    private String state;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
