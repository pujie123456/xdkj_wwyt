package com.jib.wwyt.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * spring boot websocket工具类
 * @author: pujie
 * @date: 2020/6/19 9:09
 * @description:
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocketUtil2 {
    //存放websocket 的线程安全的无序的集合
    private  static CopyOnWriteArraySet<WebSocketUtil2> websocket = new CopyOnWriteArraySet<WebSocketUtil2>();
    private Session session;
    public static CopyOnWriteArraySet<WebSocketUtil2> getWebsocket() {
        return websocket;
    }
    public static void setWebsocket(CopyOnWriteArraySet<WebSocketUtil2> websocket) {
        WebSocketUtil2.websocket = websocket;
    }
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    //用来存放每个客户端对应的WebSocketServer对象
    private static ConcurrentHashMap<String, WebSocketUtil2> webSocketMap = new ConcurrentHashMap<String, WebSocketUtil2>();
    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        websocket.add(this);     //加入set中
        // addOnlineCount();           //在线数加1
        System.out.println("进入onOpen方法");
        try {
            webSocketMap.put(session.getId(),this);  //将当前对象放入map中
            sendMessage("连接已建立成功.");
        } catch (Exception e) {
            System.out.println("IO异常");
        }
    }
    /**
     * 关闭通信连接
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        //关闭连接后将此socket删除
        websocket.remove(this);
        System.out.println("进入onClose方法");
    }
    /**
     * 获取客户端发来的信息
     */
    @OnMessage
    public void onMessage(String message){
        System.out.println("进入onMessage方法; message = " + message);
    }
    /**
     * 给客户端推送信息
     */
    public void sendMessage(String message) throws IOException {
        System.out.println("进入sendMessage方法");
        try {
            this.session.getBasicRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 异常方法
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("进入error方法");
        error.printStackTrace();
    }
    /**
     * 给所有用户发消息
     //     * @param message
     */
    public static void sendMessageAll(final String result){
        //使用entrySet而不是用keySet的原因是,entrySet体现了map的映射关系,遍历获取数据更快。
        Set<Map.Entry<String, WebSocketUtil2>> entries = webSocketMap.entrySet();
        for (Map.Entry<String, WebSocketUtil2> entry : entries) {
            final WebSocketUtil2 WebSocketUtil = entry.getValue();
            //这里使用线程来控制消息的发送,这样效率更高。
            new Thread(new Runnable() {
                public void run() {
                    try {
                        WebSocketUtil.sendMessage(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    //循环发送发送所有的websocket对象
//    CopyOnWriteArraySet<MyWebSocket> websocket = MyWebSocket.getWebsocket();
//          for (MyWebSocket myWebSocket : websocket) {
//        try {
//            myWebSocket.sendMessage("我要发消息了"+ Math.random());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
    public static ConcurrentHashMap<String, WebSocketUtil2> getWebSocketMap() {
        return webSocketMap;
    }
}
