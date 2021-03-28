package com.sephinor.kanglong.mapper;

import com.sephinor.common.entity.Category;
import com.sephinor.common.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 品类
 */

public interface CategoryMapper extends Mapper<Category> , IdListMapper<Category,Long>{

    /**
     * 查询所有三级品类,名称是全民(name1 / name2 / name3 )
     * @return
     */
    @Select("select t3.id id , CONCAT(t1.name,' / ', t2.name,' / ' , t3.name) name from t_category t1 LEFT OUTER JOIN t_category t2 on t1.id = t2.parent_id LEFT OUTER JOIN t_category t3 on t2.id = t3.parent_id WHERE t1.parent_id = 0")
    List<Category> findAll();

    /**
     * 查询指定品牌所属的所有品类
     */
    @Select("select t3.id id , CONCAT(t1.name,' / ', t2.name,' / ' , t3.name) name from t_category t1 LEFT OUTER JOIN t_category t2 on t1.id = t2.parent_id LEFT OUTER JOIN t_category t3 on t2.id = t3.parent_id WHERE t1.parent_id = 0 and t3.id in (select t.category_id from t_category_brand t where t.brand_id = #{bid})")
    List<Category> findByBrandId(@Param("bid") Long bid) ;

    /**
     * 查询树形列表集合
     */
    @Select("select t.id , t.name c1name , '' c2name , '' c3name , t.name fullname from t_category t where t.parent_id = 0 union select t2.id , '' c1name , t2.name c2name , '' c3name , concat(t1.name , '/' , t2.name) fullname from t_category t1  LEFT OUTER JOIN t_category t2 on t1.id = t2.parent_id where t1.parent_id = 0 union select t3.id , '' c1name , '' c2name , t3.name c3name , concat(t1.name , '/' , t2.name , '/' , t3.name) fullname from t_category t1  LEFT OUTER JOIN t_category t2 on t1.id = t2.parent_id  LEFT OUTER JOIN t_category t3 on t2.id = t3.parent_id where t1.parent_id = 0 order by id , fullname")
    List<CategoryVO> findTree() ;


}
