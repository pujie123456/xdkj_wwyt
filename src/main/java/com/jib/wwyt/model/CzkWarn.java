package com.jib.wwyt.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: pujie
 * @date: 2020/6/19 13:06
 * @description:车载卡报警历史表
 */
@Data
public class CzkWarn {
    private int id;
    private String warntype;//报警类型
    private Timestamp warntime;//报警时间
    private String devicenum;//报警卡号
    private String warnspeed;//报警速度值
    private String warncarname;//报警车辆名称
    private int warnspeedlimit;//报警时速度限制
    private String warnxcross;//报警时纬度
    private String warnycross;//报警时经度
}
