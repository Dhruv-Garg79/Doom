package doom.http;

import doom.models.Request;
import doom.models.Response;

/** Interface used to handle all http requests */
public interface RequestHandler {
    /**
     * handles http request
     *
     * @param request - The request Object
     * @return response - The response sent to client
     */
    Response handle(Request request);
}
