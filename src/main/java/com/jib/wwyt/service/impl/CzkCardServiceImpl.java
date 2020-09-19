package com.jib.wwyt.service.impl;

import com.jib.wwyt.mapper.CzkCardMapper;
import com.jib.wwyt.model.CzkCard;
import com.jib.wwyt.service.CzkCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 14:21
 * @description:车载卡卡号信息
 */
@Service("CzkCardService")
public class CzkCardServiceImpl implements CzkCardService {

    @Autowired
    private CzkCardMapper CzkCardMapper;
    //查询公司下所有车载卡
    public List<CzkCard> selectbydevicenum(Map map){
        return CzkCardMapper.selectbydevicenum(map);
    }

    //根据企业编码查找车载卡实体类
    public  List<Map> findcardbycompanynum(Map map){
        return CzkCardMapper.findcardbycompanynum(map);
    }

    //根据编号修改车载卡信息
    public int updatecard(Map map){
        return  CzkCardMapper.updatecard(map);
    }

    // <!--根据企业编码查找车载卡实体类列表-->
    public List<Map> findallcardlist(Map map){
        return  CzkCardMapper.findallcardlist(map);
    }
}
