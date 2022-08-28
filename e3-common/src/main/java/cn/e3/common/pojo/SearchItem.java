package cn.e3.common.pojo;

import java.io.Serializable;

/**
 * 商品检索POJO 要了item部分属性
 * 接收dao层表关联查询得到的字段信息
 *
 * @author yesyoungbaby
 * @date 2021/11/27 21:02
 */
public class SearchItem implements Serializable {
    private String id;
    private String title;
    private String sellPoint;
    private Long price;
    private String image;
    private String categoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
