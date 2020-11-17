package doom.utils;

import doom.http.Controller;
import doom.models.Request;
import doom.models.Response;
import doom.http.Route;
import doom.http.annotations.*;
import doom.middleware.MiddleWare;
import doom.middleware.MiddlewareAdder;
import doom.middleware.MiddlewareHandler;
import doom.server.DoomHttpHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationProcessor {
    public static void startProcessing(Class<?> projectClass, DoomHttpHandler handler){
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        annotationProcessor.loadAllRoutes(projectClass, annotationProcessor, handler);
    }

    private void loadAllRoutes(Class<?> projectClass, AnnotationProcessor annotationProcessor, DoomHttpHandler handler) {
        List<Class<?>> classes = Utils.getAllClasses(projectClass);

        for (Class<?> mClass : classes) {
            Path path = mClass.getAnnotation(Path.class);
            if (path != null) {
                Controller controller = new Controller(path.value());
                Object obj = Utils.getObjectForClass(mClass);
                Method[] methods = mClass.getMethods();

                assert obj != null : "Failed creating object of type " + mClass.getName();

                for (Method method : methods) {
                    annotationProcessor.processMethod(method, controller, obj);
                }

                if (mClass.isAnnotationPresent(MiddleWare.class))
                    annotationProcessor.processMiddleware(mClass.getAnnotation(MiddleWare.class), controller);

                handler.addController(controller);
            }
        }
    }

    public void processMethod(Method method, Controller controller, Object obj) {
        if (!checkValidMethod(method))
            return;

        HttpMethod httpMethod = null;
        String reqPath = "";

        if (method.isAnnotationPresent(GET.class)) {
            GET get = method.getAnnotation(GET.class);
            httpMethod = GET.class.getAnnotation(HttpMethod.class);
            reqPath = get.value();
        } else if (method.isAnnotationPresent(POST.class)) {
            POST post = method.getAnnotation(POST.class);
            httpMethod = POST.class.getAnnotation(HttpMethod.class);
            reqPath = post.value();
        } else if (method.isAnnotationPresent(PUT.class)) {
            PUT put = method.getAnnotation(PUT.class);
            httpMethod = PUT.class.getAnnotation(HttpMethod.class);
            reqPath = put.value();
        } else if (method.isAnnotationPresent(PATCH.class)) {
            PATCH patch = method.getAnnotation(PATCH.class);
            httpMethod = PATCH.class.getAnnotation(HttpMethod.class);
            reqPath = patch.value();
        } else if (method.isAnnotationPresent(HEAD.class)) {
            HEAD head = method.getAnnotation(HEAD.class);
            httpMethod = HEAD.class.getAnnotation(HttpMethod.class);
            reqPath = head.value();
        } else if (method.isAnnotationPresent(DELETE.class)) {
            DELETE delete = method.getAnnotation(DELETE.class);
            httpMethod = DELETE.class.getAnnotation(HttpMethod.class);
            reqPath = delete.value();
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

    public boolean checkValidMethod(Method method){
        if (method.getReturnType() != Response.class){
            return false;
        }

        if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != Request.class){
            System.out.println("Only one parameter type is allowed of type Request");
            return false;
        }

        return true;
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
