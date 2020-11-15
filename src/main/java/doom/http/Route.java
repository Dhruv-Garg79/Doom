package doom.http;

import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.middleware.MiddlewareProcessor;
import doom.utils.PathUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route implements MiddlewareAdder {
    private Pattern pathPattern;
    private String path;
    private HttpMethods method;
    private RequestHandler handler;
    MiddlewareProcessor middlewareProcessor;

    public Route(String path, HttpMethods method, RequestHandler handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
        this.middlewareProcessor = new MiddlewareProcessor();

        this.pathPattern = PathUtils.pathToRegex(path);
    }

    public Response processRequest(Request request) throws IOException {
        if (!middlewareProcessor.process(request))
            return null;

        return handler.handle(request);
    }

    public Matcher matchPath(String path){
        Matcher matcher = pathPattern.matcher(path);
        return matcher;
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

    @Override
    public void addMiddleware(MiddlewareHandler middleWareHandler) {
        middlewareProcessor.addMiddleware(middleWareHandler);
    }
}
