package com.jib.wwyt.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: pujie
 * @date: 2020/6/19 13:02
 * @description:车载卡信息实体类
 */
@Data
public class CzkCard {
    private int id;
    private  String devicenum; //卡号
    private  String companynum;//公司编码
    private  String phonenum ;//车载卡使用者手机号
    private String carname;//车辆名称
    private  int speedlimit;//限制速度
    private  String urlandport;//车载卡请求连接和端口号

}
