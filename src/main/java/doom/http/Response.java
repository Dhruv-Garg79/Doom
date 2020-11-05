package doom.http;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    String msg;
    int statusCode = 200;

    public Response() {}

    public Response(String message) {
        this.msg = message;
    }

    public Response(String message, int statusCode) {
        this.msg = message;
        this.statusCode = statusCode;
    }

    public static Response error(String msg) {
        return new Response(msg, 504);
    }

    public static Response notFound() {
        return new Response("Not found", 404);
    }

    public void send(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        byte[] data = msg.getBytes();

        exchange.sendResponseHeaders(statusCode, data.length);
        outputStream.write(data);

        outputStream.close();
    }

    public void sendString(String str) {}

    public void sendFile() {}

    public void sendJSON(JSONObject jsonObject) {}

    public void sendBytes(byte[] data) {}
}
