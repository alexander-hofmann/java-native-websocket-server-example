package technikum.wien.at;

import me.jittagornp.example.websocket.WebSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeProvider implements DataProvider {
    private Thread t;
    private boolean stopped = false;
    private WebSocket websocket;

    @Override
    public void start(final WebSocket webSocket) {
        this.websocket = webSocket;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                df.setTimeZone(tz);
                Date oldDate = new Date();
                while (!stopped) {
                    Date nowDate = new Date();
                    if (nowDate.after(oldDate)) {
                        String nowAsISO = df.format(nowDate);
                        try {
                            webSocket.send(nowAsISO);
                        } catch (IllegalStateException e) {
                            stop();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void stop() {
        System.out.println(String.format("Stopping thread with websocket %s", websocket.toString()));
        stopped = true;
    }
}
