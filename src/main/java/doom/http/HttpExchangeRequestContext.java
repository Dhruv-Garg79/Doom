package doom.http;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HttpExchangeRequestContext implements RequestContext {

    private final HttpExchange http;

    public HttpExchangeRequestContext(HttpExchange http) {
        this.http = http;
    }

    @Override
    public String getCharacterEncoding() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return http.getRequestHeaders().getFirst("Content-type");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return http.getRequestBody();
    }
}
