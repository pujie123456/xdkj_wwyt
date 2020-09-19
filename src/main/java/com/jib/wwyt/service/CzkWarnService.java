package com.jib.wwyt.service;

import com.jib.wwyt.model.CzkWarn;

import java.util.List;
import java.util.Map;

/**
 * @author: pujie
 * @date: 2020/6/19 16:56
 * @description:车载卡报警
 */
public interface CzkWarnService {
    int insert(CzkWarn CzkWarn);
    //根据公司和卡号进行报警查询
    List<Map> findbycompany(Map map);
}
