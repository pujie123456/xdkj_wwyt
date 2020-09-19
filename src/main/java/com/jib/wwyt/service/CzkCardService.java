package com.jib.wwyt.service;

import com.jib.wwyt.model.CzkCard;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 14:19
 * @description:车载卡卡号信息
 */
public interface CzkCardService  {
    //查询公司下所有车载卡
    List<CzkCard> selectbydevicenum(Map map);
    //根据企业编码查找车载卡实体类
    List<Map> findcardbycompanynum(Map map);
    //根据编号修改车载卡信息
    int updatecard(Map map);
    // <!--根据企业编码查找车载卡实体类列表-->
     List<Map> findallcardlist(Map map);
}
