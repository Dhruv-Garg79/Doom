package doom;

import com.sun.net.httpserver.HttpServer;
import doom.annotations.*;
import doom.http.Controller;
import doom.http.MiddleWareHandler;
import doom.http.Response;
import doom.http.Route;
import doom.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DoomServer {
    private final DoomHttpHandler handler = new DoomHttpHandler();

    public void start() {
        try {
            loadAllRoutes();

            Logger logger = Logger.getLogger("test");
            ExecutorService service = Executors.newFixedThreadPool(5);
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            httpServer.setExecutor(service);
            httpServer.createContext("/", handler);
            httpServer.start();
            logger.info("started");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGlobalMiddleWare(MiddleWareHandler middleWareHandler){
        handler.addMiddleware(middleWareHandler);
    }

    private void loadAllRoutes() {
        String packageName = DoomServer.class.getPackageName();
        List<Class<?>> classes = Utils.getClassesInPackage(packageName);

        for (Class<?> mClass : classes) {
            Path path = mClass.getAnnotation(Path.class);
            if (path != null) {
                Controller controller = new Controller(path.value());
                Object obj = getObjectForClass(mClass);
                Method[] methods = mClass.getMethods();

                assert obj != null : "Failed creating object of type " + mClass.getName();

                for (Method method : methods) {
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
                        controller.addRoute(
                                new Route(
                                        path.value() + reqPath,
                                        httpMethod.value(),
                                        request -> {
                                            String msg;
                                            try {
                                                System.out.println("In method");
                                                return (Response) method.invoke(obj, request);
                                            } catch (IllegalAccessException e) {
                                                msg = e.getLocalizedMessage();
                                            } catch (InvocationTargetException e) {
                                                e.printStackTrace();
                                                msg = e.getLocalizedMessage();
                                            }

                                            return Response.error(msg);
                                        }));
                    }
                }

                handler.addController(controller);
            }
        }
    }

    public <T> Object getObjectForClass(Class<T> mClass){
        Constructor<T> constructor = null;
        try {
            constructor = mClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
