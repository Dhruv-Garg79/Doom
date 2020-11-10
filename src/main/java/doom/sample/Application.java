package doom.sample;

import doom.server.DoomServer;

class Application {
    public static void main(String[] args) {
        DoomServer server = new DoomServer();
        server.addGlobalMiddleWare(new LogMiddleware());
        server.start();
    }
}
