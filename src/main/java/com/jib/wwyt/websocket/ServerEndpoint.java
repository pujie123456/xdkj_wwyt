package com.jib.wwyt.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.server.ServerEndpointConfig.Configurator;
/**
 * @author: Administrator
 * @date: 2020/6/19 9:15
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ServerEndpoint {
    //注释类应映射到的URI或URI模板（必写的参数）。
    String value();//子协议
    String[] subprotocols() default {};
    //解码器   输入websocket消息 输出java对象
    Class<? extends Decoder>[] decoders() default {};
    //编码器  输入java对象 输出websocket
    Class<? extends Encoder>[] encoders() default {};
    //配置器
    Class<? extends Configurator> configurator() default Configurator.class;
}
