package com.jib.wwyt.websocket;

/**
 * @author: Administrator
 * @date: 2020/6/18 18:15
 * @description:
 */

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


import com.jib.wwyt.controller.RyzgzwController;
import com.jib.wwyt.controller.XdkjRydwController;
import com.jib.wwyt.utils.PropertyUtil;
import org.apache.log4j.Logger;
/**
 * 人员定位websocket
 * pujie
 * 2020-04-21
 * */
@ServerEndpoint(value = "/websocket/rydw")
public class WebSocketUtil {
    //存放websocket 的线程安全的无序的集合
    private  static CopyOnWriteArraySet<WebSocketUtil> websocket = new CopyOnWriteArraySet<WebSocketUtil>();
    private Session session;
    public static CopyOnWriteArraySet<WebSocketUtil> getWebsocket() {
        return websocket;
    }
    public static void setWebsocket(CopyOnWriteArraySet<WebSocketUtil> websocket) {
        WebSocketUtil.websocket = websocket;
    }
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    private Logger logger = Logger.getLogger(WebSocketUtil.class);
    //用来记录当前在线连接数
    private static int onLineCount = 0;

    //用来存放每个客户端对应的WebSocketServer对象
    private static ConcurrentHashMap<String, WebSocketUtil> webSocketMap = new ConcurrentHashMap<String, WebSocketUtil>();

    /**
     * 连接建立后触发的方法
     */
    @OnOpen
    public void onOpen(@PathParam("ip")String ip, Session session) throws Exception{
        this.session = session;
        session.getId();
        logger.info("onOpen"+session.getId());
//        if(ip==null){
//            ip="192.168.0.144";
//        }
        WebSocketMapUtil.put(session.getId(),this);
        webSocketMap.put(session.getId(),this);  //将当前对象放入map中
        String rydwflag= PropertyUtil.getProperty("rydwflag");
        if(rydwflag.equals("0")){
            XdkjRydwController XdkjRydwController=new XdkjRydwController();
            XdkjRydwController.mdfkzcf();
        }

//        String result="成功";
////        String result=JSONObject.toJSONString(RyzgzwController.rydwMap, SerializerFeature.WriteNullStringAsEmpty);
//        WebSocketUtil myWebSocket = WebSocketMapUtil.get(session.getId());
//
//        //返回消息给Web Socket客户端（浏览器）
//        myWebSocket.sendMessage(result);
    }


    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose(@PathParam("ip")String ip){
        //从map中删除
        WebSocketMapUtil.remove(session.getId());
        if(session.getId()==null||"".equals(session.getId())){
            webSocketMap = new ConcurrentHashMap<String, WebSocketUtil>();
        }else {
            webSocketMap.remove(session.getId());  //根据ip(key)移除WebSocketServer对象
        }
        logger.info("====== onClose:"+session.getId()+" ======");
    }


    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String params, Session session) throws Exception{
        //获取服务端到客户端的通道
        WebSocketUtil myWebSocket = WebSocketMapUtil.get(session.getId());
        logger.info("收到来自"+session.getId()+"的消息"+params);
//        String result = "收到来自"+session.getId()+"的消息"+params;
//        String result= JSONObject.toJSONString(RyzgzwController.rydwMap, SerializerFeature.WriteNullStringAsEmpty);
        //返回消息给Web Socket客户端（浏览器）
//        myWebSocket.sendMessage(result);
    }


    /**
     * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error){
        logger.info(session.getId()+"连接发生错误"+error.getMessage());
        error.printStackTrace();
    }

//    public void sendMessage(int status,String message,Object datas) throws IOException {
//        JSONObject result = new JSONObject();
//        result.put("status", status);
//        result.put("message", message);
//        result.put("datas", datas);
//        this.session.getBasicRemote().sendText(result.toString());
//    }


    /**
     * 给当前用户发送消息
     * @param message
     */
    public void sendMessage(String message){
        try{
            //getBasicRemote()是同步发送消息,这里我就用这个了，推荐大家使用getAsyncRemote()异步
//            this.session.getBasicRemote().sendText(message);
            this.session.getAsyncRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("发送数据错误:,ip:{},message:{}"+message);
        }
    }

    /**
     * 给所有用户发消息
     //     * @param message
     */
    public static void sendMessageAll(final String result){
        //使用entrySet而不是用keySet的原因是,entrySet体现了map的映射关系,遍历获取数据更快。
        Set<Map.Entry<String, WebSocketUtil>> entries = webSocketMap.entrySet();
        for (Map.Entry<String, WebSocketUtil> entry : entries) {
            final WebSocketUtil WebSocketUtil = entry.getValue();
            //这里使用线程来控制消息的发送,这样效率更高。
            new Thread(new Runnable() {
                public void run() {
                    WebSocketUtil.sendMessage(result);
                }
            }).start();
        }
    }

    public static void setWebSocketMap(ConcurrentHashMap<String, WebSocketUtil> webSocketMap) {
        WebSocketUtil.webSocketMap = webSocketMap;
    }
    public static ConcurrentHashMap<String, WebSocketUtil> getWebSocketMap() {
        return webSocketMap;
    }

}
