package com.eats.websocket;

import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocketサービス
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    //セッションオブジェクトを格納
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * 接続成功時に呼び出されるメソッド
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("クライアント" + sid + "接続を確認");
        sessionMap.put(sid, session);
    }

    /**
     * クライアントメッセージ受信後に呼び出されるメソッ
     *
     * @param message クライアントから送信されたメッセージ
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("クライアントからのメッセージを受信：" + sid + "の情" + message);
    }

    /**
     * 接続切断時に呼び出されるメソッド
     *
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("接続が切断されました:" + sid);
        sessionMap.remove(sid);
    }

    /**
     * 一斉送信
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //サーバーからクライアントにメッセージを送信
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
