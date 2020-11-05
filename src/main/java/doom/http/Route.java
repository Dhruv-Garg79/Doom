package doom.http;

public class Route {
    private String path;
    private HttpMethods method;
    private RequestHandler handler;

    public Route(String path, HttpMethods method, RequestHandler handler){
        this.path = path;
        this.method = method;
        this.handler = handler;
    }

    public Response processRequest(Request request){
        return handler.handle(request);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethods getMethod() {
        return method;
    }

    public void setMethod(HttpMethods method) {
        this.method = method;
    }

    public RequestHandler getHandler() {
        return handler;
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }
}
