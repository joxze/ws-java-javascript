package com.josep.ws.controllers;

import com.josep.ws.handler.MyStompSessionHandler;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

@Controller
public class ClientController {

    @GetMapping("/client")
    public @ResponseBody void index() {
        System.out.println("client start.");
        String url = "ws://localhost:8080/josep-websocket";
        StompSessionHandler sessionHandler = new MyStompSessionHandler();

        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
            StompSession stompSession = stompClient.connect(url, sessionHandler).get();

            stompSession.send("topic/greetings", "Hello new user");
            System.out.println("success send");

            stompSession.subscribe("/topic/foo", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    System.out.println("success subscribe");
                    String msg = (String) payload;
                    System.out.println("Received : " + msg);
                }

            });
//            Thread.sleep(1000);
//            stompSession.send("topic/greetings", "Hello new user 2");
            System.out.println("success send");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("client end.");
    }

}
