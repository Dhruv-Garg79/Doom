package doom.sample;

import doom.models.Response;
import doom.server.DoomServer;

class Application {
    public static void main(String[] args) {
        DoomServer server = new DoomServer(Application.class);
        server.addGlobalMiddleWare(
                req -> {
                    System.out.println("global middleware");
                    return new Response("ok");
                });
        server.start();
    }
}
