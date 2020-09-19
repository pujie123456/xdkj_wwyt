package com.jib.wwyt.mapper;

import com.jib.wwyt.model.CzkCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 13:24
 * @description:车载卡卡片信息mapper
 */
@Component
public interface CzkCardMapper {
    int insert(CzkCard record);
    List<CzkCard>  selectbydevicenum(Map map);
    //根据企业编码查找车载卡实体类
    List<Map> findcardbycompanynum(Map map);
    //根据编号修改车载卡信息
    int updatecard(Map map);

     // <!--根据企业编码查找车载卡实体类列表-->
     List<Map> findallcardlist(Map map);

}
