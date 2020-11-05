package doom.http;

public interface RequestHandler {
    Response handle(Request request);
}
