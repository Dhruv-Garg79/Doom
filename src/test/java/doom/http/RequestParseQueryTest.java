package doom.http;

import com.sun.net.httpserver.HttpExchange;
import doom.models.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestParseQueryTest {
    @Mock HttpExchange exchange;

    @Test
    public void parsePathForQueries() throws URISyntaxException {
        URI uri = new URI("api/resource?id=231&name=john&lastname=doe");

        Mockito.when(exchange.getRequestMethod()).thenReturn("GET");
        Mockito.when(exchange.getRequestURI()).thenReturn(uri);

        Request request = new Request(exchange);

        assertAll(
                () -> assertEquals(request.getQueryParam("id"), "231"),
                () -> assertEquals(request.getQueryParam("name"), "john"),
                () -> assertEquals(request.getQueryParam("lastname"), "doe"),
                () -> assertNull(request.getQueryParam("email")));
    }
}
