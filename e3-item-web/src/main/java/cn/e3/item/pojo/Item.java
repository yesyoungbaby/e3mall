package cn.e3.item.pojo;

import cn.e3.pojo.TbItem;

/**
 * 通过此类了解继承的意义：
 *  需要用到通用的属性
 *  不同类也有不同的需求
 *
 * @author yesyoungbaby
 * @date 2021/12/5 10:29
 */
public class Item extends TbItem {

    public Item(TbItem tbItem) {
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
    }

    /**
     * 商品图片属性image将商品添加时的多张图片的url存在了一起，中间用，隔开了
     * 为了配合前端取图片地址item.images[0]，将其转换为string数组
     * @return
     */
    public String[] getImages() {
        String images = this.getImage();
        if (images != null && !"".equals(images)) {
            String[] strings = images.split(",");
            return strings;
        }
        return null;
    }
}
