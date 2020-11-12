package doom.http;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Response {
    private final Object data;
    private int statusCode = 200;

    public Response(String message) {
        this.data = message;
    }

    public Response(String message, int statusCode) {
        this.data = message;
        this.statusCode = statusCode;
    }

    public Response(JSONObject jsonObject) {
        this.data = jsonObject;
    }

    public Response(File file) {
        this.data = file;
    }

    public static Response error(String msg) {
        return new Response(msg, 504);
    }

    public static Response notFound() {
        return new Response("Not found", 404);
    }

    public void send(HttpExchange exchange) throws IOException {
        if (this.data instanceof String) sendString((String) data, exchange);
        else if (this.data instanceof JSONObject) sendJSON((JSONObject) data, exchange);
        else if (this.data instanceof File) sendFile((File) data, exchange);
        else System.out.println("Not Allowed object of type " + this.data.getClass().getName());
    }

    private void sendString(String str, HttpExchange exchange) throws IOException {
        sendBytes(str.getBytes(), exchange);
    }

    private void sendFile(File file, HttpExchange exchange) {
        int count = 0;
        byte[] buffer = new byte[4096];

        try {
            OutputStream outputStream = exchange.getResponseBody();
            InputStream inputStream = Files.newInputStream(file.toPath(), StandardOpenOption.READ);

            exchange.sendResponseHeaders(statusCode, Files.size(file.toPath()));

            while ((count = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, count);
            }

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendJSON(JSONObject jsonObject, HttpExchange exchange) throws IOException {
        sendBytes(jsonObject.toString().getBytes(), exchange);
    }

    private void sendBytes(byte[] data, HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(statusCode, data.length);
        outputStream.write(data);

        outputStream.close();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
