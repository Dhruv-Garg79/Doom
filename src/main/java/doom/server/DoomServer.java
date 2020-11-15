package doom.server;

import com.sun.net.httpserver.HttpServer;
import doom.middleware.MiddlewareHandler;
import doom.utils.AnnotationProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DoomServer {
    private final DoomHttpHandler handler = new DoomHttpHandler();

    public DoomServer(Class<?> projectClass){
        AnnotationProcessor.startProcessing(projectClass, handler);
    }

    public void start() {
        try {
            Logger logger = Logger.getLogger("test");
            ExecutorService service = Executors.newFixedThreadPool(5);
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            httpServer.setExecutor(service);
            httpServer.createContext("/", handler);
            httpServer.start();
            logger.info("started");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                httpServer.stop(0);
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGlobalMiddleWare(MiddlewareHandler middleWareHandler){
        handler.addMiddleware(middleWareHandler);
    }
}
