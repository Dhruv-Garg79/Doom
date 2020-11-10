package doom.http;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestParseQueryTest {
    @Mock HttpExchange exchange;

    @Test
    public void parsePathForQueries() {
        Mockito.when(exchange.getRequestMethod()).thenReturn("GET");
        Mockito.when(exchange.getRequestURI().getPath()).thenReturn("api/resource?id=231&name=john&lastname=doe");
        Request request = new Request(exchange);

        assertAll(
                () -> assertEquals(request.getQueryParam("id"), "231"),
                () -> assertEquals(request.getQueryParam("name"), "john"),
                () -> assertEquals(request.getQueryParam("lastname"), "doe"),
                () -> assertNull(request.getQueryParam("email")));
    }
}
