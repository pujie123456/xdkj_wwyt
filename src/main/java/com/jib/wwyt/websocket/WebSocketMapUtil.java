package com.jib.wwyt.websocket;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: Administrator
 * @date: 2020/6/18 18:14
 * @description:
 */
public class WebSocketMapUtil {

    public static ConcurrentMap<String, WebSocketUtil> webSocketMap = new ConcurrentHashMap<>();

    public static void put(String key, WebSocketUtil myWebSocketServer){
        webSocketMap.put(key, myWebSocketServer);
    }

    public static WebSocketUtil get(String key){
        return webSocketMap.get(key);
    }

    public static void remove(String key){
        webSocketMap.remove(key);
    }

    public static Collection<WebSocketUtil> getValues(){
        return webSocketMap.values();
    }
}