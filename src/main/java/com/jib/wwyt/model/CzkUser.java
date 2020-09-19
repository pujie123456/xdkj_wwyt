package com.jib.wwyt.model;

import lombok.Data;

/**
 * @author: pujie
 * @date: 2020/6/23 13:00
 * @description:车载卡
 */
@Data
public class CzkUser {
    private int id;
    private String username; //用户名
    private String passwd; //密码
    private String companynum; //企业编号
    private String status;//状态
    private String mapurl;//地图路径
}
