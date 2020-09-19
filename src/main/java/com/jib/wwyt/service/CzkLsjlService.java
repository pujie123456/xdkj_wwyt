package com.jib.wwyt.service;

import com.jib.wwyt.model.CzkLsjl;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 16:33
 * @description:车载卡历史记录service
 */
public interface CzkLsjlService {
    int insert(CzkLsjl CzkLsjl);
    //查询历史记录
    List<Map> findhistorybytime(Map map);

    //获取最新x,y轴
    List<Map> findbydevicenumnewest(Map map );
}
