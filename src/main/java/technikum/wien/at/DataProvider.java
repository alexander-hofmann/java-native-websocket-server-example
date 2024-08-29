package technikum.wien.at;

import me.jittagornp.example.websocket.WebSocket;

public interface DataProvider {

    public void start(final WebSocket webSocket);
    public void stop();

}