package com.jib.wwyt.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: pujie
 * @date: 2020/7/22 18:05
 * @description:毛东方信息
 */
@Component
@ConfigurationProperties(prefix = "demo")
@PropertySource("config.properties")
@Data
public class MDF {
    private String tenantId;
    private String userId;
    private String  rydwurl;
    private  String passwd;
}
