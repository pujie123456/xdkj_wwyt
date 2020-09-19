package com.jib.wwyt.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: pujie
 * @date: 2020/6/19 13:11
 * @description:车载卡历史记录
 */
@Data
public class CzkLsjl {
    private int id;
    private  String devicenum;//卡号
    private Timestamp sendtime;//传送时间
    private String xCross;//地点纬度 X
    private String yCross;//地点经度 y
    private String velocity;//时速
    private String direction;//角度
    private String horizontalfactor;//水平因子
    private String alarm;//报警
    private String voltage;//电压
    private String electricity;//电量
    private Timestamp savetime;//保存时间
    private String carname;//电量
    private int status;//状态 0为移动，1为静止
}
