package doom.models;

import com.sun.net.httpserver.HttpExchange;
import doom.enums.MediaType;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class Response {
    private final Object data;
    private int statusCode = 200;
    private String contentType = null;

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

    public Response(MultiPart multiPart){
        this.data = multiPart;
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
        else if (this.data instanceof MultiPart) sendMultipart((MultiPart) data, exchange);
        else System.out.println("Not Allowed object of type " + this.data.getClass().getName());
    }

    private void sendMultipart(MultiPart multiPart, HttpExchange exchange){
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        setContentType(exchange, MediaType.FORM_DATA.getVal() + "; boundary=" + boundary);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            //TODO:: take care of content-length
            exchange.sendResponseHeaders(statusCode, 0);

            StringBuilder sb = new StringBuilder();
            sb.append(boundary);

            for (Map.Entry<String, Object> entry : multiPart.entrySet()) {
                sb.append("\r\nContent-Disposition: form-data; name=");
                sb.append("\"").append(entry.getKey()).append("\"");

                if (entry.getValue() instanceof File){
                    File file = (File) entry.getValue();
                    Path path = file.toPath();
                    String mimeType = URLConnection.guessContentTypeFromName(file.getName());

                    sb.append("; filename=\"").append(file.getName()).append("\"\r\n");
                    sb.append(";Content-Type=\"").append(mimeType).append("\"");

                    sb.append("\r\n\r\n");
                    outputStream.write(sb.toString().getBytes());
                    sb.setLength(0);
                    Files.copy(path, outputStream);
                }
                else{
                    sb.append("\r\n\r\n");
                    sb.append(entry.getValue());
                }

                sb.append("\r\n");
                sb.append(boundary);

                outputStream.write(sb.toString().getBytes());
                sb.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendString(String str, HttpExchange exchange) throws IOException {
        setContentType(exchange, MediaType.PLAIN_TEXT.getVal());
        sendBytes(str.getBytes(), exchange);
    }

    private void sendFile(File file, HttpExchange exchange) {
        setContentType(exchange, this.contentType);
        int count = 0;
        byte[] buffer = new byte[4096];

        try (OutputStream outputStream = exchange.getResponseBody();
             InputStream inputStream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {

            exchange.sendResponseHeaders(statusCode, Files.size(file.toPath()));

            while ((count = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendJSON(JSONObject jsonObject, HttpExchange exchange) throws IOException {
        setContentType(exchange, MediaType.JSON.getVal());
        sendBytes(jsonObject.toString().getBytes(), exchange);
    }

    private void sendBytes(byte[] data, HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(statusCode, data.length);
        outputStream.write(data);

        outputStream.close();
    }

    private void setContentType(HttpExchange exchange, String contentType){
        if (this.contentType != null)
            contentType = this.contentType;

        exchange.getResponseHeaders().set("Content-Type", contentType);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
