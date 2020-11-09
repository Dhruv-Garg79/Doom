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
        Request request = new Request(exchange, "api/name?id=231&name=john&lastname=doe");

        assertAll(
                () -> assertEquals(request.getQuery("id"), "231"),
                () -> assertEquals(request.getQuery("name"), "john"),
                () -> assertEquals(request.getQuery("lastname"), "doe"),
                () -> assertNull(request.getQuery("email")));
    }
}
