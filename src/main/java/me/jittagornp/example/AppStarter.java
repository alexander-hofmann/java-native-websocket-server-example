/*
 * Copyright 2021-Current jittagornp.me
 */
package me.jittagornp.example;

import me.jittagornp.example.websocket.*;
import technikum.wien.at.DataProvider;
import technikum.wien.at.DateTimeProvider;
import technikum.wien.at.MeasurementDataProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * @author jitta
 */
public class AppStarter {

    public static void main(final String[] args) throws IOException, NoSuchAlgorithmException {

        final WebSocketHandler<String> handler = new TextWebSocketHandler() {
            private HashMap<WebSocket, DataProvider> clients = new HashMap<WebSocket, DataProvider>();
            @Override
            public void onConnect(final WebSocket webSocket) {
                System.out.println("Client connected => " + webSocket);
            }

            @Override
            public void onMessage(final WebSocket webSocket, final String message) {
                System.out.println("Client message => " + message);
                if (message.contains("datetime")) {
                    DataProvider dp = new DateTimeProvider();
                    dp.start(webSocket);
                    clients.put(webSocket, dp);
                }
                if (message.contains("sinus")) {
                    DataProvider dp = new MeasurementDataProvider();
                    dp.start(webSocket);
                    clients.put(webSocket, dp);
                }
                webSocket.send(message + " ok.");
            }

            @Override
            public void onError(final WebSocket webSocket, final Throwable e) {
                System.out.println("Client error => " + webSocket);
                System.out.println(e);
            }

            @Override
            public void onDisconnect(final WebSocket webSocket, final CloseStatus status) {
                System.out.println("Client disconnected => " + webSocket);
                System.out.println("Close status => " + status);
                DataProvider dp = clients.remove(webSocket);
                dp.stop();
            }
        };

        WebSocketServer.port(8089)
                .addWebSocketHandler(handler)
                .start();
    }
}
