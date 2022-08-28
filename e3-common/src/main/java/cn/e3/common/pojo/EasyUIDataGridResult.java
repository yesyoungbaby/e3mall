package cn.e3.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 首先是EasyUI要求的数据响应格式
 * 在manager-web的商品查询功能中
 * @author yesyoungbaby
 * @date 2021/10/20 16:33
 */
public class EasyUIDataGridResult implements Serializable {

    /**
     * 总记录数
     */
    private long total;
    /**
     * 记录列表 注意这是个list
     */
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
