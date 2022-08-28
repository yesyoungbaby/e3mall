package cn.e3.mapper;

import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbItemMapper {

    /**
     * 主键查商品
     * @param id
     * @return
     */
    TbItem selectByPrimaryKey(Long id);

    /**
     * 查询列表
     * @param example 查询条件
     * @return  List
     */
    List<TbItem> selectByExample(TbItemExample example);


    /**
     * 商品落库
     * @param record
     * @return
     */
    int insert(TbItem record);

    /**
     * 不是所有字段都有值
     * @param record
     * @return
     */
    int insertSelective(TbItem record);

    /**
     * 最简单的改动
     * @param record
     * @return
     */
    int updateByPrimaryKey(TbItem record);

    /**
     * @Param：
     *  1. 首先是mybatis的注解
     *  2. 其作用
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByExample(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByPrimaryKeySelective(TbItem record);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    int deleteByExample(TbItemExample example);


    /**
     * 查表中记录总数
     * @param example 为啥是条件查询？
     * @return
     */
    int countByExample(TbItemExample example);

}