package doom.utils;

import doom.http.Controller;
import doom.http.Response;
import doom.http.Route;
import doom.http.annotations.*;
import doom.middleware.MiddleWare;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationProcessor {
    public void processMethod(Method method, Controller controller, Object obj) {
        HttpMethod httpMethod = null;
        String reqPath = "";

        if (method.isAnnotationPresent(GET.class)) {
            GET get = method.getAnnotation(GET.class);
            httpMethod = GET.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        } else if (method.isAnnotationPresent(POST.class)) {
            POST get = method.getAnnotation(POST.class);
            httpMethod = POST.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        } else if (method.isAnnotationPresent(PUT.class)) {
            PUT get = method.getAnnotation(PUT.class);
            httpMethod = PUT.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        } else if (method.isAnnotationPresent(PATCH.class)) {
            PATCH get = method.getAnnotation(PATCH.class);
            httpMethod = PATCH.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        } else if (method.isAnnotationPresent(HEAD.class)) {
            HEAD get = method.getAnnotation(HEAD.class);
            httpMethod = HEAD.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        } else if (method.isAnnotationPresent(DELETE.class)) {
            DELETE get = method.getAnnotation(DELETE.class);
            httpMethod = DELETE.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        }

        if (httpMethod != null) {
            Route route =
                    new Route(
                            reqPath,
                            httpMethod.value(),
                            request -> {
                                String msg;
                                try {
                                    return (Response) method.invoke(obj, request);
                                } catch (IllegalAccessException e) {
                                    msg = e.getLocalizedMessage();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                    msg = e.getLocalizedMessage();
                                }

                                return Response.error(msg);
                            });

            if (method.isAnnotationPresent(MiddleWare.class))
                processMiddleware(method.getAnnotation(MiddleWare.class), route);

            controller.addRoute(route);
        }
    }

    public void processMiddleware(MiddleWare middleWare, MiddlewareAdder middlewareAdder){
        Class<? extends MiddlewareHandler>[] classes = middleWare.value();

        for (Class<? extends MiddlewareHandler> middlewareHandler : classes){
            MiddlewareHandler handler = (MiddlewareHandler) Utils.getObjectForClass(middlewareHandler);
            if (handler != null)
                middlewareAdder.addMiddleware(handler);
        }
    }
}
