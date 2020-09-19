package com.jib.wwyt.mapper;

import com.jib.wwyt.model.CzkLsjl;
import com.jib.wwyt.model.CzkWarn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 16:48
 * @description:车载卡报警Mapper
 */
@Component
public interface CzkWarnMapper {
    //插入车载卡历史记录
    int insert(CzkWarn CzkWarn);
    //根据公司和卡号进行报警查询
    List<Map> findbycompany(Map map);
}
