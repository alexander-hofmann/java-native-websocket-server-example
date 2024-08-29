package technikum.wien.at;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.jittagornp.example.websocket.WebSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MeasurementDataProvider implements DataProvider {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

    private Thread t;
    private boolean stopped = false;
    private WebSocket websocket;

    @Override
    public void start(final WebSocket webSocket) {
        this.websocket = webSocket;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                Date oldDate = new Date();
                while (!stopped) {
                    Date nowDate = new Date();
                    if (nowDate.after(oldDate)) {
                        MeasurementValue mv = new MeasurementValue(Math.sin(Math.toRadians((nowDate.getTime() / 100.0) % 360)));
                        String json = gson.toJson(mv);
                        try {
                            webSocket.send(json);
                        } catch (IllegalStateException e) {
                            stop();
                        }
                    }
                    try {
                        Thread.sleep(100);
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
