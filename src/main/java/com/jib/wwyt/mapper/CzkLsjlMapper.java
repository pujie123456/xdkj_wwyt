package com.jib.wwyt.mapper;


import com.jib.wwyt.model.CzkLsjl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 16:12
 * @description:车载卡历史记录mapper
 */
@Component
public interface CzkLsjlMapper {
    //插入车载卡历史记录
    int insert(CzkLsjl CzkLsjl);
    //查询
    List<Map> findhistorybytime(Map map );
    //获取最新x,y轴
    List<Map> findbydevicenumnewest(Map map );
}
