package doom.server;

import com.sun.net.httpserver.HttpServer;
import doom.http.annotations.Path;
import doom.http.Controller;
import doom.middleware.MiddleWare;
import doom.middleware.MiddlewareHandler;
import doom.utils.AnnotationProcessor;
import doom.utils.Utils;

import java.io.IOException;
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

    public void addGlobalMiddleWare(MiddlewareHandler middleWareHandler){
        handler.addMiddleware(middleWareHandler);
    }

    private void loadAllRoutes() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        String packageName = DoomServer.class.getPackageName();
        List<Class<?>> classes = Utils.getClassesInPackage(packageName);

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
}
